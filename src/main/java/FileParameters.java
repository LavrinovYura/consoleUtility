package main.java;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class FileParameters {
    private final boolean h;
    private final boolean c;
    private final boolean si;
    private double sum = 0;
    private int flag;
    private final List<String> files;

    public FileParameters(boolean h, boolean c, boolean si, List<String> files) {
        this.h = h;
        this.c = c;
        this.si = si;
        this.files = files;
    }

    public void sizeOfFiles(OutputStream outputStream) {

        String[] sizeS = {"B", "KB", "MB", "GB"};
        double hu = si ? 1000 : 1024;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
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

                size = humanView(size, hu);

                outputStreamWriter.write("Size of " + element + " " +
                        String.format("%.3f", size) + " " + sizeS[flag] + '\n');

            }
            size = humanView(sum, hu);
            if (c) outputStreamWriter.write("Sum of all " + String.format("%.3f", size) + " " + sizeS[flag]);

            outputStreamWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public double humanView(double len, double system) {
        flag = 0;
        do {
            len /= system;
            flag++;
            if (!h) break;
        } while (len > system);
        return len;
    }
}

