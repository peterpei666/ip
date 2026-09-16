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
    private static final String WELCOME_MESSAGE = "Welcome aboard—I'm Peter, your task navigator."
            + "\nWhat shall we chart today?";

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
        return getResponseResult(fullCommand).message();
    }

    /**
     * Generates a response together with presentation metadata for the GUI.
     *
     * @param fullCommand Raw command entered by the user.
     * @return Response text and whether it represents an input error.
     */
    public Response getResponseResult(String fullCommand) {
        try {
            Command command = Command.fromString(fullCommand);
            String message = switch (command) {
                case BYE -> "Journey paused. Your course is saved—see you on the next leg!";
                case LIST -> handleList();
                case MARK -> handleMark(fullCommand);
                case UNMARK -> handleUnmark(fullCommand);
                case TODO -> handleTodo(fullCommand);
                case DEADLINE -> handleDeadline(fullCommand);
                case EVENT -> handleEvent(fullCommand);
                case DELETE -> handleDelete(fullCommand);
                case VIEW -> handleView(fullCommand);
                case FIND -> handleFind(fullCommand);
                case SORT -> handleSort();
                default -> throw new PeterException(
                        "That route isn't on my map yet. Try a command such as list, todo, or deadline.");
            };
            return new Response(message, false);
        } catch (PeterException e) {
            return new Response(e.getMessage(), true);
        }
    }

    /**
     * Returns the greeting shown when the GUI starts.
     *
     * @return Peter's welcome message, including a loading warning when needed.
     */
    public String getWelcomeMessage() {
        if (hasLoadingError) {
            return "[Log warning] I couldn't read the saved route, so we're starting with a clear map.\n"
                    + WELCOME_MESSAGE;
        }
        return WELCOME_MESSAGE;
    }

    private String handleList() {
        if (tasks.isEmpty()) {
            return "The route is clear—there are no tasks on the map.";
        }
        StringBuilder response = new StringBuilder("Here's the course we've charted:");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(System.lineSeparator()).append(i + 1).append('.').append(tasks.get(i));
        }
        return response.toString();
    }

    private String handleMark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.mark(index);
        storage.save(tasks);
        return "Milestone reached! I've marked this task complete:\n  " + task;
    }

    private String handleUnmark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.unmark(index);
        storage.save(tasks);
        return "Course adjusted. This task is back on the route:\n  " + task;
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
        return "Route updated. I've removed this task:\n  " + removedTask
                + "\nThere are now " + tasks.size() + " tasks on the map.";
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
        return formatTasks(matchingTasks, "I searched the map, but found no tasks matching that keyword.",
                "These tasks match your search:");
    }

    private String getTaskAddedMessage(Task task) {
        return "Waypoint charted! I've added this task:\n  " + task
                + "\nThere are now " + tasks.size() + " tasks on the map.";
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
        return formatTasks(matchingTasks, "No tasks are charted for that date.",
                "Here's the route for that date:");
    }

    /**
     * Sorts deadlines chronologically and persists the reordered task list.
     *
     * @return A message containing the sorted task list.
     */
    private String handleSort() {
        tasks.sortByDeadline();
        storage.save(tasks);
        return formatTasks(tasks.getTasks(), "The route is clear—there are no deadlines to arrange.",
                "Course arranged! Deadlines now run chronologically:");
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

    /**
     * A command response and the information needed to present it appropriately.
     *
     * @param message Text to display.
     * @param isError Whether the response describes invalid user input.
     */
    public record Response(String message, boolean isError) {
    }

    public static void main(String[] args) {
        new Peter("data/peter.txt").run();
    }
}
