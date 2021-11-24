package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
        if(vertex.getHost() != null)
            values.put("host", vertex.getHost());
        if(vertex.getMessage() != null)
            values.put("message", vertex.getMessage());
        if(vertex.getService() != null)
            values.put("service", vertex.getService());
        if(vertex.getDevice() != null)
            values.put("device", vertex.getDevice());
       if(edge.getMutual_information() != null)
            values.put("mutual_information", edge.getMutual_information());

        return values;
    }
}
