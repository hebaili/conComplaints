import java.io.File;
import java.io.IOException;
//import src.Preprocessor.
//import .Analyzer.Analyzer;




public class Main {

    static String input = "/Users/bailihe/IdeaProjects/conComplaints/insight_testsuite/tests/test3/input/complaints.csv";
    //static String preprocessed;


    public static void main(String[] args) throws IOException {
	// write your code here
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.preprocess(input);
        Analyzer analyzer = new Analyzer();
//        preprocessed = preprocessor.output_file.getAbsolutePath();
        analyzer.generateReport("/Users/bailihe/IdeaProjects/conComplaints/src/preprocessed.csv");


    }
}
