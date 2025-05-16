package enums;

public enum Command {
    HELP ("help"),
    CREATE ("create"),
    READ ("read"),
    UPDATE ("update"),
    LINKS ("links"),
    TAG ("tag"),
    UNTAG ("untag"),
    TAGS ("tags"),
    TAGGED ("tagged"),
    TRENDING ("trending"),
    NOTES ("notes"),
    DELETE ("delete"),
    EXIT ("exit");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static Command toCommand(String command) {
        for (Command c : Command.values()) {
            if (c.getCommand().equals(command)) {
                return c;
            }
        }
        return null;
    }

    public String getHelp() {
        return """
            create - creates a new note
            read - reads a note
            update - updates a note
            links - lists all links in a note
            tag - tags a note
            untag - untags a note
            tags - lists all tags in alphabetical order
            tagged - lists all notes with a specific tag
            trending - lists the most popular tags
            notes - lists all notes of a given type last edited within a given time interval
            delete - deletes a note
            help - shows the available commands
            exit - terminates the execution of the program""";
    }
}
