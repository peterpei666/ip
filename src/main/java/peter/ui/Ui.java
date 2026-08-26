package peter.ui;

import java.util.List;
import java.util.Scanner;
import peter.task.Task;

public class Ui {
    private static final String LINE = "    ____________________________________________________________";
    private static final String LOGO = "____________________________________________________________\n"
            + " ____  _____ _____ _____ ____  \n"
            + "|  _ \\| ____|_   _| ____|  _ \\ \n"
            + "| |_) |  _|   | | |  _| | |_) |\n"
            + "|  __/| |___  | | | |___|  _ < \n"
            + "|_|   |_____| |_| |_____|_| \\_\\\n";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(LOGO);
        System.out.println(" Hello! I'm Peter.");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println("     " + message);
    }

    public void showLoadingError() {
        System.out.println("     [Warning] Failed to load data from storage. Starting with empty list.");
    }

    /**
     * Prints tasks found on a specific date.
     *
     * @param tasks List of tasks matching the date.
     */
    public void showTasksOnDate(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("     No tasks found on this date!");
            return;
        }
        System.out.println("     Here are the tasks on this date:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints the list of tasks matching a search keyword.
     *
     * @param matchingTasks List of tasks that matched the search query.
     */
    public void showFoundTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("     No matching tasks found in your list!");
            return;
        }
        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    public void showMessage(String message) {
        System.out.println("     " + message);
    }

    public void close() {
        scanner.close();
    }
}