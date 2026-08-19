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

        List<String> tasks = new ArrayList<>();

        System.out.println(logo);
        System.out.println("Hello! I'm Peter.");
        System.out.println("What can I do for you?");
        System.out.println(line);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                System.out.println(line);

                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + ". " + tasks.get(i));
                    }
                } else {
                    tasks.add(input);
                    System.out.println("     added: " + input);
                }

                System.out.println(line);
            }
        }
    }
}