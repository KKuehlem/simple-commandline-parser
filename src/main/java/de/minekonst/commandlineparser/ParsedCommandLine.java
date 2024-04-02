package de.minekonst.commandlineparser;

import java.util.Map;

public class ParsedCommandLine {

    private final Map<String, Object> map;

    ParsedCommandLine(Map<String, Object> map) {
        this.map = map;
    }
}
