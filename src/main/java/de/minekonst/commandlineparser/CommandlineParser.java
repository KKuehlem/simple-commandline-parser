package de.minekonst.commandlineparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandlineParser {

    public static final String SHORT_PREFIX = "-";
    public static final String LONG_PREFIX = "--";

    private final Map<String, Option<?>> shortNameMap = new HashMap<>();
    private final Map<String, Option<?>> longNameMap = new HashMap<>();

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
        List<CommandlineProblem> problems = new ArrayList<>();
        Option<?> current = null;
        String lastArg = null;

        for (String arg : args) {
            Objects.requireNonNull(arg);

            if (current == null) {
                if (arg.startsWith(LONG_PREFIX)) {
                    current = longNameMap.get(arg.substring(LONG_PREFIX.length()));
                    lastArg = arg;
                    if (current == null) {
                        problems.add(new CommandlineProblem(CommandlineProblem.CommandlineProblemType.UNKNOWN_OPTION, arg, null, null));
                    }
                }
                else if (arg.startsWith(SHORT_PREFIX)) {
                    current = shortNameMap.get(arg.substring(SHORT_PREFIX.length()));
                    lastArg = arg;
                    if (current == null) {
                        problems.add(new CommandlineProblem(CommandlineProblem.CommandlineProblemType.UNKNOWN_OPTION, arg, null, null));
                    }
                }
            }
            else {
                Object value = OptionParsers.parse(current.getType(), arg);
                
                if (value == null) {
                    problems.add(new CommandlineProblem(CommandlineProblem.CommandlineProblemType.INVALID_VALUE, lastArg, current.getType().getSimpleName(), arg, current));
                }

                if (map.put(current.getLongName(), value) != null) {
                    problems.add(new CommandlineProblem(CommandlineProblem.CommandlineProblemType.DUPLICATED_OPTION, lastArg, null, null, current));
                }
                current = null;
            }
        }

        for (Option<?> o : longNameMap.values()) {
            if (o.isRequired() && map.get(o.getLongName()) == null) {
                if (problems.stream().noneMatch(p -> p.getOptionObject() == o)) {
                    problems.add(new CommandlineProblem(CommandlineProblem.CommandlineProblemType.MISSING_REQUIRED_OPTION, o.getLongName(), null, null, o));
                }
            }
        }

        return problems.isEmpty() ? new ParsedCommandLine(map) : new ParsedCommandLine(problems);
    }

}
