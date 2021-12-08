# CS 410 - Project: Designing an Alert Correlation Engine using Mutual Information and Knowledge graph

## Overview

The modern scaled digital transformation has made it difficult for the humans to keep up with the ephemeral state of IT workloads and processes although most of them has made significant investment on monitoring the application, infrastructure, and network. In case of any outage, these monitoring systems generate different alerts, but they do not generate them at the same time and not in sequence. It takes time for IT operations to find out the relation between these alerts and they end up creating too many tickets for different teams. We intend to correlate these alerts by collecting, analysing, and building a knowledge graph between them so that it can predict and group the future similar events that may affect availability and performance. We also want to improve the performance of the model through relevant feedback channel. 
The performance of the model can be tested by referring the section: [The Software Usage and Testing the Software](#the-software-usage-and-testing-the-software) without installing anything on your machine, which I suspect most people prefer. Additionally, I introduce [The Software Development and Installation](#the-software-development-and-installation) in case you would like to setup the same development environment of this project to run the code.

## Author

### Team: Alpha

| Name                | NetId                 |
| ------------------- | --------------------- |
| Abhijit Bhadra         | abhadra2@illinois.edu |
| Swati Nanda         | swatin2@illinois.edu |
| Sanjeev Kumar         | sanjeev5@illinois.edu |


## Introduction

Application monitoring is the process of collecting different performance metrics and log data to help developers track availability, bugs, resource use, and changes to performance in applications that affect the end-user experience. Network monitoring provides the information that network administrators need to determine, in real time, whether a network is running optimally. Infrastructure monitoring provides visibility on all the assets that are necessary to deliver and support IT services: data centres, servers, storage, and other equipment. IT operation performs their day-to-day task and mitigate error scenarios via multiple alerts generated from these monitoring systems. It empowers the users- SRE’s, L1/L2, developers, who rely on these alerts to ensure health and well-being of the services and environment. Sometimes these alerts become overwhelming and create unwanted noise. Reading through each alert and analysing to get to the error pattern and identifying actionable alerts is a daunting task resulting in lengthy troubleshooting and remediation cycles and high mean time to resolution (MTTR). 
In this project we are trying to analyse the alerts and associate them so that we have a model which by identifying the symptoms can predict probable problems upfront so that corrective actions can be taken to prevent system snags. By effectively identifying the correlated alerts, we could reduce the alert fatigue and work on most urgent problem first, with reduced MTTRs.

## Design Details

The underlying algorithm uses Shanon's Mutual Information as a similarity measure and explots its grouping property.
For more details, please refer to () document.

## The Software Usage and Testing the Software

Please go to the () document and see the section 'How to Use/Test the Software' in the [ProjectReport.pdf](/ProjectReport.pdf) for the details of the software usage and testing the software.

## The Software Implementation

Please see the section 'How the Software Implemented' in the [ProjectReport.pdf](/ProjectReport.pdf) for the details of the software  implementation.

## The Software Usage Tutorial Presentation

Please watch the [Project Presentaion](https://mediaspace.illinois.edu/media/t/1_7h9807wh) for the software usage tutorial.

## The Software  Installation

The rest of the entire section below is extra detailed documentation for how to  install the software 
### Prerequisites

### Prepare the DataSet and calculate Mutual Information 
 - Download the zips from distribution folder 
 - Unzip **alert-correlation.zip** 
 - Run the following command from the extracted zip 
 ```
 java -jar alert-correlation.jar
 ```
 This is the sample output 
 ```
 % java -jar alert-correlation.jar
Enter the number of dummy incidents to create. (Default: 3) -> 4
Enter the number of correlated Alarm templates for Incident #1 (Default: 5) -> 6
Enter the number of correlated Alarm templates for Incident #2 (Default: 5) -> 7
Enter the number of correlated Alarm templates for Incident #3 (Default: 5) -> 9
Enter the number of correlated Alarm templates for Incident #4 (Default: 5) -> 4

Enter the number of total number of noise templates (Default: 200) ->500
Enter the number of intervals for the entire run (Max: 288) ->

####################################

For this demo scenarios, we have 5 incidents.
Incident #1 has 6 templates
Incident #2 has 7 templates
Incident #3 has 9 templates
Incident #4 has 4 templates
Incident #-1 has 500 templates

Total Templates = 526
Total Intervals = 287
Summary of Input data :

For Interval #1,  5 out of 6 templates will be enabled randomly , along with 8 out of 500 noise templates will be generated randomly
For Interval #2,  6 out of 7 templates will be enabled randomly , along with 8 out of 500 noise templates will be generated randomly
For Interval #3,  7 out of 9 templates will be enabled randomly , along with 8 out of 500 noise templates will be generated randomly
For Interval #4,  3 out of 4 templates will be enabled randomly , along with 8 out of 500 noise templates will be generated randomly

This will repeat 71 times

####################################

Complete Updating Mutual Information ... Took 261 ms
 ```

 - This will create the following files 
 
 **mutual_infomation.txt** : This contains the mutual information for each and every Alarm which has been generated based on the input provided 
 
 **output/*.csv** : This contains the metadata of knowledge graph in CSV format . This raw data will be used to populate Knowledge graph in neo4j database 

### Setup a Neo4j database 

 - On a Unix setup where docker is installed, create a directory named **csv** and place all the csv files generated on previous steps to this directory 
 - Spin off a Neo4j docker container 
```
docker run -d -e NEO4J_AUTH=none -p 7474:7474 -v $PWD/csv:/var/lib/neo4j/import -p 7687:7687 neo4j:4.3
```
 
 - Make sure you could able to access Neo4j browser 
http://\<hostname>:7474/browser/

 - Run the following Cypher script to load the data 
```
//id,incidentId,message  
LOAD CSV WITH HEADERS FROM "file:///Alerts.csv" AS row CREATE (:Alert {id:row.id, incidentId:row.incidentId, name:row.message, source:row.source, ci:row.ci, service:row.service});  
// id,name  
LOAD CSV WITH HEADERS FROM "file:///CIs.csv" AS row CREATE (:CI {id:row.id, name:row.name});  
// id,intervalPeriod,startTime,endTime,date  
LOAD CSV WITH HEADERS FROM "file:///Intervals.csv" AS row CREATE (:Interval {id:row.id, intervalPeriod:row.intervalPeriod, startTime:row.startTime, endTime:row.endTime, date:row.date});  
// id,name  
LOAD CSV WITH HEADERS FROM "file:///Services.csv" AS row CREATE (:Service {id:row.id, name:row.name});  
// id,name  
LOAD CSV WITH HEADERS FROM "file:///Sources.csv" AS row CREATE (:Source {id:row.id, name:row.name});  
// from,to  
LOAD CSV WITH HEADERS FROM "file:///Source-CI-MONITORS.csv" AS row MATCH (s1:Source {id:row.from}), (c1:CI {id:row.to}) CREATE (s1)-[:MONITORS{source: row.source, ci:row.target}]->(c1);  
// from,to  
LOAD CSV WITH HEADERS FROM "file:///Alert_Time-GENERATED_AT.csv" AS row MATCH (a1:Alert {id:row.from}), (t1:Interval {id:row.to}) CREATE (a1)-[:GENERATED_AT{alert: row.source, interval:row.target}]->(t1);  
// from,to  
LOAD CSV WITH HEADERS FROM "file:///CI-Alert-RAISED.csv" AS row MATCH (c1:CI {id:row.from}), (a1:Alert {id:row.to}) CREATE (c1)-[:RAISED{ci: row.source, alert:row.target}]->(a1);  
// from,to  
LOAD CSV WITH HEADERS FROM "file:///CI-Time-ALERT_RAISED_AT.csv" AS row MATCH (c1:CI {id:row.from}), (t1:Interval {id:row.to}) CREATE (c1)-[:ALERT_RAISED_AT{ci: row.source, interval:row.target}]->(t1);  
// from,to  
LOAD CSV WITH HEADERS FROM "file:///Service-CI-CONTAINS.csv" AS row MATCH (s1:Service {id:row.from}), (c1:CI {id:row.to}) CREATE (s1)-[:CONTAINS{service: row.source, ci:row.target}]->(c1);  
// from,to,mi  
LOAD CSV WITH HEADERS FROM "file:///MutualInformation.csv" AS row MATCH (a1:Alert {id:row.from}), (a2:Alert {id:row.to}) CREATE (a1)-[:CORRELATED_AT {mutual_information:row.mi}]->(a2);
```

### Querying Knowledge Graph and Extract data  

 - Ensure Neo4j instance is running and set up with user and database
 - Download **Neo4jAlerts.zip** in the Artifacts directory and unzip it.

Update your Neo4j instance details in application.properties: 

**org.neo4j.driver.uri**=bolt://\<neo4jsystem\>:7687

**org.neo4j.driver.authentication.username**=\<username> 

**org.neo4j.driver.authentication.password**=\<password>

 - Execute the service as follows on the terminal
```
java -jar demo-0.0.1-SNAPSHOT.jar com.example.demo.Neo4jAlertsApplication -a application.properties
```
 - The chat client will appear on the terminal. Through the guided instruction we can navigate the system to get additional info about alerts
 - The chat client outputs the cypher query and that can be directly used. It also executes the queries automatically on Neo4j instance via Neo4j client.
 - The REST APIs can be executed on http://\<locaalhost>:8080/<path> end points as illustrated in this section



### Project Steps

This project has the following different steps:

1. Downloading the Project Source Code
2. Setting Up the Local Environment
3. Ingesting different kinds of Alerts 
4. Alert Preprocessing and Alert Correlation Analysis
5. Building Knowledge graph for the correlated Alerts
6. Building a querying layer to pull information from knowledge graph
7. Building a Chat client that will interact with the Knowledge graph

#### 1 Downloading the Project Source Code

Please follow the steps below to download the project zip file.

1. Go to https://github.com/masamip2/CourseProject .
2. Click 'Code' button.
3. Choose 'Download ZIP'.

NOTE: Alternatively, you can clone the project repository, if you prefer to use GIT.

```bash
git clone https://github.com/masamip2/CourseProject.git
```

#### 2 Setting Up the Local Environment


#### 3 Ingesting different kinds of Alerts

Please see the section 'Ingesting different kinds of Alerts' in the [ProjectReport.pdf](/ProjectReport.pdf) for more details.

#### 4 Alert Preprocessing and Alert Correlation Analysis
Please see the section 'Alert Preprocessing and Alert Correlation Analysis' in the [ProjectReport.pdf](/ProjectReport.pdf) for more details.

#### 5 Building Knowledge graph for the correlated Alerts
Please see the section 'Building Knowledge graph for the correlated Alerts' in the [ProjectReport.pdf](/ProjectReport.pdf) for more details.

#### 6 Building a querying layer to pull information from knowledge graph
Please see the section 'Building a querying layer to pull information from knowledge graph' in the [ProjectReport.pdf](/ProjectReport.pdf) for more details.

#### 7 Building a Chat client that will interact with the Knowledge graph
Please see the section 'Building a Chat client' in the [ProjectReport.pdf](/ProjectReport.pdf) for the details of the software  implementation.

#### 8 Various APIs exposed to Chat client
Please see the section 'Building a Chat client' in the [ProjectReport.pdf](/ProjectReport.pdf) for the details of the software  implementation.

#### 9 Running the Neo4jAlerts service
Please see the section 'Running the Neo4jAlerts service' in the [ProjectReport.pdf](/ProjectReport.pdf) for the details.

