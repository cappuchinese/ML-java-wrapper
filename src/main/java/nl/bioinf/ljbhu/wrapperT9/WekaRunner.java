package nl.bioinf.ljbhu.wrapperT9;

import java.io.InputStream;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class WekaRunner {
    private AbstractClassifier benignModel;
    private AbstractClassifier controlModel;
    final String benignFilename = "/benign.model";
    final String controlFilename = "/control.model";

    public WekaRunner(String inputFile) throws Exception {
        try {
            Instances newInstances = loadFile(inputFile);
            loadModel();
            System.out.println("\nUnclassified input instances = \n" + newInstances);
            classifyInput(benignModel, newInstances);
            classifyInput(controlModel, newInstances);
        } catch (Exception exception) {
            throw new Exception(String.format("Given file could not be read: (%s) %s",
                    exception.getClass().getSimpleName(), exception.getMessage()));
        }
    }

    private void classifyInput(AbstractClassifier model, Instances unknownInput) throws Exception {
        Instances labeled = new Instances(unknownInput);
        for (int i = 0; i < unknownInput.numInstances(); i++) {
            double clsLabel = model.classifyInstance(unknownInput.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }

    private void loadModel() throws Exception {
        try {
            InputStream benignFile = getClass().getResourceAsStream(benignFilename);
            InputStream controlFile = getClass().getResourceAsStream(controlFilename);
            this.benignModel = (AbstractClassifier) weka.core.SerializationHelper.read(benignFile);
            this.controlModel = (AbstractClassifier) weka.core.SerializationHelper.read(controlFile);
        } catch (Exception exception) {
            throw new Exception(String.format("Given file could not be read: (%s) %s",
                    exception.getClass().getSimpleName(), exception.getMessage()));
        }
    }

    private Instances loadFile(String datafile) throws Exception {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception exception) {
            throw new Exception(String.format("Given file could not be read: (%s) %s",
                    exception.getClass().getSimpleName(), exception.getMessage()));
        }
    }
}
