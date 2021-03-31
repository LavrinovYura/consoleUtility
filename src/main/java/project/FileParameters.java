package project;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class FileParameters {
    private final boolean h;
    private final boolean c;
    private final boolean si;
    private final List<String> files;
    private double sum;
    private final String[] sizeS = {"B", "KB", "MB", "GB"};

    public FileParameters(boolean h, boolean c, boolean si, List<String> files) {
        this.h = h;
        this.c = c;
        this.si = si;
        this.files = files;
    }

    public Map<String, Double> sizeOfFiles() {
        LinkedHashMap<String, Double> preSizes = new LinkedHashMap<>();
        double size;
        for (String element : files) {

            File file = new File(element);

            if (!file.exists()) {
                System.err.println("invalid file name/path or it doesn't exist");
            }
            if (file.isDirectory()) size = FileUtils.sizeOfDirectory(file);
            else size = file.length();

            if (c) sum += size;

            preSizes.put(element, size);
        }
        if (c) preSizes.put("sum", sum);
        return preSizes;
    }

    public Map<String, Pair<Double, String>> humanView(Map<String, Double> preResult) {
        Map<String, Pair<Double, String>> humanResult = new LinkedHashMap<>();
        double hu = si ? 1000 : 1024;
        for (Map.Entry<String, Double> entry : preResult.entrySet()) {
            int flag = 0;
            double changedSize = entry.getValue();
            do {
                changedSize /= hu;
                flag++;
                if (!h) break;
            } while (changedSize > hu);
            humanResult.put(entry.getKey(), new Pair<>(changedSize, sizeS[flag]));
        }
        return humanResult;
    }

    public void outputFile(OutputStream outputStream, Map<String, Pair<Double, String>> finalResult) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            int counter = 1;
            for (Map.Entry<String, Pair<Double, String>> entry : finalResult.entrySet()) {
                if (finalResult.size() != counter || !c) outputStreamWriter.write("Size of " + entry.getKey() + " "
                        + String.format("%.3f", entry.getValue().getFirst()) + " " + entry.getValue().getSecond() + "\n");
                else outputStreamWriter.write("Sum of all " + String.format("%.3f", entry.getValue().getFirst())
                        + " " + entry.getValue().getSecond());
                counter++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
