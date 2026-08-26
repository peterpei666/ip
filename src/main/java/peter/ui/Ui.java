package peter.ui;

import java.util.Scanner;

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