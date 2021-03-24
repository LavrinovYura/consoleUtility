package main.java;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileParameters {
    private final boolean h;
    private final boolean c;
    private final boolean si;
    private final List<String> files;


    public FileParameters(boolean h, boolean c, boolean si, List<String> files) {
        this.h = h;
        this.c = c;
        this.si = si;
        this.files = files;
    }

    public void sizeOfFiles(OutputStream outputStream) {

        List<String> result = new ArrayList<>();
        double hu = si ? 1000 : 1024;
        double size;
        Pair<Double, String> pair;
        double sum = 0;

        for (String element : files) {

            File file = new File(element);

            if (!file.exists()) {
                System.err.println("invalid file name/path or it doesn't exist");
                return;
            }

            if (file.isDirectory()) size = FileUtils.sizeOfDirectory(file);
            else size = file.length();

            if (c) sum += size;

            pair = humanView(size, hu);

            result.add("Size of " + element + " " + String.format("%.3f", pair.getFirst())
                    + " " + pair.getSecond() + '\n');
        }
        pair = humanView(sum, hu);
        if (c) result.add("Sum of all " + String.format("%.3f", pair.getFirst())
                + " " + pair.getSecond());

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            for (String element : result) {
                outputStreamWriter.write(element);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public Pair<Double, String> humanView(double len, double system) {
        String[] sizeS = {"B", "KB", "MB", "GB"};
        int flag = 0;
        do {
            len /= system;
            flag++;
            if (!h) break;
        } while (len > system);
        return new Pair<>(len, sizeS[flag]);
    }
}
