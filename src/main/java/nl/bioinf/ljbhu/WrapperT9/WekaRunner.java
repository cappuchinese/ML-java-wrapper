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
 * @author Lisa Hu (414264)
 */
public class WekaRunner {
    private AbstractClassifier benignModel;
    private AbstractClassifier controlModel;
    final String benignFilename = "/benign.model";
    final String controlFilename = "/control.model";
    private Instances comparedClassified;

    /**
     * Constructor method to run all the methods in the class.
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

    private Instances classifyInput(AbstractClassifier model, Instances unknownInstances) {
        Instances labeledInstances = new Instances(unknownInstances);
        try {
            for (int i = 0; i < unknownInstances.numInstances(); i++) {
                 double clsLabel = model.classifyInstance(unknownInstances.instance(i));
                 labeledInstances.instance(i).setClassValue(clsLabel);
            }
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
        return labeledInstances;
    }

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

    private void loadModel() {
        try {
            InputStream benignFile = getClass().getResourceAsStream(benignFilename);
            InputStream controlFile = getClass().getResourceAsStream(controlFilename);
            this.benignModel = (AbstractClassifier) weka.core.SerializationHelper.read(benignFile);
            this.controlModel = (AbstractClassifier) weka.core.SerializationHelper.read(controlFile);
        } catch (Exception exception) {
            System.out.println((String.format("(Line %d) %s: %s",
                    exception.getStackTrace()[0].getLineNumber(),
                    exception.getClass().getSimpleName(), exception.getMessage())));
        }
    }

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

    public Instances getComparedClassified() {
        return comparedClassified;
    }
}
