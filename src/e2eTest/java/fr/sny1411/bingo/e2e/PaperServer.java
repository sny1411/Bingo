package fr.sny1411.bingo.e2e;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A Paper server with the plugin, started in build/e2e/server and deleted when it stops.
 */
public final class PaperServer implements AutoCloseable {
    private static final Duration START_TIMEOUT = Duration.ofMinutes(5);
    private static final Duration STOP_TIMEOUT = Duration.ofMinutes(1);
    private static final Duration LOG_TIMEOUT = Duration.ofSeconds(10);
    private static final Pattern ANSI_CODE = Pattern.compile("\u001B\\[[;\\d]*m");
    // RCON answers keep the colors as § codes
    private static final Pattern LEGACY_COLOR_CODE = Pattern.compile("§.");

    private final Path directory;
    private final Process process;
    private final List<String> log = new ArrayList<>();
    private final CountDownLatch started = new CountDownLatch(1);
    private final int port;
    private Rcon rcon;
    private int nextLogMarker = 1;

    private PaperServer(Path directory, Process process, Path logFile, int port) {
        this.directory = directory;
        this.process = process;
        this.port = port;
        Thread reader = new Thread(() -> readLog(logFile), "Paper server log");
        reader.setDaemon(true);
        reader.start();
    }

    static PaperServer start() throws IOException, InterruptedException {
        Path paperJar = Path.of(property("bingo.e2e.paperJar"));
        Path pluginJar = Path.of(property("bingo.e2e.pluginJar"));
        Path e2eDirectory = Path.of(property("bingo.e2e.directory"));
        Path directory = e2eDirectory.resolve("server");
        deleteDirectory(directory);
        Files.createDirectories(directory.resolve("plugins"));
        Files.copy(pluginJar, directory.resolve("plugins/Bingo.jar"), StandardCopyOption.REPLACE_EXISTING);

        int port = freePort();
        int rconPort = freePort();
        String rconPassword = UUID.randomUUID().toString();
        writeServerProperties(directory, port, rconPort, rconPassword);

        Process process = new ProcessBuilder(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-Xmx2G",
                "-Dcom.mojang.eula.agree=true",
                // Paper downloads Minecraft and its libraries there once, instead of in each new server
                "-DbundlerRepoDir=" + e2eDirectory.resolve("cache"),
                "-Dterminal.jline=false",
                "-Dterminal.ansi=false",
                "-jar", paperJar.toString(), "--nogui")
                .directory(directory.toFile())
                .redirectErrorStream(true)
                .start();
        PaperServer server = new PaperServer(directory, process, e2eDirectory.resolve("server.log"), port);
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroyForcibly));
        try {
            server.waitUntilStarted();
            server.rcon = new Rcon(rconPort, rconPassword);
        } catch (IOException | InterruptedException | RuntimeException e) {
            server.close();
            throw e;
        }
        return server;
    }

    /**
     * Runs a console command and returns what it answered.
     */
    public String command(String command) {
        try {
            return LEGACY_COLOR_CODE.matcher(rcon.command(command)).replaceAll("");
        } catch (IOException e) {
            throw new UncheckedIOException("Could not run /" + command, e);
        }
    }

    public int getPort() {
        return port;
    }

    /**
     * The position of the next line of the log, to read what is logged from now on with {@link #logSince}.
     */
    public int logMark() {
        synchronized (log) {
            return log.size();
        }
    }

    /**
     * Waits until the log has every line written before this call: the server log is read a bit later than it is written.
     */
    public void waitForLog() {
        String marker = "e2e log marker " + nextLogMarker++;
        int mark = logMark();
        command("say " + marker);
        long deadline = System.nanoTime() + LOG_TIMEOUT.toNanos();
        while (logSince(mark).stream().noneMatch(line -> line.endsWith(marker))) {
            if (System.nanoTime() > deadline) {
                throw new IllegalStateException("The server log did not show \"" + marker + "\" in " + LOG_TIMEOUT.toSeconds() + " seconds");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
    }

    public List<String> logSince(int mark) {
        synchronized (log) {
            return List.copyOf(log.subList(mark, log.size()));
        }
    }

    private void waitUntilStarted() throws InterruptedException, IOException {
        long deadline = System.nanoTime() + START_TIMEOUT.toNanos();
        while (!started.await(1, TimeUnit.SECONDS)) {
            if (!process.isAlive()) {
                throw new IOException("The Paper server stopped while starting (exit code " + process.exitValue() + "), see build/e2e/server.log");
            }
            if (System.nanoTime() > deadline) {
                throw new IOException("The Paper server did not start in " + START_TIMEOUT.toMinutes() + " minutes, see build/e2e/server.log");
            }
        }
    }

    private void readLog(Path logFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Files.newBufferedWriter(logFile, StandardCharsets.UTF_8), true)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                line = ANSI_CODE.matcher(line).replaceAll("");
                writer.println(line);
                synchronized (log) {
                    log.add(line);
                }
                if (line.contains("]: Done (")) {
                    started.countDown();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws IOException, InterruptedException {
        if (rcon != null) {
            try {
                rcon.command("stop");
            } catch (IOException e) {
                // The server may close the connection before answering
            }
            rcon.close();
        }
        if (!process.waitFor(STOP_TIMEOUT.toSeconds(), TimeUnit.SECONDS)) {
            process.destroyForcibly().waitFor();
        }
        deleteDirectory(directory);
    }

    private static void writeServerProperties(Path directory, int port, int rconPort, String rconPassword) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("online-mode", "false");
        properties.setProperty("server-ip", "127.0.0.1");
        properties.setProperty("server-port", String.valueOf(port));
        properties.setProperty("enable-rcon", "true");
        properties.setProperty("rcon.port", String.valueOf(rconPort));
        properties.setProperty("rcon.password", rconPassword);
        properties.setProperty("broadcast-rcon-to-ops", "false");
        // The server settings recommended in the README
        properties.setProperty("spawn-protection", "0");
        properties.setProperty("gamemode", "survival");
        properties.setProperty("force-gamemode", "false");
        properties.setProperty("generate-structures", "true");
        try (var writer = Files.newBufferedWriter(directory.resolve("server.properties"), StandardCharsets.UTF_8)) {
            properties.store(writer, null);
        }
    }

    private static int freePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static String property(String name) {
        String value = System.getProperty(name);
        if (value == null) {
            throw new IllegalStateException(name + " is not set: run the end-to-end tests with ./gradlew e2eTest");
        }
        return value;
    }

    private static void deleteDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }
}
