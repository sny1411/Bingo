package fr.sny1411.bingo.e2e;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Finds the exceptions thrown by the plugin in lines of the server log.
 */
final class PluginExceptions {
    // Paper puts the name of the plugin jar before the class: "at Bingo.jar//fr.sny1411.bingo.Game.end(Game.java:93)"
    private static final Pattern PLUGIN_FRAME = Pattern.compile("^at (?:\\S+//)?fr\\.sny1411\\.bingo\\.");

    private PluginExceptions() {
    }

    // Each exception is returned with the log line that announces it, which says what failed
    static List<String> find(List<String> lines) {
        List<String> exceptions = new ArrayList<>();
        int i = 0;
        while (i < lines.size()) {
            if (!isStackTrace(lines.get(i))) {
                i++;
                continue;
            }
            int start = Math.max(i - 1, 0);
            if (start > 0 && !isLogLine(lines.get(start)) && isLogLine(lines.get(start - 1))) {
                start--;
            }
            while (i < lines.size() && (isStackTrace(lines.get(i)) || isCause(lines.get(i)))) {
                i++;
            }
            List<String> exception = lines.subList(start, i);
            if (exception.stream().anyMatch(line -> PLUGIN_FRAME.matcher(line.trim()).find())) {
                exceptions.add(String.join("\n", exception));
            }
        }
        return exceptions;
    }

    private static boolean isStackTrace(String line) {
        String trimmed = line.trim();
        return (line.startsWith("\t") || line.startsWith(" ")) && (trimmed.startsWith("at ") || trimmed.startsWith("..."));
    }

    private static boolean isLogLine(String line) {
        return line.startsWith("[");
    }

    private static boolean isCause(String line) {
        return line.startsWith("Caused by: ") || line.trim().startsWith("Suppressed: ");
    }
}
