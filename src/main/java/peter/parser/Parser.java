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
            throw new PeterException("OOPS!!! The description of a todo cannot be empty.");
        }
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
            throw new PeterException("OOPS!!! The description and date of a deadline cannot be empty.");
        }
        
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
            throw new PeterException("OOPS!!! The description of an event cannot be empty.");
        }
        String[] parts = content.split(" /from ");
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new PeterException("OOPS!!! Please use format: event description /from start /to end");
        }
        String[] timeParts = parts[1].split(" /to ");
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            throw new PeterException("OOPS!!! Please specify both /from and /to time ranges.");
        }
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
            throw new PeterException("OOPS!!! Please specify the task number.");
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new PeterException("OOPS!!! Task index must be a valid integer.");
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
            throw new PeterException("OOPS!!! Please specify a date in yyyy-MM-dd format (e.g., view 2019-12-02).");
        }
        try {
            return LocalDate.parse(parts[1].trim());
        } catch (DateTimeParseException e) {
            throw new PeterException("OOPS!!! Invalid date format. Please use yyyy-MM-dd (e.g., 2019-12-02).");
        }
    }
}