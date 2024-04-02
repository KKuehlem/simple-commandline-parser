package de.minekonst.commandlineparser;

import java.util.Map;

public class ParsedCommandLine {

    private final Map<String, Object> map;

    ParsedCommandLine(Map<String, Object> map) {
        this.map = map;
    }

    public <T> T get(String key, Class<T> type) {
        return get(key, type, null);
    }

    public <T> T get(Option<T> option, T defaultValue) {
        return get(option.getLongName(), option.getType(), defaultValue);
    }

    public <T> T get(Option<T> option) {
        return get(option.getLongName(), option.getType(), null);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type, T defaultValue) {
        T t = (T) map.get(key);
        return t != null ? t : defaultValue;
    }

}
