package com.illinois.cs410.project;
import java.util.List;

public class Graph {
    List<AlarmTemplates> alarms;
    List<Edge> edgeList;

    public Graph(List<AlarmTemplates> alarms, List<Edge> edgeList) {
        this.alarms = alarms;
        this.edgeList = edgeList;
    }
}
