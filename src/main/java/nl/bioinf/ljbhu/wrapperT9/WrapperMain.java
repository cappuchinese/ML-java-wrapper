package nl.bioinf.ljbhu.wrapperT9;

public class WrapperMain {
    private WrapperMain() {}

    public static void main(String[] args) {
        ApacheCliOptionsProvider optionsProvider = new ApacheCliOptionsProvider(args);
        try {
            optionsProvider.initialize();

            // Get the different variables
            String inputFile = optionsProvider.getInputFile();
            String outputFile = optionsProvider.getOutputFile();

            // Run the classifier
            WekaRunner wekaRunner = new WekaRunner(inputFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
