package fr.sny1411.bingo.commands.argument;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DurationArgumentTest {

    @Test
    void parsesMinutesAndSeconds() {
        assertEquals(10 * 60, DurationArgument.parse("10:00"));
        assertEquals(59 * 60 + 59, DurationArgument.parse("59:59"));
        assertEquals(65, DurationArgument.parse("1:05"));
    }

    @Test
    void parsesHours() {
        assertEquals(3600 + 59 * 60 + 50, DurationArgument.parse("1:59:50"));
    }

    @Test
    void rejectsWrongFormats() {
        for (String input : new String[]{"", "10", "abc", "10:60", "1:00:60", "100:00", "1:2:3:4", "10:", ":10", "-1:00", "10:00 5"}) {
            assertEquals(-1, DurationArgument.parse(input), input);
        }
    }
}
