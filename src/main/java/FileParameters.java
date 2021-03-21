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
            for (String element : files) {
                File file = new File(element);

                if (!file.exists()) throw new IllegalArgumentException();

                if (file.isDirectory()) {
                    double sizeDirect = FileUtils.sizeOfDirectory(new File(element));
                    if (c) sum += sizeDirect;
                    sizeDirect = humanView(sizeDirect, hu);
                    outputStreamWriter.write("Size of directory " + element + " " +
                            String.format("%.3f",sizeDirect) + " " + sizeS[flag] + '\n');
                } else {
                    double res = file.length();
                    if (c) sum += res;
                    res = humanView(res, hu);
                    outputStreamWriter.write("Size of file " + file + " " +
                            String.format("%.3f",res)+ " " + sizeS[flag] + "\n");
                }
            }
            if (c) outputStreamWriter.write("Sum of all " +   String.format("%.3f",humanView(sum, hu)) + " " + sizeS[flag]);
            outputStreamWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public double humanView(double len, double system) {
        flag = 0;
        if (!h) {
            flag = 1;
            return len / system;
        } else do {
            len /= system;
            flag++;
        } while (len > system);
        return len;
    }
}

