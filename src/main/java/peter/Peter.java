package peter;

import java.time.LocalDate;
import java.util.List;

import peter.command.Command;
import peter.exception.PeterException;
import peter.parser.Parser;
import peter.storage.Storage;
import peter.task.Task;
import peter.task.TaskList;
import peter.ui.Ui;

/**
 * The main class of the chatbot.
 */
public class Peter {
    private static final String WELCOME_MESSAGE = "Hello! I'm Peter.\nWhat can I do for you?";

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;
    private boolean hasLoadingError;

    /**
     * Constructs a chatbot.
     *
     * @param filePath The path to the storage file.
     */
    public Peter(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (PeterException e) {
            hasLoadingError = true;
            tasks = new TaskList();
        }
    }

    /**
     * Runs the chatbot's command loop.
     */
    public void run() {
        if (hasLoadingError) {
            ui.showLoadingError();
        }
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }

            ui.showLine();
            ui.showMessage(getResponse(fullCommand));
            isExit = Command.fromString(fullCommand) == Command.BYE;
            ui.showLine();
        }
        ui.close();
    }

    /**
     * Generates Peter's response to one command for use by both the GUI and text UI.
     *
     * @param fullCommand Raw command entered by the user.
     * @return Peter's response text.
     */
    public String getResponse(String fullCommand) {
        try {
            Command command = Command.fromString(fullCommand);
            return switch (command) {
                case BYE -> "Bye. Hope to see you again soon!";
                case LIST -> handleList();
                case MARK -> handleMark(fullCommand);
                case UNMARK -> handleUnmark(fullCommand);
                case TODO -> handleTodo(fullCommand);
                case DEADLINE -> handleDeadline(fullCommand);
                case EVENT -> handleEvent(fullCommand);
                case DELETE -> handleDelete(fullCommand);
                case VIEW -> handleView(fullCommand);
                case FIND -> handleFind(fullCommand);
                default -> throw new PeterException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            };
        } catch (PeterException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the greeting shown when the GUI starts.
     *
     * @return Peter's welcome message, including a loading warning when needed.
     */
    public String getWelcomeMessage() {
        if (hasLoadingError) {
            return "[Warning] Failed to load data from storage. Starting with empty list.\n" + WELCOME_MESSAGE;
        }
        return WELCOME_MESSAGE;
    }

    private String handleList() {
        if (tasks.isEmpty()) {
            return "Your task list is currently empty!";
        }
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(System.lineSeparator()).append(i + 1).append('.').append(tasks.get(i));
        }
        return response.toString();
    }

    private String handleMark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.mark(index);
        storage.save(tasks);
        return "Nice! I've marked this task as done:\n  " + task;
    }

    private String handleUnmark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.unmark(index);
        storage.save(tasks);
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    private String handleTodo(String input) throws PeterException {
        Task task = Parser.parseTodo(input);
        tasks.add(task);
        storage.save(tasks);
        return getTaskAddedMessage(task);
    }

    private String handleDeadline(String input) throws PeterException {
        Task task = Parser.parseDeadline(input);
        tasks.add(task);
        storage.save(tasks);
        return getTaskAddedMessage(task);
    }

    private String handleEvent(String input) throws PeterException {
        Task task = Parser.parseEvent(input);
        tasks.add(task);
        storage.save(tasks);
        return getTaskAddedMessage(task);
    }

    private String handleDelete(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task removedTask = tasks.delete(index);
        storage.save(tasks);
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the find command by searching tasks matching the keyword.
     *
     * @param fullCommand Raw command input string.
     * @throws PeterException If keyword is missing.
     */
    private String handleFind(String fullCommand) throws PeterException {
        String keyword = Parser.parseFindKeyword(fullCommand);
        List<Task> matchingTasks = tasks.findTasks(keyword);
        return formatTasks(matchingTasks, "No matching tasks found in your list!",
                "Here are the matching tasks in your list:");
    }

    private String getTaskAddedMessage(Task task) {
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the view command to list all tasks occurring on a specific date.
     *
     * @param fullCommand Raw command string entered by the user (e.g., "view 2019-12-02").
     * @throws PeterException If the date format is invalid or parameter is missing.
     */
    private String handleView(String fullCommand) throws PeterException {
        LocalDate date = Parser.parseViewDate(fullCommand);
        List<Task> matchingTasks = tasks.getTasksOnDate(date);
        return formatTasks(matchingTasks, "No tasks found on this date!",
                "Here are the tasks on this date:");
    }

    private String formatTasks(List<Task> matchingTasks, String emptyMessage, String heading) {
        if (matchingTasks.isEmpty()) {
            return emptyMessage;
        }
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append(System.lineSeparator()).append(i + 1).append('.').append(matchingTasks.get(i));
        }
        return response.toString();
    }

    public static void main(String[] args) {
        new Peter("data/peter.txt").run();
    }
}
