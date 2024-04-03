package de.minekonst.commandlineparser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandlineProblem {
    
    public enum CommandlineProblemType {
        INVALID_VALUE, UNKNOWN_OPTION, DUPLICATED_OPTION, MISSING_REQUIRED_OPTION;
    }

    private final CommandlineProblemType type;
    private final String option;
    private final String optionType;
    private final String value;
    private final Option<?> optionObject;

    public CommandlineProblem(CommandlineProblemType type, String option, String optionType, String value) {
        this.type = type;
        this.option = option;
        this.optionType = optionType;
        this.value = value;
        this.optionObject = null;
    }

    @Override
    public String toString() {
        if (type == CommandlineProblemType.INVALID_VALUE) {
            return "Invalid value for option \"" + option + "\": " + value + " (expected: " + optionType + ")";
        }
        else if (type == CommandlineProblemType.UNKNOWN_OPTION) {
            return "Unknown option: " + option;
        }
        else if (type == CommandlineProblemType.DUPLICATED_OPTION) {
            return "Duplicated option: " + option;
        }
        else {
            throw new IllegalStateException("Unknown problem type: " + type);
        }
    }
}
