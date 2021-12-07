MATCH (n) DETACH DELETE n;
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

// To delete everything
// MATCH (n) DETACH DELETE n

// To start a docker container
// docker run -d -e NEO4J_AUTH=none -p 7474:7474 -v $PWD/csv:/var/lib/neo4j/import -p 7687:7687 neo4j:4.3