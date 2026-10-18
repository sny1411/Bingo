package fr.sny1411.bingo.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PaperServerExtension.class)
class PluginEnabledTest {

    @Test
    void theConsoleCanRunTheBingoCommands(PaperServer server) {
        String answer = server.command("bingo grid");
        assertTrue(answer.startsWith("[BINGO]"), answer);
    }
}
