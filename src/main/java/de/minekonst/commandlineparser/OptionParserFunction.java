package de.minekonst.commandlineparser;

@FunctionalInterface
public interface OptionParserFunction<T> {
    
    public T parse(String value) throws Exception;
}
