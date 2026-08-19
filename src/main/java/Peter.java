import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Peter {
    public static void main(String[] args) {
        String line = "    ____________________________________________________________";
        String logo = "____________________________________________________________\n"
                    + " ____  _____ _____ _____ ____  \n"
                    + "|  _ \\| ____|_   _| ____|  _ \\ \n"
                    + "| |_) |  _|   | | |  _| | |_) |\n"
                    + "|  __/| |___  | | | |___|  _ < \n"
                    + "|_|   |_____| |_| |_____|_| \\_\\\n";

        List<Task> tasks = new ArrayList<>();

        System.out.println(logo);
        System.out.println(" Hello! I'm Peter.");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                System.out.println(line);

                try {
                    if (input.equalsIgnoreCase("bye")) {
                        System.out.println("     Bye. Hope to see you again soon!");
                        System.out.println(line);
                        break;
                    } else if (input.equalsIgnoreCase("list")) {
                        if (tasks.isEmpty()) {
                            System.out.println("     Your task list is currently empty!");
                        } else {
                            System.out.println("     Here are the tasks in your list:");
                            for (int i = 0; i < tasks.size(); i++) {
                                System.out.println("     " + (i + 1) + "." + tasks.get(i));
                            }
                        }
                    } else if (input.startsWith("mark")) {
                        handleMark(input, tasks);
                    } else if (input.startsWith("unmark")) {
                        handleUnmark(input, tasks);
                    } else if (input.startsWith("todo")) {
                        handleTodo(input, tasks);
                    } else if (input.startsWith("deadline")) {
                        handleDeadline(input, tasks);
                    } else if (input.startsWith("event")) {
                        handleEvent(input, tasks);
                    } else {
                        throw new PeterException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                    }
                } catch (PeterException e) {
                    System.out.println("     " + e.getMessage());
                }

                System.out.println(line);
            }
        }
    }

    private static void handleMark(String input, List<Task> tasks) throws PeterException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new PeterException("OOPS!!! Please specify the task number to mark.");
        }
        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new PeterException("OOPS!!! Task number " + parts[1] + " does not exist.");
            }
            Task task = tasks.get(taskIndex);
            task.markAsDone();
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + task);
        } catch (NumberFormatException e) {
            throw new PeterException("OOPS!!! Task index must be a valid integer.");
        }
    }

    private static void handleUnmark(String input, List<Task> tasks) throws PeterException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new PeterException("OOPS!!! Please specify the task number to unmark.");
        }
        try {
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new PeterException("OOPS!!! Task number " + parts[1] + " does not exist.");
            }
            Task task = tasks.get(taskIndex);
            task.markAsUndone();
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + task);
        } catch (NumberFormatException e) {
            throw new PeterException("OOPS!!! Task index must be a valid integer.");
        }
    }

    private static void handleTodo(String input, List<Task> tasks) throws PeterException {
        String description = input.substring(4).trim();
        if (description.isEmpty()) {
            throw new PeterException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task task = new Todo(description);
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void handleDeadline(String input, List<Task> tasks) throws PeterException {
        String content = input.substring(8).trim();
        if (content.isEmpty()) {
            throw new PeterException("OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] parts = content.split(" /by ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new PeterException("OOPS!!! Please specify both description and /by time (e.g., deadline task /by Sunday).");
        }
        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void handleEvent(String input, List<Task> tasks) throws PeterException {
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
        Task task = new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    private static void printTaskAdded(Task task, int count) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
    }
}