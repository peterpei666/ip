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

        System.out.println(logo);
        System.out.println("Hello! I'm Peter.");
        System.out.println("What can I do for you?");
        System.out.println(line);


        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                System.out.println(line);

                if (input.equals("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(line);
                    break;
                }
                
                System.out.println("     " + input);
                System.out.println(line);
            }
        }
    }
}