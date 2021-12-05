package com.illinois.cs410.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DemoScenario {

    List<Incident> incidents = new ArrayList<Incident>();
    int intervals;
    static int totalTemplates;
    int topCorrelatedTemplates = 10;
    public static void main(String[] args) throws IOException {
        DemoScenario demoScenarios = new DemoScenario();
        demoScenarios.ask();
        MatrixGenerator generator = new MatrixGenerator(demoScenarios.getIncidents(), demoScenarios.getIntervals(), demoScenarios.getTotalTemplates());
        LinkedHashMap<Integer, BitSet> templates = generator.doDataProcessing();
        DataProcessor processor = new DataProcessor(templates, demoScenarios.getIntervals());
        processor.dumpDataToCsv(demoScenarios.getIncidents());
        processor.computerMutualInformation();
        double[][] mi = processor.getMutualInfomation();
        writeFile(mi);


    }

    private static void writeFile(double[][] mi) throws IOException {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < mi.length; i++)//for each row
        {
            for(int j = 0; j < mi.length; j++)//for each column
            {
                builder.append(mi[i][j]+"");//append to the output string
                if(j < mi.length - 1)//if this is not the last row element
                    builder.append(",");//then add comma (if you don't like commas you can use spaces)
            }
            builder.append("\n");//append new line at the end of the row
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("mutual_infomation.txt"));
        writer.write(builder.toString());//save the string representation of the board
        writer.close();
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }

    public int getTotalTemplates() {
        return totalTemplates;
    }

    public void setTotalTemplates(int totalTemplates) {
        this.totalTemplates = totalTemplates;
    }

    private void ask() {
        Scanner scan = new Scanner(System.in);
        System.out.format("Enter the number of dummy incidents to create. (Default: 3) -> ");
        String s = scan.nextLine();
        int totalScenarios = getInputValue(s, 3);
        int startId=0;
        for(int i=0; i<totalScenarios; i++)
        {
            System.out.format("Enter the number of correlated Alarm templates for Incident #%d (Default: 5) -> ",(i+1));
            s = scan.nextLine();
            Incident incident = new Incident(i+1, getInputValue(s, 5));
            incidents.add(incident);
            startId+=incident.getTemplates();
        }
        System.out.format("Enter the number of total number of noise templates (Default: 200) ->");
        s = scan.nextLine();
        Incident incident = new Incident(-1, getInputValue(s, 200));
        incidents.add(incident);
        incident.updateRandomNumber();
        System.out.format("Enter the number of intervals for the entire run (Max: 288) ->");
        s = scan.nextLine();
        intervals = getInputValue(s, 288);
        if(intervals >=288)
            intervals = 287;
        printSummary();

    }

    private void printSummary() {
        System.out.println("####################################");
        System.out.format("For this demo scenarios, we have %d incidents. \n",this.incidents.size());
        for(int i=0; i<incidents.size(); i++)
        {
            Incident incident = incidents.get(i);
            System.out.format(" Incident #%d has %d templates\n",incident.getId(), incident.getTemplates());
            this.totalTemplates += incident.getTemplates();
        }
        System.out.format("Total Templates = %d\n",totalTemplates);
        System.out.format("Total Intervals = %d\n",intervals);
        System.out.println("Summary of Input data :");
        Incident noiseIncident = incidents.get(incidents.size() -1);
        for(int i=0; i<incidents.size() -1; i++)
        {
            Incident incident = incidents.get(i);
            System.out.format(" For Interval #%d,  %d out of %d templates will be enabled randomly , along with %d out of %d noise templates will be generated randomly\n",
                (i+1), incident.getRandomCount(), incident.getTemplates(), noiseIncident.getRandomCount(), noiseIncident.getTemplates());
        }
        System.out.format("This will repeat %d times \n", intervals/(incidents.size()-1));
        System.out.println("####################################");

    }



    private int getInputValue(String s, int defaultValue) {
        try {
            if (s.isEmpty())
                return defaultValue;
            else
                return Integer.parseInt(s);
        } catch (Exception e)
        {
            System.out.println("Invalid Input, try again !!!!");
            System.exit(0);
        }
        return -1;
    }
}
