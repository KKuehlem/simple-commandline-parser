package de.minekonst.commandlineparser;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandlineParser {

    public static final String SHORT_PREFIX = "-";
    public static final String LONG_PREFIX = "--";

    private final Map<String, Option<?>> shortNameMap = new HashMap<>();
    private final Map<String, Option<?>> longNameMap = new HashMap<>();
    private final PrintStream out;

    public CommandlineParser() {
        this(System.out);
    }

    public CommandlineParser(PrintStream out) {
        this.out = out;
    }

    public void addOptions(Option<?>... options) {
        for (Option<?> o : options) {
            if (longNameMap.put(o.getLongName(), o) != null) {
                throw new IllegalArgumentException("Duplicated option for long name " + o.getLongName());
            }
            if (o.getShortName() != null && shortNameMap.put(o.getShortName(), o) != null) {
                throw new IllegalArgumentException("Duplicated option for short name " + o.getShortName());
            }
        }
    }

    public ParsedCommandLine parse(String... args) {
        Map<String, Object> map = new HashMap<>();
        Option<?> current = null;

        for (String arg : args) {
            Objects.requireNonNull(arg);

            if (arg.startsWith(SHORT_PREFIX)) {
                current = shortNameMap.get(arg.substring(SHORT_PREFIX.length()));
                if (current == null) {
                    printUnknownOption(arg);
                    return null;
                }
            }
            else if (arg.startsWith(LONG_PREFIX)) {
                current = longNameMap.get(arg.substring(LONG_PREFIX.length()));
                if (current == null) {
                    printUnknownOption(arg);
                    return null;
                }
            }
        }

        return new ParsedCommandLine(map);
    }

    private void printUnknownOption(String arg) {
        out.printf("Error: Unknown option \"%s\", use --help / -h for a list of options\n", arg);
    }
}