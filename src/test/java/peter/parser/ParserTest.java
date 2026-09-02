package peter.parser;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.Task;
import peter.task.Todo;


public class ParserTest {

    @Test
    public void parseTodo_validInput_success() throws PeterException {
        Task task = Parser.parseTodo("todo read book");
        assertTrue(task instanceof Todo);
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void parseTodo_emptyDescription_exceptionThrown() {
        PeterException exception = assertThrows(PeterException.class, () -> {
            Parser.parseTodo("todo   ");
        });
        assertEquals("OOPS!!! The description of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    public void parseDeadline_validInput_success() throws PeterException {
        Task task = Parser.parseDeadline("deadline return book /by Sunday");
        assertTrue(task instanceof Deadline);
        assertEquals("[D][ ] return book (by: Sunday)", task.toString());
    }

    @Test
    public void parseDeadline_missingByFormat_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseDeadline("deadline return book Sunday");
        });
    }

    @Test
    public void parseIndex_validInteger_returnsZeroBasedIndex() throws PeterException {
        int index = Parser.parseIndex("mark 2");
        assertEquals(1, index);
    }

    @Test
    public void parseIndex_invalidInteger_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseIndex("mark abc");
        });
    }
}
