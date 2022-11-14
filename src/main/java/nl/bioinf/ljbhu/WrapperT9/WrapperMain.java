package nl.bioinf.ljbhu.WrapperT9;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;

import static java.util.Objects.isNull;

public class WrapperMain {
    /**
     * Constructor method. Private because it should not be used.
     */
    private WrapperMain() {}

    /**
     * Main method.
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        ApacheCliOptionsProvider optionsProvider = new ApacheCliOptionsProvider(args);
        try {
            optionsProvider.initialize();

            // Get the different variables
            String inputFile = optionsProvider.getInputFile();
            String outputFile = optionsProvider.getOutputFile();

            // Run the classifier
            WekaRunner wekaRunner = new WekaRunner(inputFile);
            Instances classifiedData = wekaRunner.getComparedClassified();

            // Write the output
            if (isNull(outputFile)) {
                outputFile = "output.arff";
            }
            else {
                outputFile += ".arff";
            }
            outputWriter(outputFile, classifiedData);
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }

    /**
     * Writes an output file
     * @param outputFile path/name of the output file
     * @param classifiedData the classified instances from the WekaRunner
     */
    private static void outputWriter(String outputFile, Instances classifiedData) {
        try {
            DataSink.write(outputFile, classifiedData);
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }
}
