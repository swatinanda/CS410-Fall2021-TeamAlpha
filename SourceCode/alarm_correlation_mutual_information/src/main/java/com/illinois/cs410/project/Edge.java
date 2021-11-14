package com.illinois.cs410.project;

public class Edge {
    int node1;
    int node2;
    double mutualInformation;

    public Edge(int node1, int node2, double mutualInformation) {
        this.node1 = node1;
        this.node2 = node2;
        this.mutualInformation = mutualInformation;
    }
}
