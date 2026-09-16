package peter.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import peter.exception.PeterException;
import peter.task.TaskList;
import peter.task.Todo;

public class StorageTest {
    @TempDir
    private Path tempDir;

    @Test
    public void load_missingFile_returnsEmptyResult() throws PeterException {
        Storage storage = new Storage(tempDir.resolve("missing.txt").toString());

        Storage.LoadResult result = storage.load();

        assertEquals(0, result.tasks().size());
        assertEquals(0, result.skippedEntries());
    }

    @Test
    public void load_mixedValidAndDamagedEntries_recoversValidEntries() throws IOException, PeterException {
        Path dataFile = tempDir.resolve("tasks.txt");
        Files.writeString(dataFile, "T | 0 | read book\n"
                + "D | maybe | damaged | 2026-09-10 1800\n"
                + "T | 0 | READ BOOK\n"
                + "not a task\n");
        Storage storage = new Storage(dataFile.toString());

        Storage.LoadResult result = storage.load();

        assertEquals(1, result.tasks().size());
        assertEquals(3, result.skippedEntries());
    }

    @Test
    public void save_validTasks_replacesDataFile() throws IOException, PeterException {
        Path dataFile = tempDir.resolve("nested/tasks.txt");
        Storage storage = new Storage(dataFile.toString());
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        storage.save(tasks);

        assertEquals("T | 0 | read book" + System.lineSeparator(), Files.readString(dataFile));
    }

    @Test
    public void save_destinationIsDirectory_exceptionThrown() {
        Storage storage = new Storage(tempDir.toString());

        assertThrows(PeterException.class, () -> {
            storage.save(new TaskList());
        });
    }
}
