import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Preprocessor {
    /*
    Preprocess raw data
    Extracting useful columns: Date received, Product, Company

    @param: raw data

    @output: processed date received, product, Company
     */

    public Preprocessor() {
    }

    public void preprocess(String inputFile) throws IOException {
        /*
        preprocess data by extracting Date received, Product, Company
        steps: Read CSV
         */
        String csvFilePath = inputFile;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        FileWriter writer = new FileWriter("preprocessed.csv");
        BufferedWriter bwr = new BufferedWriter(writer);

        ArrayList<String> complaint = new ArrayList<>();
        br = new BufferedReader(new FileReader(csvFilePath));
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            Use comma as seperator
            Deal with cases like
            "2019-01-01, product A,
            sub product of A"
             */
            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(line.split(cvsSplitBy)));
            if (temp.size() == 0) continue;
            if (date.isValidDate(temp.get(0))) {
                complaint = temp;
                if (temp.size() < 18) continue;
            } else {
                complaint.addAll(temp);
                if (complaint.size() < 18) continue;
            }

            // deal with cases like "abc, def" (delimiter inside double quote)
            int startidx = -1;
            for (int i = 0; i < complaint.size(); i++) {
                if (!complaint.get(i).isEmpty() && complaint.get(i).charAt(0) == '"') {
                    startidx = i;
                }
                if (!complaint.get(i).isEmpty() && complaint.get(i).charAt(complaint.get(i).length()-1) == '"' && startidx > -1) {
                    String joinComplaint = complaint.subList(startidx, i+1).stream().collect(Collectors.joining(","));
                    complaint.subList(startidx, i+1).clear();
                    complaint.add(startidx, joinComplaint);
                    i = startidx;
                    startidx = -1;
                }
            }

            /*
            Extract Year, Product and Company
            Add " at the beginning and end of product name when there is comma present
            According to the given test case, product name should be in lower case
             */
            String[] currData = { complaint.get(0).substring(0,4), complaint.get(1).replaceAll("^\"|\"$", ""), complaint.get(7).replaceAll("^\"|\"$", "")};
            if (currData[1].indexOf(",") != -1 && currData[1].charAt(0) != '"' && currData[1].charAt(currData[1].length() - 1) != '"') {
                currData[1] = "\"" + currData[1] + "\"";
            }
            currData[1] = currData[1].toLowerCase();

            // write processed data into csv file
            for (int i = 0; i < 3; i++) {
                bwr.write(currData[i]);
                if (i < 2) {
                    bwr.write(",");
                }
            }
            bwr.newLine();
        }
        bwr.close();
    }
}

class date {
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}