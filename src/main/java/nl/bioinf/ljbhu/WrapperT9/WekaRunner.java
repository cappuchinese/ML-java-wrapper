package nl.bioinf.ljbhu.WrapperT9;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * This class classifies the instances based on the two models.
 * Models are based on the Logistic algorithm and optimized using ThresholdSelector.
 * @author Lisa Hu (l.j.b.hu@st.hanze.nl)
 */
public class WekaRunner {
    private AbstractClassifier benignModel;
    private AbstractClassifier controlModel;
    final String benignFilename = "/benign.model";
    final String controlFilename = "/control.model";
    private Instances comparedClassified;

    /**
     * Constructor method.
     * @param inputFile path to the unclassified instances
     */
    public WekaRunner(String inputFile) {
        try {
            // Load the model
            loadModel();
            // Load the unclassified instances
            Instances newInstances = loadFile(inputFile);
            System.out.println("\nNumber of unclassified instances: " + newInstances.numInstances());
            // Classify the instances
            Instances controlClassified = classifyInput(controlModel, newInstances);
            Instances benignClassified = classifyInput(benignModel, newInstances);
            // Set the same instances after each other for comparison
            compareInstances(controlClassified, benignClassified);
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }

    /**
     * Classify the unclassified instances based on the loaded predictive model.
     * @param model the predictive model.
     * @param unknownInstances  all the instances that need to be classified
     * @return the classified instances
     */
    private Instances classifyInput(AbstractClassifier model, Instances unknownInstances) {
        // Create a new Instances object for the labeled instances
        Instances labeledInstances = new Instances(unknownInstances);
        try {
            // Classify each instance
            for (int i = 0; i < unknownInstances.numInstances(); i++) {
                 double clsLabel = model.classifyInstance(unknownInstances.instance(i));
                 // Set the instance to the classification label
                 labeledInstances.instance(i).setClassValue(clsLabel);
            }
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
        return labeledInstances;
    }

    /**
     * Creates a new Instances object that puts the same instance from the different model underneath each other
     * for easier comparison. Stores the comparison as class attribute.
     * @param controlClassified the classified instances from the control model
     * @param benignClassified the classified instances from the benign model
     */
    private void compareInstances(Instances controlClassified, Instances benignClassified) {
        try {
            for (int i = 0; i < controlClassified.numInstances(); i++) {
                comparedClassified.add(controlClassified.instance(i));
                comparedClassified.add(benignClassified.instance(i));
            }
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }

    /**
     * Load the models and store as class attribute
     */
    private void loadModel() {
        try {
            // Get the model files
            InputStream benignFile = getClass().getResourceAsStream(benignFilename);
            InputStream controlFile = getClass().getResourceAsStream(controlFilename);
            // Read the models
            this.benignModel = (AbstractClassifier) weka.core.SerializationHelper.read(benignFile);
            this.controlModel = (AbstractClassifier) weka.core.SerializationHelper.read(controlFile);
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }

    /**
     * Loads the unclassified instances from the file. Creates the structure for comparedClassified.
     * @param datafile path to the file with unclassified instances
     * @return loaded instances
     * @throws Exception if the path to the file is invalid
     */
    private Instances loadFile(String datafile) throws Exception {
        try {
            // Load data
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            comparedClassified = source.getStructure();
            // Try to write the class attribute
            try {
                // Create class labels
                List<String> classLabels = new ArrayList<>(Arrays.asList("Healthy", "Malignant"));
                // Create new attribute
                Attribute newClassAttribute = new Attribute("diagnosis", classLabels);
                // Add attribute to data
                data.insertAttributeAt(newClassAttribute, data.numAttributes());
                // Set last column to class attribute
                data.setClassIndex(data.numAttributes() - 1);
            } catch (IllegalArgumentException exception) {
                // Set class attribute to last column
                data.setClassIndex(data.numAttributes() - 1);
            }
        return data;
        } catch (Exception exception) {
            throw new Exception(String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage()));
        }
    }

    /**
     * Getter method for the class attribute comparedClassified
     * @return class attribute comparedClassified
     */
    public Instances getComparedClassified() {
        return comparedClassified;
    }
}
