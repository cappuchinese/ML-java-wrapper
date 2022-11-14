package nl.bioinf.ljbhu.WrapperT9;

import org.apache.commons.cli.*;

import java.util.List;

/**
 * Apache CommandLine Interface. Provides the options needed to run the program with a helper log if needed.
 * @author Lisa Hu (l.j.b.hu@st.hanze.nl)
 */
public class ApacheCliOptionsProvider {
    private final String[] clArgs;
    private final Options clOptions;

    private String inputFile;
    private String outputFile;

    /**
     * Constructor method.
     * @param clArgs commandline arguments
     */
    public ApacheCliOptionsProvider(final String[] clArgs) {
        this.clArgs = clArgs;
        this.clOptions = new Options();
    }

    /**
     * Main method.
     * @throws ParseException when invalid arguments are given
     */
    public void initialize() throws ParseException {
        // The help option
        Option helpOption = new Option("h", "help", false, "Print this help window");
        clOptions.addOption(helpOption);

        // Check if help option is specified
        boolean printHelp = checkHelp();
        // Build the rest of the options
        buildOptions();

        // Print the help window if specified or there are unknown arguments
        if (printHelp) {
            printHelpWindow();
            System.exit(0);
        }

        // Parse the rest and save as class attributes
        parseOptions();
    }

    /**
     * Builds the command line options
     */
    private void buildOptions() {
        Option inputFile = Option.builder("i").longOpt("input").argName("file-name")
                .desc(".arff type file with unclassified data").hasArg().required().build();
        Option outputFile = Option.builder("o").longOpt("output").argName("file-name")
                .desc("name of the output file to save the classified data").hasArg().build();

        clOptions.addOption(inputFile);
        clOptions.addOption(outputFile);
    }

    /**
     * Parse the command line arguments
     * @throws ParseException if there are any problems with the parsing
     */
    private void parseOptions() throws ParseException {
        // Parse commandline arguments
        CommandLine cmd = new DefaultParser().parse(clOptions, clArgs);

        // Set arguments as class attributes
        this.inputFile = cmd.getOptionValue("input");
        this.outputFile = cmd.getOptionValue("output");
    }

    /**
     * Check if the user used the help argument
     * @return if the user needs help or not
     * @throws ParseException if there are any problems with the parsing
     */
    private boolean checkHelp() throws ParseException {
        // Showing help window is false by default
        boolean hasHelp = false;

        // Parse CommandLine but don't throw exception at unknown argument
        CommandLine cmd = new DefaultParser().parse(clOptions, clArgs, true);
        // Get the list of arguments to check if help has been called incorrectly
        List<String> unparsed = cmd.getArgList();
        boolean checkHelpArg = unparsed.contains("-h") || unparsed.contains("--help");

        // Show help window when help option is used
        if (cmd.hasOption("help") || checkHelpArg || cmd.getArgList().isEmpty()) {
            hasHelp = true;
        }
        return hasHelp;
    }

    /**
     * Prints the help window
     */
    private void printHelpWindow() {
        HelpFormatter window = new HelpFormatter();
        window.printHelp("java -jar Theme09-ML-Application-0.2.0.jar", clOptions, true);
        System.out.println(); // Blank
    }

    /**
     * Getter method for class attribute inputFile
     * @return class attribute inputFile
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Getter method for class attribute outputFile
     * @return class attribute outputFile
     */
    public String getOutputFile() {
        return outputFile;
    }
}
