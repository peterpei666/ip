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



public class Peter {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public Peter(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (PeterException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }

            ui.showLine();
            try {
                Command command = Command.fromString(fullCommand);
                switch (command) {
                    case BYE:
                        ui.showMessage("Bye. Hope to see you again soon!");
                        isExit = true;
                        break;
                    case LIST:
                        handleList();
                        break;
                    case MARK:
                        handleMark(fullCommand);
                        break;
                    case UNMARK:
                        handleUnmark(fullCommand);
                        break;
                    case TODO:
                        handleTodo(fullCommand);
                        break;
                    case DEADLINE:
                        handleDeadline(fullCommand);
                        break;
                    case EVENT:
                        handleEvent(fullCommand);
                        break;
                    case DELETE:
                        handleDelete(fullCommand);
                        break;
                    case VIEW: 
                        handleView(fullCommand);
                        break;
                    case FIND:
                        handleFind(fullCommand);
                        break;
                    default:
                        throw new PeterException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (PeterException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.close();
    }

    private void handleList() {
        if (tasks.isEmpty()) {
            ui.showMessage("Your task list is currently empty!");
        } else {
            ui.showMessage("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                ui.showMessage((i + 1) + "." + tasks.get(i));
            }
        }
    }

    private void handleMark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.mark(index);
        storage.save(tasks);
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("  " + task);
    }

    private void handleUnmark(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task task = tasks.unmark(index);
        storage.save(tasks);
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("  " + task);
    }

    private void handleTodo(String input) throws PeterException {
        Task task = Parser.parseTodo(input);
        tasks.add(task);
        storage.save(tasks);
        printTaskAdded(task);
    }

    private void handleDeadline(String input) throws PeterException {
        Task task = Parser.parseDeadline(input);
        tasks.add(task);
        storage.save(tasks);
        printTaskAdded(task);
    }

    private void handleEvent(String input) throws PeterException {
        Task task = Parser.parseEvent(input);
        tasks.add(task);
        storage.save(tasks);
        printTaskAdded(task);
    }

    private void handleDelete(String input) throws PeterException {
        int index = Parser.parseIndex(input);
        Task removedTask = tasks.delete(index);
        storage.save(tasks);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("  " + removedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Handles the find command by searching tasks matching the keyword.
     *
     * @param fullCommand Raw command input string.
     * @throws PeterException If keyword is missing.
     */
    private void handleFind(String fullCommand) throws PeterException {
        String keyword = Parser.parseFindKeyword(fullCommand);
        List<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(matchingTasks);
    }

    private void printTaskAdded(Task task) {
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Handles the view command to list all tasks occurring on a specific date.
     *
     * @param fullCommand Raw command string entered by the user (e.g., "view 2019-12-02").
     * @throws PeterException If the date format is invalid or parameter is missing.
     */
    private void handleView(String fullCommand) throws PeterException {
        LocalDate date = Parser.parseViewDate(fullCommand);
        List<Task> matchingTasks = tasks.getTasksOnDate(date);
        ui.showTasksOnDate(matchingTasks);
    }

    public static void main(String[] args) {
        new Peter("data/peter.txt").run();
    }
}