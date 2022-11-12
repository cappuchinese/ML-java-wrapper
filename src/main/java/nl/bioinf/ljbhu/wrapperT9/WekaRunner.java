package nl.bioinf.ljbhu.wrapperT9;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class WekaRunner {
    private AbstractClassifier benignModel;
    private AbstractClassifier controlModel;
    final String benignFile = "/benign.model";
    final String controlFile = "/control.model";

    public WekaRunner(String inputFile) {
        try {
            Instances newInstances = loadFile(inputFile);
            loadModel();
            System.out.println("\nUnclassified input instances = \n" + newInstances);
            classifyInput(benignModel, newInstances);
            classifyInput(controlModel, newInstances);
        } catch (Exception e) {
            e.printStackTrace();
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
        this.benignModel = (AbstractClassifier) weka.core.SerializationHelper.read(benignFile);
        this.controlModel = (AbstractClassifier) weka.core.SerializationHelper.read(controlFile);
    }

    private Instances loadFile(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            throw new IOException("Could not read file");
        }
    }
}
