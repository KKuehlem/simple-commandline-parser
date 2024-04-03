package de.minekonst.commandlineparser;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandlineParserTest {

    @Test
    public void test() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c = new Option<>("c", "c", String.class, "A simple option", true);
        parser.addOptions(a, b, c);

        ParsedCommandLine parsed = parser.parse("--a", "42", "--b", "2.2", "-c", "ABC");
        assertTrue(parsed.isSuccess());
        assertEquals(42, (int) parsed.get(a)); // Ambigious without cast
        assertEquals(2.2, parsed.get(b), 0);
        assertEquals("ABC", parsed.get(c));
        assertEquals("ABC", parsed.get("c", String.class));
        assertNull(parsed.get("x", String.class));
        assertEquals("X", parsed.get("x", String.class, "X"));
    }

    @Test
    public void testUnknownOption() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c = new Option<>("c", "c", String.class, "A simple option", true);
        parser.addOptions(a, b, c);

        ParsedCommandLine parsed = parser.parse("--a", "42", "--b", "2.2", "-x", "ABC");
        assertFalse(parsed.isSuccess());
        assertEquals(1, parsed.getProblems().size());
        assertEquals("-x", parsed.getProblems().get(0).getOption());
        assertEquals(CommandlineProblem.CommandlineProblemType.UNKNOWN_OPTION, parsed.getProblems().get(0).getType());
    }

    @Test
    public void testInvalidValue() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c = new Option<>("c", "c", String.class, "A simple option", true);
        parser.addOptions(a, b, c);

        ParsedCommandLine parsed = parser.parse("--b", "2.2", "-c", "ABC", "--a", "X");
        assertFalse(parsed.isSuccess());
        assertEquals(1, parsed.getProblems().size());
        assertEquals("--a", parsed.getProblems().get(0).getOption());
        assertEquals("Integer", parsed.getProblems().get(0).getOptionType());
        assertEquals(CommandlineProblem.CommandlineProblemType.INVALID_VALUE, parsed.getProblems().get(0).getType());
    }

    @Test
    public void testDuplicatedOptions() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c = new Option<>("c", "c", String.class, "A simple option", true);
        parser.addOptions(a, b, c);

        ParsedCommandLine parsed = parser.parse("--a", "42", "-a", "43", "--b", "2.2", "-c", "ABC");
        assertFalse(parsed.isSuccess());
        assertEquals(1, parsed.getProblems().size());
        assertEquals("-a", parsed.getProblems().get(0).getOption());
        assertEquals(CommandlineProblem.CommandlineProblemType.DUPLICATED_OPTION, parsed.getProblems().get(0).getType());
    }

    @Test
    public void testBadUsage() {
        CommandlineParser parser = new CommandlineParser();

        Option<Integer> a = new Option<>("a", "a", Integer.class, "A simple option", true);
        Option<Double> b = new Option<>("b", "b", Double.class, "A simple option", true);
        Option<String> c1 = new Option<>("a", "c", String.class, "A simple option", true);
        Option<String> c2 = new Option<>("c", "a", String.class, "A simple option", true);
        assertThrows(IllegalArgumentException.class, () -> parser.addOptions(a, b, c1));
        assertThrows(IllegalArgumentException.class, () -> parser.addOptions(a, b, c2));

        CommandlineParser parser2 = new CommandlineParser();
        parser2.addOptions(a, b);

        assertThrows(NullPointerException.class, () -> parser2.parse((String[]) null));
        assertThrows(NullPointerException.class, () -> parser2.parse((String) null));

        ParsedCommandLine parsed = parser2.parse("--a", "1", "--b", "2");
        assertThrows(IllegalStateException.class, () -> parsed.printErrors(System.out));
    }
}
