import java.io.*;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


public class Analyzer {
    /*
    Analyzes preprocessed data and output report

    @input: preprocessed data: year, product, company

    Calculate: total number of complaints received per product per year
               counting of companies (#complaints > 1) each year
               highest percentage (rounded to the nearest whole number) of total complaints filed against one company for that product and year

    @output: report.csv
             product, year, counting of companies each year, counting of companies(#complaints > 1), highest percentage
             ordered by product(alphabetical), year(ascending)
     */

    static File output_file;

    // Key: "Product - Year", value: HashMaps (with key = company ,value = complaints against that company)
    static Map<String, Map<String,Integer>> product_year_category_map;


    // Each List<String>:
    // Year,
    // product,
    // total number of complaints received per product per year,
    // counting of companies (#complaints > 1) each year
    // highest percentage
    List<List<String>> result;




    public Analyzer() {
        output_file = null;
        product_year_category_map = new HashMap<>();
    }

    /*
    @param: inputFile
    @output: updated product_year_category_map in order to generate report
     */
    public static void analyze(String inputFile) throws IOException {
        // Read from input file
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int iteration = 0;

        br = new BufferedReader(new FileReader(inputFile));
        while ((line = br.readLine()) != null) {
            if (iteration == 0) {
                iteration++;
                continue;
            }
            // use comma as separator
            ArrayList<String> info = new ArrayList<String>(Arrays.asList(line.split(cvsSplitBy)));


            int startIdx = -1;
            for (int i = 0; i < info.size(); i++) {
                if (!info.get(i).isEmpty() && info.get(i).charAt(0) == '"') {
                    startIdx = i;

                }
                if (!info.get(i).isEmpty() && info.get(i).charAt(info.get(i).length()-1) == '"' && startIdx > -1) {
                    String joinComplaint = info.subList(startIdx, i+1).stream().collect(Collectors.joining(","));
                    info.subList(startIdx, i+1).clear();
                    info.add(startIdx, joinComplaint);
                    i = startIdx;
                    startIdx = -1;
                }
            }


            String productYear = info.get(1) + "-" + info.get(0);
            if (!product_year_category_map.containsKey(productYear)) {
                product_year_category_map.put(productYear,new HashMap<>());
                product_year_category_map.get(productYear).put(info.get(2),1);
            } else {
                if (!product_year_category_map.get(productYear).containsKey(info.get(2))) {
                    product_year_category_map.get(productYear).put(info.get(2), 1);
                } else {
                    product_year_category_map.get(productYear).put(info.get(2), product_year_category_map.get(productYear).get(info.get(2)) + 1);
                }
            }
        }
    }

    public void generateReport(String inputFile) throws IOException {
        analyze(inputFile); // maps updated

        // Iterate through product_year_category_map
        // Put information in List<List<String>>
        result = new ArrayList<>();
        for (String productYear: product_year_category_map.keySet()) {
            String[] str = productYear.split("-");
            String product = str[0];
            String year = str[1];
            int totalComplaints = 0;
            int maxComplaintAgainstOneCompany = Integer.MIN_VALUE;
            for (String company: product_year_category_map.get(productYear).keySet()) {
                int countForThisCompany = product_year_category_map.get(productYear).get(company);
                totalComplaints += countForThisCompany;
                if (maxComplaintAgainstOneCompany < countForThisCompany) {
                    maxComplaintAgainstOneCompany = countForThisCompany;
                }
            }
            int totalCompanies = product_year_category_map.get(productYear).size();
            int percentage = calculatePercentage(Double.valueOf(maxComplaintAgainstOneCompany),Double.valueOf(totalComplaints));

            // Write information into List<String> and add to result
            List<String> curr = new ArrayList<>();
            curr.add(product);
            curr.add(year);
            curr.add(String.valueOf(totalComplaints));
            curr.add(String.valueOf(totalCompanies));
            curr.add(String.valueOf(percentage));
            result.add(curr);
        }
        Collections.sort(result,new ListComparator<>());


        // write csv
        FileWriter writer = new FileWriter("../output/report.csv");
        BufferedWriter bwr = new BufferedWriter(writer);


        for (int row = 0; row < result.size(); row++) {
            for (int col = 0; col < 5; col++) {
                bwr.write(result.get(row).get(col));
                if (col < 4) {
                    bwr.write(",");
                }
            }
            bwr.newLine();
        }
        bwr.close();

    }


    private int calculatePercentage(double maxComplaint, double totalComplaint) {
        return (int) Math.round(maxComplaint/totalComplaint*100);
    }
}


// self defined comparator to sort product in alphabetical order and year in ascending order
class ListComparator<T extends Comparable<T>> implements Comparator<List<T>> {

    @Override
    public int compare(List<T> o1, List<T> o2) {
        int c = o1.get(0).compareTo(o2.get(0));
        if (c != 0 ) {
            return c;
        }
        return o1.get(1).compareTo(o2.get(1));
    }

}
