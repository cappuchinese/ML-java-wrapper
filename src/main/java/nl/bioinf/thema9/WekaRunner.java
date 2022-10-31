package nl.bioinf.thema9;

import weka.classifier.functions.SimpleLogistics;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class WekaRunner {
    public static void main(String[] args) {
        WekaRunner runner = new WekaRunner();
        runner.start();
    }

    private Instances loadFile(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            return source.getDataSet();
        } catch (Exception e) {
            throw new IOException("Could not read file");
        }
    }
}
