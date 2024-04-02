package de.minekonst.commandlineparser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommandlineParserTest {

    @Test
    public void test() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c = new Option<>("c", "c", String.class, "A simple option", true);
        parser.addOptions(a, b, c);

        ParsedCommandLine parsed = parser.parse("--a", "42", "--b", "2.2", "-c", "ABC");
        assertEquals(42, (int) parsed.get(a)); // Ambigious without cast
        assertEquals(2.2, parsed.get(b), 0);
        assertEquals("ABC", parsed.get(c));
        assertEquals("ABC", parsed.get("c", String.class));
        assertNull(parsed.get("x", String.class));
        assertEquals("X", parsed.get("x", String.class, "X"));
    }
}
