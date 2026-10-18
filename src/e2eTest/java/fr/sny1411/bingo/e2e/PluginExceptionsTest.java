package fr.sny1411.bingo.e2e;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginExceptionsTest {

    // Copied from a Paper 26.2 log
    @Test
    void findsAnExceptionThrownByThePlugin() {
        List<String> log = List.of(
                "[09:42:59 WARN]: *************************************************************************************",
                "[09:42:59 ERROR]: Command exception: /bingo pack",
                "java.lang.IllegalStateException: e2e check",
                "\tat Bingo.jar//fr.sny1411.bingo.commands.PackCommand.lambda$node$1(PackCommand.java:21) ~[?:?]",
                "\tat com.mojang.brigadier.context.ContextChain.runExecutable(ContextChain.java:73) ~[brigadier-1.3.10.jar:?]",
                "\tat net.minecraft.commands.execution.tasks.ExecuteCommand.execute(ExecuteCommand.java:30) ~[paper-26.2.jar:26.2-134-3e24be0]",
                "[09:43:00 INFO]: Alpha lost connection: Disconnected");

        List<String> exceptions = PluginExceptions.find(log);

        assertEquals(1, exceptions.size());
        assertTrue(exceptions.getFirst().startsWith("[09:42:59 ERROR]: Command exception: /bingo pack"), exceptions.getFirst());
        assertTrue(exceptions.getFirst().endsWith("ExecuteCommand.java:30) ~[paper-26.2.jar:26.2-134-3e24be0]"), exceptions.getFirst());
    }

    @Test
    void findsTheCauseInThePlugin() {
        List<String> log = List.of(
                "[10:00:01 WARN]: Unhandled exception",
                "java.lang.RuntimeException: wrapped",
                "\tat net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:1) ~[paper.jar:?]",
                "Caused by: java.lang.IllegalStateException: boom",
                "\tat Bingo.jar//fr.sny1411.bingo.Game.end(Game.java:93) ~[?:?]",
                "\t... 3 more");

        assertEquals(1, PluginExceptions.find(log).size());
    }

    @Test
    void ignoresTheExceptionsOfOtherPlugins() {
        List<String> log = List.of(
                "[10:00:01 ERROR]: Could not pass event PlayerJoinEvent to Other v1.0",
                "java.lang.NullPointerException",
                "\tat com.example.other.Listener.onJoin(Listener.java:10) ~[Other.jar:?]",
                "[10:00:02 INFO]: Bingo logs a line that mentions fr.sny1411.bingo.Game");

        assertTrue(PluginExceptions.find(log).isEmpty());
    }
}
