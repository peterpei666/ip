package peter.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import peter.exception.PeterException;

public class TaskListTest {

    @Test
    public void delete_validIndex_taskRemovedSuccessfully() throws PeterException {
        TaskList tasks = new TaskList();
        Task todo = new Todo("read book");
        tasks.add(todo);

        Task removed = tasks.delete(0);
        assertEquals(todo, removed);
        assertEquals(0, tasks.size());
    }

    @Test
    public void delete_invalidIndex_exceptionThrown() throws PeterException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(PeterException.class, () -> {
            tasks.delete(1);
        });

        assertThrows(PeterException.class, () -> {
            tasks.delete(-1);
        });
    }

    @Test
    public void add_duplicateTask_exceptionThrown() throws PeterException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(PeterException.class, () -> {
            tasks.add(new Todo("READ BOOK"));
        });
        assertEquals(1, tasks.size());
    }

    @Test
    public void mark_validIndex_taskMarkedAsDone() throws PeterException {
        TaskList tasks = new TaskList();
        Task todo = new Todo("read book");
        tasks.add(todo);

        Task marked = tasks.mark(0);
        assertEquals("[T][X] read book", marked.toString());
    }

    @Test
    public void findTasks_matchingKeyword_returnsMatchingTasksIgnoringCase() {
        Task matchingTask = new Todo("Read Book");
        TaskList tasks = new TaskList(List.of(matchingTask, new Todo("buy groceries")));

        assertEquals(List.of(matchingTask), tasks.findTasks("book"));
    }

    @Test
    public void getTasksOnDate_matchingDeadline_returnsMatchingDeadlines() throws PeterException {
        Task matchingDeadline = new Deadline("return book", "2019-12-02 1800");
        TaskList tasks = new TaskList(List.of(
                matchingDeadline,
                new Deadline("submit report", "2019-12-03 1800"),
                new Todo("plan December 2 activities")));

        assertEquals(List.of(matchingDeadline), tasks.getTasksOnDate(LocalDate.of(2019, 12, 2)));
    }

    @Test
    public void sortByDeadline_mixedTasks_deadlinesSortedBeforeOtherTasks() throws PeterException {
        Task laterDeadline = new Deadline("submit report", "2019-12-03 1800");
        Task firstTodo = new Todo("buy groceries");
        Task earlierDeadline = new Deadline("return book", "2019-12-02 1800");
        Task secondTodo = new Todo("read notes");
        TaskList tasks = new TaskList(List.of(laterDeadline, firstTodo, earlierDeadline, secondTodo));

        tasks.sortByDeadline();

        assertEquals(List.of(earlierDeadline, laterDeadline, firstTodo, secondTodo), tasks.getTasks());
    }
}
