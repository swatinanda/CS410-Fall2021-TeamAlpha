package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

public class AlertVertexRelation {
    public AlertVertexRelation() {

    }
    public AlertVertexRelation(AlertVertex vertex, AlertEdge edge) {
        this.vertex = vertex;
        this.edge = edge;

    }
    public AlertVertex getVertex() {
        return vertex;
    }

    public void setVertex(AlertVertex vertex) {
        this.vertex = vertex;
    }

    public AlertEdge getEdge() {
        return edge;
    }

    public void setEdge(AlertEdge edge) {
        this.edge = edge;
    }

    AlertVertex vertex;
    AlertEdge edge;


    public Map<String, Object> getValues() {
        Map<String, Object> values = new HashMap<>();
        values.put("host", vertex.getHost());
        values.put("message", vertex.getMessage());
        values.put("service", vertex.getService());
        values.put("device", vertex.getDevice());
        values.put("score", edge.getScore());

        return values;
    }
}
