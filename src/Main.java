import java.io.File;
import java.io.IOException;
//import src.Preprocessor.
//import .Analyzer.Analyzer;




public class Main {


    //static String input = "complaints2.csv";
    //static String preprocessed;


    public static void main(String[] args) throws IOException {
	// write your code here
        for (String argument : args) {
            // do something with the argument
            System.out.println(argument);
        }
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.preprocess(args[0]);
        Analyzer analyzer = new Analyzer();
//        preprocessed = preprocessor.output_file.getAbsolutePath();
        analyzer.generateReport("preprocessed.csv");


    }
}
