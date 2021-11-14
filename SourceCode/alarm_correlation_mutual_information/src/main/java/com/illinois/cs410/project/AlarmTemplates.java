package com.illinois.cs410.project;

public class AlarmTemplates {
    int id;
    String message;
    String host;
    String source;

    public AlarmTemplates(int id, String message, String host, String source) {
        this.id = id;
        this.message = message;
        this.host = host;
        this.source = source;
    }
}
