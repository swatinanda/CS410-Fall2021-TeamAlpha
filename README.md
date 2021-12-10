# CS 410 - Project: Designing an Alert Correlation Engine using Mutual Information and Knowledge graph

### Team: Alpha

## Authors
| Name                | NetId                 |
| ------------------- | --------------------- |
| Abhijit Bhadra         | abhadra2@illinois.edu |
| Swati Nanda         | swatin2@illinois.edu |
| Sanjeev Kumar         | sanjeev5@illinois.edu |

## Overview

The modern scaled digital transformation has made it difficult for humans to keep up with the ephemeral state of IT workloads and processes although most of them have made significant investment on monitoring the application, infrastructure, and network. In case of any outage, these monitoring systems generate different alerts, but they do not generate them at the same time and not in sequence. It takes time for IT operations to find out the relation between these alerts and they end up creating too many tickets for different teams.
Application monitoring is the process of collecting different performance metrics and log data to help developers track availability, bugs, resource use, and changes to performance in applications that affect the end-user experience. Network monitoring provides the information that network administrators need to determine, in real time, whether a network is running optimally. Infrastructure monitoring provides visibility on all the assets that are necessary to deliver and support IT services: data centres, servers, storage, and other equipment. IT operations perform their day-to-day task and mitigate error scenarios via multiple alerts generated from these monitoring systems. It empowers the users- SRE’s, L1/L2, developers, who rely on these alerts to ensure health and well-being of the services and environment. Sometimes these alerts become overwhelming and create unwanted noise. Reading through each alert and analysing to get to the error pattern and identifying actionable alerts is a daunting task resulting in lengthy troubleshooting and remediation cycles and high mean time to resolution (MTTR).
In this project, we are correlating these alerts by collecting, analysing, and building a knowledge graph between them so that it can predict and group future similar events that may affect availability and performance. We also improve the performance and reliability of the model through relevant feedback channels. This helps in identifying the symptoms and predicting probable problems upfront so that corrective actions can be taken to prevent system snags. By effectively identifying the correlated alerts, we are able to reduce the alert fatigue and work on the most urgent problem first, with reduced MTTRs.

## Design Details

The underlying algorithm uses Shanon's Mutual Information as a similarity measure and explots its grouping property.
For more details, please refer to () document.

## The Software Components

Please see the section 'Component Details' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for the details of the software  implementation.

## The Software Usage Tutorial Presentation

Please watch the [Project Presentation](https://mediaspace.illinois.edu/media/t/1_381l1bdt) for the software usage tutorial.

## Software  Installation

Please see the section 'Software Installation' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for the details of the software  implementation.

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

1. Go to https://github.com/swatinanda/CS410-Fall2021-TeamAlpha .
2. Click 'Code' button.
3. Choose 'Download ZIP'.

NOTE: Alternatively, you can clone the project repository, if you prefer to use GIT.

```bash
git clone https://github.com/swatinanda/CS410-Fall2021-TeamAlpha.git
```

#### 2 Setting Up the Local Environment
Please see the section 'Source Code Information' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for more details.

#### 3 Ingesting different kinds of Alerts

Please see the section 'Ingesting different kinds of Alerts' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for more details.

#### 4 Alert Preprocessing and Alert Correlation Analysis
Please see the section 'Alert Pre-processing and Alert Correlation Analysis' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for more details.

#### 5 Building a querying layer to pull information from knowledge graph
Please see the section 'Building a querying layer to pull information from knowledge graph' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for more details.

#### 6 Building a Chat client that will interact with the Knowledge graph
Please see the section 'Building a Chat client' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for the details of the software  implementation.

#### 7 Various APIs exposed to Chat client
Please see the section 'Various REST APIs exposed to Chat client' in the [Team_Alpha_Final_Project_Report.pdf](/Team_Alpha_Final_Project_Report.pdf) for the details of the software  implementation.
