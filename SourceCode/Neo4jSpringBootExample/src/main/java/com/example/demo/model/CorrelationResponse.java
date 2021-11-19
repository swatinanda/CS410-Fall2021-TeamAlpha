package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

public class CorrelationResponse {
    public Map<AlertVertex, Double> getCorrelatedAlerts() {
        return correlatedAlerts;
    }

    public void setCorrelatedAlerts(Map<AlertVertex, Double> correlatedAlerts) {
        this.correlatedAlerts = correlatedAlerts;
    }

    public void addCorrelatedVertex (AlertVertex vertex, Double score) {
        if( correlatedAlerts == null)
            correlatedAlerts = new HashMap<>();
        correlatedAlerts.put(vertex, score);
    }

    Map<AlertVertex, Double> correlatedAlerts;
}
