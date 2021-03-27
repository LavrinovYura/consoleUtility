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
    private double sum;
    private final String[] sizeS = {"B", "KB", "MB", "GB"};
    List<String> result = new ArrayList<>();

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
       if(c) humanView(sum, hu,"");

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            for (String element : result) {
                outputStreamWriter.write(element);
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

       if (!name.equals("")) result.add("Size of " + name + " " + String.format("%.3f",len)
                + " " + sizeS[flag] + '\n');
       else   result.add("Sum of all " + String.format("%.3f", len)
               + " " + sizeS[flag]);
    }
}
