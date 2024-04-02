package de.minekonst.commandlineparser;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Option<T> {
    
    private final String longName;
    private final String shortName;
    private final Class<T> type;

    public Option(String longName, String shortName, Class<T> type, String description) {
        this.longName = Objects.requireNonNull(longName);
        this.shortName = shortName;
        this.type = type;
    }

}
