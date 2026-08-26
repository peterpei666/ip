import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) throws PeterException {
        if (index < 0 || index >= tasks.size()) {
            throw new PeterException("OOPS!!! Task number " + (index + 1) + " does not exist.");
        }
        return tasks.remove(index);
    }

    public Task mark(int index) throws PeterException {
        if (index < 0 || index >= tasks.size()) {
            throw new PeterException("OOPS!!! Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    public Task unmark(int index) throws PeterException {
        if (index < 0 || index >= tasks.size()) {
            throw new PeterException("OOPS!!! Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    public Task get(int index) {
        return tasks.get(index);
    }
}