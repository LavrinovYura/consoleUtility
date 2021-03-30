package main.java;

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
    private final Map<String, Pair<Double, String>> result = new LinkedHashMap<>();
    private final Map<String, Double> preResult = new LinkedHashMap<>();

    public FileParameters(boolean h, boolean c, boolean si, List<String> files) {
        this.h = h;
        this.c = c;
        this.si = si;
        this.files = files;
    }

    public void sizeOfFiles(OutputStream outputStream) {
        double size;
        for (String element : files) {

            File file = new File(element);

            if (!file.exists()) {
                System.err.println("invalid file name/path or it doesn't exist");
                return;
            }
            if (file.isDirectory()) size = FileUtils.sizeOfDirectory(file);
            else size = file.length();

            if (c) sum += size;

            preResult.put(element, size);
        }
        if (c) preResult.put("sum", sum);
        humanView();

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            int counter = 1;
            for (Map.Entry<String, Pair<Double, String>> entry : result.entrySet()) {
                if (result.size() != counter || !c) outputStreamWriter.write("Size of " + entry.getKey() + " "
                        + String.format("%.3f", entry.getValue().getFirst()) + " " + entry.getValue().getSecond() + "\n");
                else outputStreamWriter.write("Sum of all " + String.format("%.3f", entry.getValue().getFirst())
                        + " " + entry.getValue().getSecond());
                counter++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void humanView() {
        double hu = si ? 1000 : 1024;
        for (Map.Entry<String, Double> entry : preResult.entrySet()) {
            int flag = 0;
            double changedSize = entry.getValue();
            do {
                changedSize /= hu;
                flag++;
                if (!h) break;
            } while (changedSize > hu);

            result.put(entry.getKey(), new Pair<>(changedSize, sizeS[flag]));
        }
    }
}
