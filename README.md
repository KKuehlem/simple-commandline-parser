![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
[![Build and Release](https://github.com/KKuehlem/simple-commandline-parser/actions/workflows/Build.yml/badge.svg)](https://github.com/KKuehlem/simple-commandline-parser/actions/workflows/Build.yml)

# A simple command-line parser
This is a very simple command-line parser for Java I made for my own projects. It is designed to be easy to use and to be able to parse command-line arguments into Java objects.

Here is a quick example of how to use the command-line parser:
```java
import de.kkuehlem.commandlineparser.CommandlineParser;
import de.kkuehlem.commandlineparser.Option;
import de.kkuehlem.commandlineparser.ParsedCommandLine;
import de.kkuehlem.commandlineparser.OptionParsers;

public class Main {

    public static void main(String[] args) {
        CommandlineParser parser = new CommandlineParser();
        
        Option<Integer> a = new Option<>("integer", "i", Integer.class, "A simple  integer option", true);
        Option<Double> b = new Option<>("double", "d", Double.class, "A simple double option", true);
        Option<String> c = new Option<>("string", "s", String.class, "A simple String option", false);
        parser.addOptions(a, b, c);

        // From a string array
        ParsedCommandLine parsed = parser.parse("-i", "42", "--double", "2.2");
        int aVal = parsed.get(a); // 42
        double bVal = parsed.get(b); // 2.2
        String cVal = parsed.get(c, "ABC"); // "ABC" (default value)

        // From the command line arguments
        ParsedCommandLine parsedFromArgs = parser.parse(args);
        if (!parsedFromArgs.isSuccess()) {
            parsedFromArgs.printErrors(System.out); // You can also handle the errors yourself with parsedFromArgs.getProblems()
            return;
        }

        // Do something with the parsed arguments...

        // You can also add your own types, lets suppose you have a Vector2 class
        OptionParsers.register(Vector2.class, Vector2::parse);
        Option<Vector2> vec = new Option<>("vector", "v", Vector2.class, "A simple Vector2 option", true);

        ParsedCommandLine parsedVec = parser.parse("-v", "1.0,2.0");
        Vector2 vecVal = parsedVec.get(vec); // Vector2(1.0, 2.0)
    }
}
```

# To-Do
- [ ] Help message generation
- [ ] Add support for arrays / lists
- [ ] Add support for enums
- [ ] Add support for flags
- [ ] Publish as a maven package
