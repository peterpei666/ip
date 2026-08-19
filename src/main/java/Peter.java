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
                System.out.println(line);

                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (input.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        Task task = tasks.get(taskIndex);
                        task.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + task);
                    }
                } else if (input.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        Task task = tasks.get(taskIndex);
                        task.markAsUndone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + task);
                    }
                } else if (input.startsWith("todo ")) {
                    Task task = new Todo(input.substring(5));
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());
                } else if (input.startsWith("deadline ")) {
                    String[] parts = input.substring(9).split(" /by ");
                    Task task = new Deadline(parts[0], parts[1]);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());
                } else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from ");
                    String[] timeParts = parts[1].split(" /to ");
                    Task task = new Event(parts[0], timeParts[0], timeParts[1]);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());
                }

                System.out.println(line);
            }
        }
    }

    private static void printTaskAdded(Task task, int count) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
    }
}