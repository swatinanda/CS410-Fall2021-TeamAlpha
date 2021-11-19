
package com.example.demo;

 import java.util.*;
 import com.example.demo.model.AlertEdge;
 import com.example.demo.model.AlertVertex;
 import com.example.demo.model.AlertVertexRelation;
 import com.fasterxml.jackson.core.JsonProcessingException;
 import org.neo4j.driver.*;
 import org.neo4j.driver.internal.InternalNode;
 import org.neo4j.driver.internal.InternalPath;
 import org.neo4j.driver.internal.InternalRecord;
 import org.neo4j.driver.internal.value.ListValue;
 import org.neo4j.driver.types.Relationship;
 import org.springframework.http.MediaType;
 import org.springframework.util.StringUtils;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertsController {

    private final Driver driver;

    public AlertsController(Driver driver) {
        this.driver = driver;
    }

    @GetMapping(path = "/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getAlerts(String message, String device) throws JsonProcessingException {
        final List<Map<String, Object>> response = new ArrayList<>();
        try (Session session = driver.session(SessionConfig.forDatabase("alertdb"))) {

            session.readTransaction(new TransactionWork<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> execute(Transaction tx) {

                    //1Query qry = new Query("MATCH p=()-->() RETURN p LIMIT 25");
                    //2Query qry = new Query("MATCH (n:Alert) RETURN n LIMIT 25");
                    String deviceClause = !StringUtils.isEmpty(device) ? String.format("toLower(n.device)=toLower('%s')", device) : null;
                    String messageClause = !StringUtils.isEmpty(device) ? String.format("toLower(n.message) CONTAINS toLower('%s')", message) : null;
                    List<String> lstWhere = new ArrayList<>();
                    if(deviceClause != null)
                        lstWhere.add(deviceClause);
                    if(messageClause != null)
                        lstWhere.add(messageClause);
                    String whereClause = String.join(" AND ", lstWhere);
                    //Query qry = new Query("MATCH (n:Alert ) WHERE " +   whereClause + "\n" +
                    //        " CALL {\n" +
                    //        "    WITH n MATCH (n)-[correlated_to]-(asso_alert)  RETURN asso_alert,(n)-[correlated_to]-(asso_alert) as relation\n" +
                    //        "    \n" +
                   //         "}\n" +
                    //        "RETURN asso_alert as vertex, relation as edge");

                    Query qry = new Query("MATCH (n:Alert ) WHERE " +   whereClause + "\n" +
                         " CALL {\n" +
                         " WITH n MATCH (n)-[r:correlated_to]-(asso_alert) RETURN asso_alert,(n)-[r:correlated_to]-(asso_alert) as relation\n" +
                         " ORDER BY r.score DESC\n" +
                         " }\n" +
                         " RETURN asso_alert as vertex, relation as edge ");

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

    private AlertVertex getAlertVertex(InternalNode vertex) {
        AlertVertex av = new AlertVertex();
        av.setMessage(vertex.get("message").asString());
        av.setDevice(vertex.get("device").asString());
        av.setHost(vertex.get("host").asString());
        av.setService(vertex.get("service").asString());
        return av;
    }

    private AlertEdge getAlertEdge(Relationship edge) {
        AlertEdge ae = new AlertEdge();
        ae.setScore(Double.parseDouble(edge.get("score").toString()));
        ae.setType(edge.get("type").toString());

        return ae;
    }
}
