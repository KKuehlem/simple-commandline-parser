package de.kkuehlem.commandlineparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OptionParsers {

    private static final Map<Class<?>, OptionParserFunction<?>> functions = new HashMap<>();

    static {
        register(Integer.class, Integer::valueOf);
        register(Double.class, Double::valueOf);
        register(String.class, s -> s);
        register(Boolean.class, s -> {
            if ("true".equalsIgnoreCase(s) || "1".equalsIgnoreCase(s)) {
                return true;
            }
            else if ("false".equalsIgnoreCase(s) || "0".equalsIgnoreCase(s)) {
                return false;
            }
            
            throw new IllegalArgumentException(s);
        });
    }

    public static <T> void register(Class<T> type, OptionParserFunction<T> function) {
        functions.put(type, Objects.requireNonNull(function));
    }

    public static boolean isRegistered(Class<?> type) {
        return functions.get(type) != null;
    }

    public static <T> T parse(Class<T> type, String value) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(value);
        
        @SuppressWarnings("unchecked")
        OptionParserFunction<T> f = (OptionParserFunction<T>) functions.get(type);
        if (f == null) throw new IllegalStateException("No parser registered for class " + type.getCanonicalName());

        try {
            return (T) f.parse(value);
        }
        catch (Exception ex) {
            return null;
        }
    }

}
