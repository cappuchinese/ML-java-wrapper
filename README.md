# Wrapper application ML model
Wrapper application for the pancreatic cancer prediction model

## Usage
```
usage: java -jar Theme09-ML-Application-0.2.0.jar [-h] -i <file-name> [-o <file-name>]
 -h,--help                 Print this help window
 -i,--input <file-name>    .arff type file with unclassified data
 -o,--output <file-name>   name of the output file to save the classified data
```

## Project structure
<pre><font color="#12488B"><b>.</b></font>
├── build.gradle &#9632 Gradle build file
├── <font color="#12488B"><b>data</b></font> &#9632 Contains a test data to test the application with
│   └── test.arff &#9632 ARFF formatted file with instances to test with
├── settings.gradle &#9632 Gradle settings file
└── <font color="#12488B"><b>src</b></font>
    └── <font color="#12488B"><b>main</b></font>
        ├── <font color="#12488B"><b>java</b></font>
        │   └── <font color="#12488B"><b>nl</b></font>
        │       └── <font color="#12488B"><b>bioinf</b></font>
        │           └── <font color="#12488B"><b>ljbhu</b></font>
        │               └── <font color="#12488B"><b>WrapperT9</b></font>
        │                   ├── ApacheCliOptionsProvider.java &#9632 Class for parsing command line arguments
        │                   ├── WekaRunner.java &#9632 Class to run Weka methods, classifying the unknown instances
        │                   └── WrapperMain.java &#9632 Class to run classes above
        └── <font color="#12488B"><b>resources</b></font> &#9632 Folder with the models used for the application
            ├── benign.model
            └── control.model
</pre>