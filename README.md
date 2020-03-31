# conComplaints

## 1. Purpose of the project<br/>
   Generate a report.csv from complaints.csv where records complaints filed against companies. For example, payment problem with 
   credit card or debit card tactics. This project will show the number of complaints file against companies and how they distribute
   between companies. <br/>
   <br/>
   
## 2. Chanllenge<br/>
a. The first letter in dataset is capitalized, therefore they should be converted into lower case.<br/>
b. There are cases where delimiters appear in double quote. For example, a,"abc,def",c should be interpreted as "a","abc,def","c".<br/>
c. In complaints.csv there is a case where a new line starts before it is supposed to start. For example: <br/>
2019-01-01,<br/>
debt collection.<br/>
d. Date received is in format of yyyy-MM-dd, while only yyyy is needed.<br/>
<br/>

## 3. My approach
In my approach, The process is divided to two stages: data preprocessing and analysis, implemented in Preprocessor.java and Analyzer.java respectively.<br/>
<br/>
### Preprocessor.java: <br/>
The preprocessor class takes complaints.csv as a input and generate an intermediate csv file: preprocessed.csv. In the preprocessing stage, product names are converted to lower case. The delimiter problem is also solved in this stage by tracking double quote and joining. In this stage I also corrected excess new lines by checking if the character at the starting index is a date or not. Finally, preprocessed.csv, which consists of year, product and company, is generated.<br/>
<br/>
### Analyzer.java<br/>
The analyzer class takes preprocessed.csv as input for data analysis. For each product and year, in order to calculate total complaints, number companies and highest percentage of complaints filed against one company, I used a HashMap to store those values and iterate through the map to count total and update maximum number of complaints. <br/>
<br/>

## 4. Performamce
### Space Complexity
The file is extracted into three columns as preprocessed.csv, which is around 1/8 of raw data, which consists large amount of text information like complaints. Preprocessed information is stored in Hashmap where key is product- year and value is another hashmap where counts number of complaints for each company. Therefore, it takes O(number of product*number of years*companies for that product and year).<br/>

### Time Complexity
The most expensive operation should be report generating function. The process iterates through the Hashmap to update total complaints and maximum complaints against one company, which is O(number of product*number of years*companies for that product and year). <br/>

## 5. My Comment
Thanks Insight Data Science for giving me this opportunity to show my coding skills through this project. it gives me a nice weekend and gives me a relief from quarantine due to COVID-19.


