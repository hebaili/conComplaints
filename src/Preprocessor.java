import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public class Preprocessor {
    //public File output_file = null;
    /*
    Preprocess raw data
    Extracting useful columns: Date received, Product, Company

    @param: raw data

    @output: processed date received, product, Company

     */

    //File output_file;



    public Preprocessor() {
       // output_file = null;
    }

    public void preprocess(String inputFile) throws IOException {
        /*
        preprocess data by extracting Date received, Product, Company
        steps: Read CSV
         */
        // Read CSV
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


            // use comma as separator
            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(line.split(cvsSplitBy)));
            if (temp.size() == 0) continue;
            if (date.isValidDate(temp.get(0))) {
                complaint = temp;
                if (temp.size() < 18) continue;
            } else {
                complaint.addAll(temp);
                if (complaint.size() < 18) continue;
            }



            // skip the first line
            //if (complaint.get(0) == "Date Received") continue;

            int startidx = -1;
            for (int i = 0; i < complaint.size(); i++) {
                if (!complaint.get(i).isEmpty() && complaint.get(i).charAt(0) == '"') {
                    startidx = i;
                    //System.out.println("start");
                }
                if (!complaint.get(i).isEmpty() && complaint.get(i).charAt(complaint.get(i).length()-1) == '"' && startidx > -1) {
                    String joinComplaint = complaint.subList(startidx, i+1).stream().collect(Collectors.joining(","));
                    complaint.subList(startidx, i+1).clear();
                    complaint.add(startidx, joinComplaint);
                    i = startidx;
                    startidx = -1;
                }
            }

            if (complaint.size() <=8) {
                for (int i = 0; i < complaint.size(); i++) {
                    System.out.println(i);
                    System.out.println(complaint.get(i));
                }
                System.out.println("Finished");
                System.out.println(line);
            }
            String[] currData = { complaint.get(0).substring(0,4), complaint.get(1).replaceAll("^\"|\"$", ""), complaint.get(7).replaceAll("^\"|\"$", "")};
            // skip the first line
            //if (currData[0] == "Date") continue;

            // Processes uncleaned data in currData[2], which is product
            //String product = currData[1];
            // Check if the product is supposed to be a sub product

            //currData[1] = processProduct(product);
            if (currData[1].indexOf(",") != -1 && currData[1].charAt(0) != '"' && currData[1].charAt(currData[1].length() - 1) != '"') {
                currData[1] = "\"" + currData[1] + "\"";
            }


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