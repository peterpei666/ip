package peter.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import peter.exception.PeterException;

/**
 * Manages the collection of tasks and operations performed on it.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list containing the supplied tasks.
     *
     * @param tasks Initial tasks to copy into this task list.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Task list must not be null";
        this.tasks = new ArrayList<>(tasks);
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "Cannot add a null task";
        tasks.add(task);
    }

    /**
     * Removes the task at the specified zero-based index.
     *
     * @param index Zero-based index of the task to remove.
     * @return The removed task.
     * @throws PeterException If the index is outside the task list.
     */
    public Task delete(int index) throws PeterException {
        validateIndex(index);
        assert index >= 0 && index < tasks.size() : "Task index should have been validated";
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified zero-based index as completed.
     *
     * @param index Zero-based index of the task to mark.
     * @return The task that was marked.
     * @throws PeterException If the index is outside the task list.
     */
    public Task mark(int index) throws PeterException {
        validateIndex(index);
        assert index >= 0 && index < tasks.size() : "Task index should have been validated";
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the specified zero-based index as incomplete.
     *
     * @param index Zero-based index of the task to unmark.
     * @return The task that was unmarked.
     * @throws PeterException If the index is outside the task list.
     */
    public Task unmark(int index) throws PeterException {
        validateIndex(index);
        assert index >= 0 && index < tasks.size() : "Task index should have been validated";
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    /**
     * Checks that an index identifies an existing task.
     *
     * @param index Zero-based task index to check.
     * @throws PeterException If the index is outside the task list.
     */
    private void validateIndex(int index) throws PeterException {
        if (index < 0 || index >= tasks.size()) {
            throw new PeterException("OOPS!!! Task number " + (index + 1) + " does not exist.");
        }
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
        return tasks.stream()
                .filter(task -> task instanceof Deadline deadline
                        && deadline.getBy().toLocalDate().equals(date))
                .toList();
    }

    /**
     * Finds tasks whose description contains the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return A list of matching tasks.
     */
    public List<Task> findTasks(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword))
                .toList();
    }
}
