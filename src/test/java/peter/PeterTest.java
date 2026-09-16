package peter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PeterTest {
    @TempDir
    private Path tempDir;

    @Test
    public void getResponseResult_unknownCommand_marksResponseAsError() {
        Peter peter = new Peter(tempDir.resolve("tasks.txt").toString());

        Peter.Response response = peter.getResponseResult("abracadabra");

        assertTrue(response.isError());
        assertTrue(response.message().contains("route"));
    }

    @Test
    public void getResponseResult_validCommand_marksResponseAsNormal() {
        Peter peter = new Peter(tempDir.resolve("tasks.txt").toString());

        Peter.Response response = peter.getResponseResult("list");

        assertFalse(response.isError());
        assertTrue(response.message().contains("map"));
    }

    @Test
    public void getWelcomeMessage_usesPeterNavigatorPersonality() {
        Peter peter = new Peter(tempDir.resolve("tasks.txt").toString());

        String welcomeMessage = peter.getWelcomeMessage();

        assertTrue(welcomeMessage.contains("Peter"));
        assertTrue(welcomeMessage.contains("chart"));
    }

    @Test
    public void getResponseResult_noArgumentCommandWithExtraText_marksResponseAsError() {
        Peter peter = new Peter(tempDir.resolve("tasks.txt").toString());

        Peter.Response response = peter.getResponseResult("list please");

        assertTrue(response.isError());
    }

    @Test
    public void getResponseResult_duplicateTask_marksSecondResponseAsError() {
        Peter peter = new Peter(tempDir.resolve("tasks.txt").toString());
        peter.getResponseResult("todo read book");

        Peter.Response response = peter.getResponseResult("todo READ BOOK");

        assertTrue(response.isError());
        assertTrue(response.message().contains("already"));
    }

    @Test
    public void getResponseResult_unwritableDestination_reportsSaveError() {
        Peter peter = new Peter(tempDir.toString());

        Peter.Response response = peter.getResponseResult("todo read book");

        assertTrue(response.isError());
        assertTrue(response.message().contains("couldn't save"));
    }

    @Test
    public void getLoadingWarning_storagePathIsDirectory_returnsWarning() {
        Peter peter = new Peter(tempDir.toString());

        String warning = peter.getLoadingWarning();

        assertTrue(warning.contains("isn't a regular file"));
    }
}
