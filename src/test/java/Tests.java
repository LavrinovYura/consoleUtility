import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.FileParameters;
import project.Pair;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    ByteArrayOutputStream stream;

    @BeforeEach
    public void initStream() {
        stream = new ByteArrayOutputStream();
    }

    @Test
    public void firstTest() {
        List<String> filesPath = Arrays.asList("files\\car", "files\\direct", "files\\direct\\papka");
        FileParameters fileParameters = new FileParameters(true, false, false, filesPath);
        Map<String, Double> preResult = fileParameters.sizeOfFiles(filesPath);
        Map<String, Pair<Double, String>> result = fileParameters.humanView(preResult);
        fileParameters.outputFile(stream, result);
        String actual = stream.toString(StandardCharsets.UTF_8);
        String expected = "Size of files\\car 215,682 KB\n" +
                "Size of files\\direct 7,583 MB\n" +
                "Size of files\\direct\\papka 6,529 MB\n";
        assertEquals(expected, actual);
    }

    @Test
    public void secondTest() {
        List<String> filesPath = Arrays.asList("files\\fd", "files\\direct", "files\\direct\\papka");
        FileParameters fileParameters = new FileParameters(true, false, false, filesPath);
        Map<String, Double> preResult = fileParameters.sizeOfFiles(filesPath);
        Map<String, Pair<Double, String>> result = fileParameters.humanView(preResult);
        fileParameters.outputFile(stream, result);
        String actual = stream.toString(StandardCharsets.UTF_8);
        String expected = "Size of files\\fd 0,001 KB\n" +
                "Size of files\\direct 7,583 MB\n" +
                "Size of files\\direct\\papka 6,529 MB\n";
        assertEquals(expected, actual);
    }

    @Test
    public void thirdTest() {
        List<String> filesPath = Arrays.asList("files\\fd", "files\\direct", "files\\direct\\papka");
        FileParameters fileParameters = new FileParameters(false, false, false, filesPath);
        Map<String, Double> preResult = fileParameters.sizeOfFiles(filesPath);
        Map<String, Pair<Double, String>> result = fileParameters.humanView(preResult);
        fileParameters.outputFile(stream, result);
        String actual = stream.toString(StandardCharsets.UTF_8);
        String expected = "Size of files\\fd 0,001 KB\n" +
                "Size of files\\direct 7764,567 KB\n" +
                "Size of files\\direct\\papka 6686,131 KB\n";
        assertEquals(expected, actual);
    }

    @Test
    public void fourthTest() {
        List<String> filesPath = Arrays.asList("files\\fd", "files\\direct", "files\\direct\\papka");
        FileParameters fileParameters = new FileParameters(false, true, true,filesPath);
        Map<String, Double> preResult = fileParameters.sizeOfFiles(filesPath);
        Map<String, Pair<Double, String>> result = fileParameters.humanView(preResult);
        fileParameters.outputFile(stream, result);
        String actual = stream.toString(StandardCharsets.UTF_8);
        String expected = "Sum of all 14797,516 KB";
        assertEquals(expected, actual);
    }
}
