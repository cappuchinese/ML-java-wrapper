package nl.bioinf.ljbhu.wrapperT9;

import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class WekaRunner {
    public static void main(String[] args) {
        WekaRunner runner = new WekaRunner();
        runner.start(args);
    }

    private void start(String[] args) {
        String modelFile = "";
        String fileInput = args[0];
        try {
            Instances newInstances = loadFile(fileInput);
            CostSensitiveClassifier fromModel = loadModel(modelFile);
            System.out.println("\nUnclassified input instances = \n" + newInstances);
            classifyInput(fromModel, newInstances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void classifyInput(CostSensitiveClassifier model, Instances input) throws Exception {
        Instances labeled = new Instances(input);
        for (int i = 0; i < input.numInstances(); i++) {
            double clsLabel = model.classifyInstance(input.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
    }

    private CostSensitiveClassifier loadModel(String modelFile) throws Exception {
        return (CostSensitiveClassifier) weka.core.SerializationHelper.read(modelFile);
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
