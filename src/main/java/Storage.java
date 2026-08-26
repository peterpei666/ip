import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private static final String FILE_PATH = Paths.get(".", "data", "peter.txt").toString();

    public static List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("     [Warning] Skipping corrupted line in storage: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("     [Warning] Could not load data from storage: " + e.getMessage());
        }
        return tasks;
    }

    public static void save(List<Task> tasks) {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("     [Error] Failed to save tasks to file: " + e.getMessage());
        }
    }

    private static Task parseTaskFromFile(String line) throws PeterException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new PeterException("Corrupted format");
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) throw new PeterException("Corrupted deadline format");
                task = new Deadline(description, parts[3].trim());
                break;
            case "E":
                if (parts.length < 5) throw new PeterException("Corrupted event format");
                task = new Event(description, parts[3].trim(), parts[4].trim());
                break;
            default:
                throw new PeterException("Unknown task type");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}