package main.java;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;


public class Du {

    @Option(name = "-h", usage = "Human view")
    private boolean humanSize;

    @Option(name = "-c", usage = "Sum of all")
    private boolean allSize;

    @Option(name = "--si", usage = "System = 1000")
    private boolean basement;

    @Argument(required = true, usage = "Input file name")
    private  List<String> inputFileName = new ArrayList<>();


    public static void main(String[] args) {
        new Du().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            return;
        }

        FileParameters fileParameters = new FileParameters(humanSize,
                allSize,
                basement,
                inputFileName);
        fileParameters.sizeOfFiles(System.out);
    }
}