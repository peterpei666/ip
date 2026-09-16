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
}
