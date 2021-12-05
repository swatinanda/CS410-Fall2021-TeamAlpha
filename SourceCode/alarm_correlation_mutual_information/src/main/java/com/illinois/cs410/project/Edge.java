package com.illinois.cs410.project;

public class Edge {
    int source;
    int target;
    double information;

    public Edge(int node1, int node2, double v) {
        this.source = node1;
        this.target = node2;
        this.information = v;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getInformation() {
        return information;
    }

    public void setInformation(double information) {
        this.information = information;
    }
}
