package fr.sny1411.bingo.e2e;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * A client for the RCON protocol of Minecraft servers, to run console commands.
 */
final class Rcon implements AutoCloseable {
    private static final int TYPE_COMMAND = 2;
    private static final int TYPE_LOGIN = 3;
    // Answered with an error, which tells that the whole answer of the previous command was read
    private static final int TYPE_END_MARKER = 0;
    // The server splits longer answers into packets of this size
    private static final int MAX_ANSWER_PACKET = 4096;

    private final Socket socket;
    private final DataInputStream in;
    private final OutputStream out;
    private int nextId = 1;

    Rcon(int port, String password) throws IOException {
        socket = new Socket("127.0.0.1", port);
        in = new DataInputStream(socket.getInputStream());
        out = socket.getOutputStream();
        int id = write(TYPE_LOGIN, password);
        if (read().id() != id) {
            close();
            throw new IOException("The RCON password was refused");
        }
    }

    // The server reads one packet at a time: sending two at once makes it close the connection
    synchronized String command(String command) throws IOException {
        int id = write(TYPE_COMMAND, command);
        Packet packet = read();
        StringBuilder answer = new StringBuilder(packet.body());
        if (packet.size() == MAX_ANSWER_PACKET) {
            int endId = write(TYPE_END_MARKER, "");
            for (packet = read(); packet.id() != endId; packet = read()) {
                if (packet.id() == id) {
                    answer.append(packet.body());
                }
            }
        }
        return answer.toString();
    }

    private int write(int type, String body) throws IOException {
        int id = nextId++;
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ByteBuffer packet = ByteBuffer.allocate(14 + bytes.length).order(ByteOrder.LITTLE_ENDIAN);
        packet.putInt(10 + bytes.length).putInt(id).putInt(type).put(bytes).put((byte) 0).put((byte) 0);
        out.write(packet.array());
        out.flush();
        return id;
    }

    private Packet read() throws IOException {
        byte[] lengthBytes = new byte[4];
        in.readFully(lengthBytes);
        int length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        ByteBuffer packet = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int id = packet.getInt();
        packet.getInt();
        return new Packet(id, new String(bytes, 8, length - 10, StandardCharsets.UTF_8), length - 10);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private record Packet(int id, String body, int size) {
    }
}
