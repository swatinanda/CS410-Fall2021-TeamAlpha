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
#### Python 3.9
#### JDK 8
#### Neo4J
#### SpringBoot

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
##### Prerequisites
##### Python 3.9
##### JDK 8
##### Neo4J
##### SpringBoot

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

