package peter.parser;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import peter.exception.PeterException;
import peter.task.Deadline;
import peter.task.Event;
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
        assertEquals("That waypoint needs a description. Try: todo read book", exception.getMessage());
    }

    @Test
    public void parseDeadline_validInput_success() throws PeterException {
        Task task = Parser.parseDeadline("deadline return book /by 2019-12-02 1800");
        assertTrue(task instanceof Deadline);
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00 PM)", task.toString());
    }

    @Test
    public void parseDeadline_missingByFormat_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseDeadline("deadline return book Sunday");
        });
    }

    @Test
    public void parseDeadline_flexibleWhitespace_success() throws PeterException {
        Task task = Parser.parseDeadline("  deadline   return   book   /by   2019-12-02   1800  ");

        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00 PM)", task.toString());
    }

    @Test
    public void parseDeadline_duplicateBy_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseDeadline("deadline return book /by 2019-12-02 /by 1800");
        });
    }

    @Test
    public void parseDeadline_nonExistentDate_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseDeadline("deadline return book /by 2026-02-30 1800");
        });
    }

    @Test
    public void parseEvent_validTimeRange_success() throws PeterException {
        Task task = Parser.parseEvent("event meeting /from 0930 /to 1600");

        assertTrue(task instanceof Event);
        assertEquals("[E][ ] meeting (from: 0930 to: 1600)", task.toString());
    }

    @Test
    public void parseEvent_endNotAfterStart_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseEvent("event meeting /from 1600 /to 1600");
        });
        assertThrows(PeterException.class, () -> {
            Parser.parseEvent("event meeting /from 1700 /to 1600");
        });
    }

    @Test
    public void parseEvent_invalidTime_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseEvent("event meeting /from 2560 /to 2700");
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

    @Test
    public void parseIndex_extraParameter_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseIndex("mark 1 extra");
        });
    }

    @Test
    public void parseViewDate_nonExistentDate_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseViewDate("view 2026-02-30");
        });
    }

    @Test
    public void parseTodo_reservedDelimiter_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.parseTodo("todo read | write notes");
        });
    }

    @Test
    public void requireNoArguments_trailingParameter_exceptionThrown() {
        assertThrows(PeterException.class, () -> {
            Parser.requireNoArguments("list now", "list");
        });
    }
}
