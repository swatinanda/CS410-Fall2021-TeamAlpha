package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertEdge {
    String type;
    Double mutual_information;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMutual_information() {
        return mutual_information;
    }

    public void setMutual_information(Double mutual_information) {
        this.mutual_information = mutual_information;
    }
}
