package peter.parser;

import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.Event;
import peter.task.Task;
import peter.task.Todo;

public class Parser {

    public static Task parseTodo(String input) throws PeterException {
        String description = input.substring(4).trim();
        if (description.isEmpty()) {
            throw new PeterException("OOPS!!! The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    public static Task parseDeadline(String input) throws PeterException {
        String content = input.substring(8).trim();
        if (content.isEmpty()) {
            throw new PeterException("OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] parts = content.split(" /by ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new PeterException("OOPS!!! Please specify both description and /by time.");
        }
        return new Deadline(parts[0].trim(), parts[1].trim());
    }

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
}