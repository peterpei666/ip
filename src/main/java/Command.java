public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    DELETE,
    UNKNOWN;

    /**
     * Parses a raw user input string and returns the corresponding Command enum.
     *
     * @param input Raw command string from the user.
     * @return Matching Command enum value.
     */
    public static Command fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        String firstWord = input.trim().split("\\s+")[0].toUpperCase();
        try {
            return Command.valueOf(firstWord);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}