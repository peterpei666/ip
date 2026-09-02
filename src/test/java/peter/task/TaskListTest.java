package peter.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void delete_invalidIndex_exceptionThrown() {
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
    public void mark_validIndex_taskMarkedAsDone() throws PeterException {
        TaskList tasks = new TaskList();
        Task todo = new Todo("read book");
        tasks.add(todo);

        Task marked = tasks.mark(0);
        assertEquals("[T][X] read book", marked.toString());
    }
}
