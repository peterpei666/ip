package peter.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.Event;
import peter.task.Task;
import peter.task.Todo;

/**
 * Parses user input strings into corresponding command tasks or parameters.
 */
public class Parser {

    /**
     * Parses the user input for creating a Todo task.
     *
     * @param input Raw user input string.
     * @return A new Todo object.
     * @throws PeterException If the description is empty.
     */
    public static Task parseTodo(String input) throws PeterException {
        String description = input.substring(4).trim();
        if (description.isEmpty()) {
            throw new PeterException("That waypoint needs a description. Try: todo read book");
        }
        assert !description.isBlank() : "Todo description should have been validated";
        return new Todo(description);
    }

    /**
     * Parses the command for creating a Deadline task.
     *
     * @param input Raw command string e.g., "deadline return book /by 2019-12-02 1800"
     * @return A new Deadline object.
     * @throws PeterException If description or /by date is missing.
     */
    public static Task parseDeadline(String input) throws PeterException {
        String body = input.substring(8).trim();
        String[] parts = body.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new PeterException("A deadline needs a task and arrival time. "
                    + "Try: deadline return book /by 2026-09-10 1800");
        }

        assert !parts[0].isBlank() : "Deadline description should have been validated";
        assert !parts[1].isBlank() : "Deadline date should have been validated";
        return new Deadline(parts[0].trim(), parts[1].trim());
    }

    /**
     * Parses the user input for creating an Event task.
     *
     * @param input Raw user input string.
     * @return A new Event object.
     * @throws PeterException If the description is empty.
     */
    public static Task parseEvent(String input) throws PeterException {
        String content = input.substring(5).trim();
        if (content.isEmpty()) {
            throw new PeterException("That event needs a description before I can chart it.");
        }
        String[] parts = content.split(" /from ");
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new PeterException("I need the full route. Try: event meeting /from 1400 /to 1600");
        }
        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            throw new PeterException("Please chart both the /from and /to times for that event.");
        }
        assert !parts[0].isBlank() : "Event description should have been validated";
        assert !timeParts[0].isBlank() : "Event start time should have been validated";
        assert !timeParts[1].isBlank() : "Event end time should have been validated";
        return new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
    }

    /**
     * Parses the zero-based index from the user input.
     *
     * @param input Raw user input string containing a task index.
     * @return Zero-based task index.
     * @throws PeterException If the index format is invalid or missing.
     */
    public static int parseIndex(String input) throws PeterException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new PeterException("Which waypoint? Please include a task number.");
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new PeterException("That waypoint number isn't valid. Please use a whole number.");
        }
    }

    /**
     * Parses the date string from the user input for the view command.
     *
     * @param input Full user command string e.g., "view 2019-12-02"
     * @return Parsed LocalDate object.
     * @throws PeterException If input format is invalid or empty.
     */
    public static LocalDate parseViewDate(String input) throws PeterException {
        String[] parts = input.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new PeterException("Which date should I scout? Try: view 2026-09-10");
        }
        try {
            return LocalDate.parse(parts[1].trim());
        } catch (DateTimeParseException e) {
            throw new PeterException("I couldn't read that date. Please use yyyy-MM-dd, such as 2026-09-10.");
        }
    }

    /**
     * Parses the search keyword from the find command.
     *
     * @param input Full command input string (e.g., "find book").
     * @return The keyword string to search for.
     * @throws PeterException If the keyword is missing or empty.
     */
    public static String parseFindKeyword(String input) throws PeterException {
        String[] parts = input.trim().split("\\s+", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new PeterException("What should I scout for? Try: find book");
        }
        return parts[1].trim();
    }
}
