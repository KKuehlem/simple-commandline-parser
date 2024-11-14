package de.kkuehlem.commandlineparser;

import java.util.Map;
import java.util.List;
import java.io.PrintStream;

import lombok.Getter;

public class ParsedCommandLine {

    private final Map<String, Object> map;
    @Getter private final boolean success;
    @Getter private final List<CommandlineProblem> problems;

    ParsedCommandLine(Map<String, Object> map) {
        this.map = map;
        this.success = true;
        this.problems = null;
    }

    ParsedCommandLine(List<CommandlineProblem> problems) {
        this.map = null;
        this.success = false;
        this.problems = problems;
    }

    public <T> T get(String key, Class<T> type) {
        ensureSuccess();
        return get(key, type, null);
    }

    public <T> T get(Option<T> option, T defaultValue) {
        ensureSuccess();
        return get(option.getLongName(), option.getType(), defaultValue);
    }

    public <T> T get(Option<T> option) {
        ensureSuccess();
        return get(option.getLongName(), option.getType(), null);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type, T defaultValue) {
        T t = (T) map.get(key);
        return t != null ? t : defaultValue;
    }

    public void printErrors(PrintStream out) {
        if (success) {
            throw new IllegalStateException("No errors to print, check success first using isSuccess()");
        }
        for (CommandlineProblem problem : problems) {
            out.println(problem.toString());
        }
    }

    private void ensureSuccess() {
        if (!success) {
            throw new IllegalStateException("Commandline parsing was not successful, check success first using isSuccess()");
        }
    }

}
