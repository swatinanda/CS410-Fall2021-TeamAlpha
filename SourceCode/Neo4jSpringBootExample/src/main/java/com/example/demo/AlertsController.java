
package com.example.demo;

 import java.util.*;
 import com.example.demo.model.AlertEdge;
 import com.example.demo.model.AlertVertex;
 import com.example.demo.model.AlertVertexRelation;
 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.fasterxml.jackson.databind.JsonNode;
 import org.neo4j.driver.*;
 import org.neo4j.driver.internal.InternalNode;
 import org.neo4j.driver.internal.InternalPath;
 import org.neo4j.driver.internal.InternalRecord;
 import org.neo4j.driver.internal.value.ListValue;
 import org.neo4j.driver.internal.value.NodeValue;
 import org.neo4j.driver.types.Relationship;
 import org.springframework.http.MediaType;
 import org.springframework.util.StringUtils;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertsController {

    private Driver driver = null;
    public AlertsController() {}
    public AlertsController(Driver driver) {
        this.driver = driver;
    }

    @GetMapping(path = "/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getAlerts(String message, String device) throws JsonProcessingException {
        final List<Map<String, Object>> response = new ArrayList<>();
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {

            session.readTransaction(new TransactionWork<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> execute(Transaction tx) {

                    String deviceClause = !StringUtils.isEmpty(device) ? String.format("toLower(n.ci)=toLower('%s')", device) : null;
                    String messageClause = !StringUtils.isEmpty(message) ? String.format("toLower(n.name) CONTAINS toLower('%s')", message) : null;
                    List<String> lstWhere = new ArrayList<>();
                    if(deviceClause != null)
                        lstWhere.add(deviceClause);
                    if(messageClause != null)
                        lstWhere.add(messageClause);
                    String whereClause = String.join(" AND ", lstWhere);

                    Query qry = new Query("MATCH (n:Alert ) WHERE  " +   whereClause + "\n" +
                         " CALL {\n" +
                         " WITH n MATCH (n)-[r:CORRELATED_AT]-(asso_alert) RETURN asso_alert,(n)-[r:CORRELATED_AT]-(asso_alert) as relation\n" +
                         " ORDER BY r.mutual_information DESC\n" +
                         " }\n" +
                         " RETURN asso_alert as vertex, relation as edge LIMIT 5");

                    System.out.println("********* Cypher Query *********");
                    System.out.println(qry.toString());
                    System.out.println("********************************");
                    Result result = tx.run(qry);
                    while (result.hasNext()) {
                        Record record = result.next();

                        InternalNode vertex = (InternalNode)(((InternalRecord) record).get(0).asNode());
                        Iterable<Relationship> edges = ((InternalPath)((ListValue)((InternalRecord) record).get(1)).asList().get(0)).relationships();
                        Relationship edge = edges.iterator().next();

                        AlertVertex av = getAlertVertex(vertex);
                        AlertEdge ae = getAlertEdge(edge);
                        AlertVertexRelation avr = new AlertVertexRelation(av, ae);
                        response.add(avr.getValues());

                    }
                    return response;
                }
            });

        }
        return response;

    }

    @GetMapping(path = "/alertsCounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAlertsCount(String start, String end) throws JsonProcessingException {
        final String[] response = {""};
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {

            session.readTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {

                   String[] startDateTime = start.split(" ");
                    String[] endDateTime = end.split(" ");

                    String startClause = " i.date >= '" + startDateTime[0] + "' AND i.startTime >= '" + startDateTime[1] + "'";
                    String endClause = "i.date <= '" + endDateTime[0] + "' AND i.endTime <= '" + endDateTime[1] + "'";
                    List<String> lstWhere = new ArrayList<>();
                    if (startClause != null)
                        lstWhere.add(startClause);
                    if (endClause != null)
                        lstWhere.add(endClause);
                    String whereClause = String.join(" AND ", lstWhere);

                    Query qry = new Query("MATCH(i:Interval)-[r]-(a:Alert) WHERE  " + whereClause + "\n" +
                            " return  count(a) as cnt ");

                    System.out.println("********* Cypher Query *********");
                    System.out.println(qry.toString());
                    System.out.println("********************************");
                    Result result = tx.run(qry);
                    while (result.hasNext()) {
                        Record record = result.next();
                        response[0] = "Alert counts between period " + start + " and " + end + " is " + record.get("cnt").asInt();

                    }
                    return response[0];
                }

            });
        }
        return response[0];
    }


    @GetMapping(path = "/affectedCIs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getAffectedCIs(String start, String end, String alert) throws JsonProcessingException {
         Set<String> response = new HashSet<>();
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {

            session.readTransaction(new TransactionWork<Set<String>>() {
                @Override
                public Set<String> execute(Transaction tx) {

                    String[] startDateTime = start.split(" ");
                    String[] endDateTime = end.split(" ");

                    String startClause = " i.date >= '" + startDateTime[0] + "' AND i.startTime >= '" + startDateTime[1] + "'";
                    String endClause = "i.date <= '" + endDateTime[0] + "' AND i.endTime <= '" + endDateTime[1] + "'";
                    List<String> lstWhere = new ArrayList<>();
                    if (startClause != null)
                        lstWhere.add(startClause);
                    if (endClause != null)
                        lstWhere.add(endClause);
                    String whereClause = String.join(" AND ", lstWhere);

                    String whereAlert = String.format(" toLower(n.name) CONTAINS toLower('%s') ", alert);


                    Query qry = new Query("MATCH (n:Alert ) WHERE  " + whereAlert + "\n" +
                            "MATCH (n)-[r:CORRELATED_AT]-(asso_alert) \n" +
                            "with n,asso_alert, r ORDER BY r.mutual_information DESC limit 5 \n" +
                            "optional MATCH (n )-[ar]-(c:CI), (c:CI)-[ci]-(i:Interval) WHERE " + whereClause + "\n" +

                            " return  c as ci ,(n )-[ar]-(c:CI) as ar , i \n" +
                            "UNION \n" +
                            "MATCH (n:Alert ) WHERE  " + whereAlert + "\n" +
                            "MATCH (n)-[r:CORRELATED_AT]-(asso_alert) \n" +
                            "with n,asso_alert, r ORDER BY r.mutual_information DESC limit 5 \n" +
                            "optional MATCH (asso_alert )-[ar]-(c:CI), (c:CI)-[ci]-(i:Interval) WHERE " + whereClause + "\n" +

                            " return  c as ci ,(asso_alert )-[ar]-(c:CI) as ar , i ");

                    System.out.println("********* Cypher Query *********");
                    System.out.println(qry.toString());
                    System.out.println("********************************");
                    Result result = tx.run(qry);
                    while (result.hasNext()) {
                        Record record = result.next();
                        //response[0] = "Alert counts between period " + start + " and " + end + " is " + record.get("cnt").asInt();
                        if(record.get("ci") != null)
                            response.add(((NodeValue)record.get("ci")).get("name").asString());
                    }
                    return response;
                }

            });
        }
        return response;
    }

    @GetMapping(path = "/affectedServices", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getAffectedServices(String start, String end, String alert) throws JsonProcessingException {
        Set<String> response = new HashSet<>();
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {

            session.readTransaction(new TransactionWork<Set<String>>() {
                @Override
                public Set<String> execute(Transaction tx) {

                    String[] startDateTime = start.split(" ");
                    String[] endDateTime = end.split(" ");

                    String startClause = " i.date >= '" + startDateTime[0] + "' AND i.startTime >= '" + startDateTime[1] + "'";
                    String endClause = "i.date <= '" + endDateTime[0] + "' AND i.endTime <= '" + endDateTime[1] + "'";
                    List<String> lstWhere = new ArrayList<>();
                    if (startClause != null)
                        lstWhere.add(startClause);
                    if (endClause != null)
                        lstWhere.add(endClause);
                    String whereClause = String.join(" AND ", lstWhere);

                    String whereAlert = String.format(" toLower(n.name) CONTAINS toLower('%s') ", alert);


                    Query qry = new Query("MATCH (n:Alert ) WHERE  " + whereAlert + "\n" +
                            "MATCH (n)-[r:CORRELATED_AT]-(asso_alert) \n" +
                            "with n,asso_alert, r ORDER BY r.mutual_information DESC limit 5 \n" +
                            "optional MATCH (n )-[ar]-(c:CI), (c:CI)-[ci]-(i:Interval), (c:CI)-[]-(s:Service) WHERE " + whereClause + "\n" +

                            " return  c as ci ,(n )-[ar]-(c:CI) as ar , i, (c:CI)-[]-(s:Service) as cis, s \n" +
                            "UNION \n" +
                            "MATCH (n:Alert ) WHERE  " + whereAlert + "\n" +
                            "MATCH (n)-[r:CORRELATED_AT]-(asso_alert) \n" +
                            "with n,asso_alert, r ORDER BY r.mutual_information DESC limit 5 \n" +
                            "optional MATCH (asso_alert )-[ar]-(c:CI), (c:CI)-[ci]-(i:Interval), (c:CI)-[]-(s:Service) WHERE " + whereClause + "\n" +

                            " return  c as ci ,(asso_alert )-[ar]-(c:CI) as ar , i, (c:CI)-[]-(s:Service) as cis, s ");

                    System.out.println("********* Cypher Query *********");
                    System.out.println(qry.toString());
                    System.out.println("********************************");
                    Result result = tx.run(qry);
                    while (result.hasNext()) {
                        Record record = result.next();
                        //response[0] = "Alert counts between period " + start + " and " + end + " is " + record.get("cnt").asInt();
                        if(record.get("s") != null)
                            response.add(((NodeValue)record.get("s")).get("name").asString());
                    }
                    return response;
                }

            });
        }
        return response;
    }

    private AlertVertex getAlertVertex(InternalNode vertex) {
        AlertVertex av = new AlertVertex();
        av.setMessage(vertex.get("name").asString());
        //av.setDevice(vertex.get("device").asString());
        //av.setHost(vertex.get("host").asString());
        //av.setService(vertex.get("service").asString());
        return av;
    }

    private AlertEdge getAlertEdge(Relationship edge) {
        AlertEdge ae = new AlertEdge();
        ae.setMutual_information(Double.parseDouble(edge.get("mutual_information").asString()));
        ae.setType(edge.get("type").toString());

        return ae;
    }
}
