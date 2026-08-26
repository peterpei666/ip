package peter.storage;

import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.Event;
import peter.task.Task;
import peter.task.TaskList;
import peter.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;
    
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() throws PeterException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

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
                    // 忽略损坏数据
                }
            }
        } catch (IOException e) {
            throw new PeterException("Could not read file: " + filePath);
        }
        return tasks;
    }

    public void save(TaskList taskList) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : taskList.getTasks()) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("     [Error] Failed to save tasks to file: " + e.getMessage());
        }
    }

    private Task parseTaskFromFile(String line) throws PeterException {
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