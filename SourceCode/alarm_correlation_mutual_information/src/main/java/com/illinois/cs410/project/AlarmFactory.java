package com.illinois.cs410.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlarmFactory {
    private int number;
    private List<AlarmTemplates> alarms;

    public List<AlarmTemplates> getAlarms() {
        return alarms;
    }

    Set<String> messages;
    Set<String> host;
    Set<String> source;
    String dirName = "data/";
    public AlarmFactory(int noOfAlarm) {
        this.number = noOfAlarm;
        this.alarms = new ArrayList<>();
        this.host = new HashSet<>();
        this.messages = new HashSet<>();
        this.source = new HashSet<>();
        readHostCorpus("host_corpus.txt");
        readMessageCorpus("alarm_message_corpus.txt");


    }

    private void readHostCorpus(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dirName+fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                this.host.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessageCorpus(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dirName+fileName))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                // process the line.
                String[] elements = line.split(";");
                if(elements.length <2)
                    System.out.println();
                this.source.add(elements[0]);
                this.messages.add(elements[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createAlarms()
    {
        int hostSize = this.host.size();
        int messageSize = this.messages.size();
        int sourceSize = this.source.size();
        for(int i=1; i<=number; i++)
        {
            this.alarms.add(new AlarmTemplates(i,
                    getRandom(messageSize, this.messages),
                    getRandom(hostSize, this.host),
                    getRandom(sourceSize, this.source)));
        }
    }

    private String getRandom(int count, Set<String> messages) {
        List<String> m = new ArrayList<>(messages);
        return m.get(getRandomNumber(count));
    }
    private int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)));
    }

    public List<Edge> getEdges(double[][] mi) {
        List<Edge> edges = new ArrayList<>();
        for (int node1=0; node1<mi.length; node1++) {
            for (int node2 = node1+1; node2 < mi.length; node2++) {
                edges.add(new Edge(node1, node2, mi[node1][node2]));
            }
        }
        return edges;
    }
}
