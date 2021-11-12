# Alarm Correlation via Mutual Information ( POC )

- Run DemoScenario
- This is the sample output

 ```
 Enter the number of dummy incidents to create. (Default: 3) -> 4
Enter the number of correlated Alarm templates for Incident #1 (Default: 5) -> 7
Enter the number of correlated Alarm templates for Incident #2 (Default: 5) -> 8
Enter the number of correlated Alarm templates for Incident #3 (Default: 5) -> 3
Enter the number of correlated Alarm templates for Incident #4 (Default: 5) -> 5
Enter the number of total number of noise templates (Default: 200) ->300
Enter the number of intervals for the entire run (Default: 600) ->1000
####################################
For this demo scenarios, we have 5 incidents. 
 Incident #1 has 7 templates
 Incident #2 has 8 templates
 Incident #3 has 3 templates
 Incident #4 has 5 templates
 Incident #-1 has 300 templates
Total Templates = 323
Total Intervals = 1000
Summary of Input data :
 For Interval #1,  6 out of 7 templates will be enabled randomly , along with 8 out of 300 noise templates will be generated randomly
 For Interval #2,  6 out of 8 templates will be enabled randomly , along with 8 out of 300 noise templates will be generated randomly
 For Interval #3,  3 out of 3 templates will be enabled randomly , along with 8 out of 300 noise templates will be generated randomly
 For Interval #4,  4 out of 5 templates will be enabled randomly , along with 8 out of 300 noise templates will be generated randomly
This will repeat 250 times 
####################################


 ```

- This will create a matrix of mutual information and the same is dumped into mutual_information.txt
- Run **mi_display_parser.py** to visualise the information

# Output 

The Incidental templates are getting correlated 

![alt text](https://github.com/swatinanda/CS410-Fall2021-TeamAlpha/blob/main/SourceCode/alarm_correlation_mutual_information/mi.png)

