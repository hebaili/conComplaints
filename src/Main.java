import java.io.IOException;

public class Main {

    static String input = "../input/complaints.csv";

    public static void main(String[] args) throws IOException {
        Preprocessor preprocessor = new Preprocessor();
        preprocessor.preprocess(input);
        Analyzer analyzer = new Analyzer();
        analyzer.generateReport("preprocessed.csv");
    }
}
