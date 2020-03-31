import java.io.File;
import java.io.IOException;
//import src.Preprocessor.
//import .Analyzer.Analyzer;




public class Main {


    static String input = "../input/complaints.csv";


    public static void main(String[] args) throws IOException {

        Preprocessor preprocessor = new Preprocessor();
        preprocessor.preprocess(input);
        Analyzer analyzer = new Analyzer();
        analyzer.generateReport("preprocessed.csv");


    }
}
