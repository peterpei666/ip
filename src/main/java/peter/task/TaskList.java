package peter.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import peter.exception.PeterException;

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

    /**
     * Finds and prints tasks that fall on the specified date.
     *
     * @param date Target date to filter tasks.
     * @return List of matching tasks on that date.
     */
    public List<Task> getTasksOnDate(LocalDate date) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().toLocalDate().equals(date)) {
                    matchingTasks.add(deadline);
                }
            }
        }
        return matchingTasks;
    }

    /**
     * Finds tasks whose description contains the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return A list of matching tasks.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}