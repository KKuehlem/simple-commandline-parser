package de.minekonst.commandlineparser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Vector2 {

    private final double x;
    private final double y;

    public static Vector2 parse(String value) {
        String[] parts = value.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid vector format: " + value);
        }

        return new Vector2(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }
}
