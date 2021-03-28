package main.java;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

    public FileParameters(boolean h, boolean c, boolean si, List<String> files) {
        this.h = h;
        this.c = c;
        this.si = si;
        this.files = files;
    }

    public void sizeOfFiles(OutputStream outputStream) {

        double hu = si ? 1000 : 1024;
        double size;
        for (String element : files) {

            File file = new File(element);

            if (!file.exists()) {
                System.err.println("invalid file name/path or it doesn't exist");
                return;
            }
            if (file.isDirectory()) size = FileUtils.sizeOfDirectory(file);
            else size = file.length();

            humanView(size, hu, element);
        }
        if (c) humanView(sum, hu, "");

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Pair<Double, String>> entry : result.entrySet()) {
                outputStreamWriter.write(entry.getKey() + " " + String.format("%.3f", entry.getValue().getFirst()) + " "
                        + entry.getValue().getSecond() + "\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void humanView(double len, double system, String name) {
        if (c) sum += len;
        int flag = 0;
        do {
            len /= system;
            flag++;
            if (!h) break;
        } while (len > system);

        if (!name.equals("")) result.put("Size of " + name, new Pair<>(len, sizeS[flag]));
        else result.put("Sum of all" + name, new Pair<>(len, sizeS[flag]));
    }
}
