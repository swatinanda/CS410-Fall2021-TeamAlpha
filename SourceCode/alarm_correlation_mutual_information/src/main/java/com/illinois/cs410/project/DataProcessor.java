package com.illinois.cs410.project;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {
    LinkedHashMap <Integer, BitSet> t;
    int intervals;

    public double[][] getMutualInfomation() {
        return mutualInfomation;
    }

    double[][] mutualInfomation;
    private final ThreadLocal<BitSet> tempBits;

    public DataProcessor(LinkedHashMap<Integer, BitSet> templates, int intervals) {
        this.t = templates;
        this.intervals = intervals;
        this.tempBits  = ThreadLocal.withInitial(() -> new BitSet(8640));
        this.mutualInfomation = new double[templates.size()][templates.size()];
    }
    private double getLog2(double number) {
        if (number == 0)
            return 0.0D;
        return (1.0D * Math.log(number) / Math.log(2));
    }
    public void computerMutualInformation() {
        try{
            List<Edge> edgeList = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            int size = t.size();
            PrintWriter mi = new PrintWriter("output/MutualInformation.csv");
            int[][] mo= new int[5][5];
            int sumMo = 0;
            DecimalFormat df = new DecimalFormat("#.00000");
            mi.write("from,to,mi\n");
            for(int i =0; i<size; i++)
            {
                for(int k=i+1; k<size; k++)
                {
                    //System.out.format("Calculating mutual Information between %d and %d \n", i, k);
                    BitSet tBits = tempBits.get();
                    BitSet i_bit = t.get(i);
                    BitSet k_bit = t.get(k);
                    //System.out.println("Cardinality = "+i_bit.cardinality()+"Length = "+i_bit.length()+", Size = "+i_bit.size());
                    // Perform some smoothing 0.5 for p_i_true and p_k_true & 0.25 for p_i_true_k_true
                    double p_i_true = (0.5+ (1.0D * i_bit.cardinality()))/(1+intervals);
                    double p_i_false = 1.00 - p_i_true;
                    double p_k_true = (0.5+(1.0D * k_bit.cardinality()))/(1+intervals);
                    double p_k_false = 1.00 - p_k_true;
                    tBits.or(i_bit);
                    tBits.and(k_bit);
                    int intersection = tBits.cardinality();
                    double p_i_true_k_true = (0.25 + (1.0D * intersection))/(intervals+1);
                    tBits.clear();
                    double p_i_true_k_false = p_i_true - p_i_true_k_true;
                    double p_i_false_k_true = p_k_true - p_i_true_k_true;
                    double p_i_false_k_false = 1 - p_i_true_k_true - p_i_true_k_false - p_i_false_k_true;

                    double infotmation = p_i_false_k_false * getLog2(1.0D * (p_i_false_k_false)/(p_i_false * p_k_false)) +
                            p_i_false_k_true * getLog2(1.0D * (p_i_false_k_true)/(p_i_false * p_k_true)) +
                            p_i_true_k_false * getLog2(1.0D * (p_i_true_k_false)/(p_i_true * p_k_false)) +
                            p_i_true_k_true * getLog2(1.0D * (p_i_true_k_true)/(p_i_true * p_k_true)) ;
                    //System.out.format("Calculating mutual Information between %d and %d is %f\n", i, k, infotmation);
                    this.mutualInfomation[i][k] = infotmation;
                    this.mutualInfomation[k][i] = infotmation;
                    edgeList.add(new Edge(i,k, infotmation));
                    mi.write(i+","+k+","+df.format(infotmation)+"\n");
                    // Abhijit - ToDo - POST Mutual Information between edges
                }

            }
            mi.close();
            //UpdateVertexInformation();
            //UpdateEdgeInformation(edgeList);
            System.out.println("Complete Updating Mutual Information ... Took "+(System.currentTimeMillis() - startTime));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void dumpDataToCsv(List<Incident> incidents) {
        int startId = 0;
        List<AlarmTemplates> alarmTemplatesList = new ArrayList<>();
        int incidentId = 1;
        for(Incident i : incidents)
        {
            AlarmFactory factory = new AlarmFactory();
            factory.setNumber(i.getTemplates());
            factory.createAlarms(startId);
            for(AlarmTemplates alert : factory.getAlarms())
                alert.setIncidentId(incidentId);
            alarmTemplatesList.addAll(factory.getAlarms());
            startId +=i.getTemplates();
            incidentId++;
        }
        for(int i =0; i<alarmTemplatesList.size(); i++)
        {
            alarmTemplatesList.get(i).setIntervals(this.t.get(i));
        }
        writeAlertFile(alarmTemplatesList);
        writeOtherFiles(alarmTemplatesList);
        createRelationshipFiles(alarmTemplatesList);
    }

    private void createRelationshipFiles(List<AlarmTemplates> alarmTemplatesList) {
        try{
            PrintWriter sourceCi = new PrintWriter("./output/Source-CI-MONITORS.csv");
            PrintWriter serviceCi = new PrintWriter("./output/Service-CI-CONTAINS.csv");
            PrintWriter ciTime = new PrintWriter("./output/CI-Time-ALERT_RAISED_AT.csv");
            PrintWriter ciAlert = new PrintWriter("./output/CI-Alert-RAISED.csv");
            PrintWriter alertTime = new PrintWriter("./output/Alert_Time-GENERATED_AT.csv");
            sourceCi.write("from,to,source,target\n");
            serviceCi.write("from,to,source,target\n");
            ciTime.write("from,to,source,target\n");
            ciAlert.write("from,to,source,target\n");
            alertTime.write("from,to,source,target\n");
            for(AlarmTemplates t:alarmTemplatesList)
            {
                Long alarmId = t.getId();
                Integer sourceId = sourceMap.get(t.getSource());
                String source = t.getSource();
                Integer serviceId = serviceMap.get(t.getService());
                String service = t.getService();
                Integer ciId = ciMap.get(t.getHost());
                String ci = t.getHost();
                String alarm = t.toString();
                List<Interval> intervals = t.getIntervals();
                sourceCi.write(sourceId+","+ciId+","+source+","+ci+"\n");
                serviceCi.write(serviceId+","+ciId+","+service+","+ci+"\n");
                ciAlert.write(ciId+","+alarmId+","+ci+","+alarm+"\n");
                for(Interval i : intervals)
                {
                    ciTime.write(ciId+","+intervalMap.get(i)+","+ci+","+i+"\n");
                    alertTime.write(alarmId+","+intervalMap.get(i)+","+alarm+","+i+"\n");
                }
            }
            sourceCi.close();
            serviceCi.close();
            ciTime.close();
            ciAlert.close();
            alertTime.close();
        }catch(Exception e)
        {

        }
    }

    Map<String, Integer> ciMap = new HashMap<>();
    Map<String, Integer> sourceMap = new HashMap<>();
    Map<String, Integer> serviceMap = new HashMap<>();
    Map<Interval, Integer> intervalMap = new HashMap<>();

    private void writeOtherFiles(List<AlarmTemplates> alarmTemplatesList) {
        Set<String> cIs = alarmTemplatesList.stream().map(x->x.host).collect(Collectors.toSet());
        Set<String> sources = alarmTemplatesList.stream().map(x->x.source).collect(Collectors.toSet());
        Set<String> services = alarmTemplatesList.stream().map(x->x.service).collect(Collectors.toSet());
        Set<Interval> intervals = new HashSet<>();
        for(AlarmTemplates t: alarmTemplatesList)
        {
            intervals.addAll(t.intervals);
        }
        int count =0;
        try {
            PrintWriter ci = new PrintWriter("./output/CIs.csv");
            ci.write("id,name\n");
            for(String host : cIs)
            {
                ci.write(count+","+host+"\n");
                ciMap.put(host, count);
                count++;
            }
            ci.close();

            PrintWriter source = new PrintWriter("./output/Sources.csv");
            count =0;
            source.write("id,name\n");
            for(String s : sources)
            {
                source.write(count+","+s+"\n");
                sourceMap.put(s, count);
                count++;
            }
            source.close();

            PrintWriter svc = new PrintWriter("./output/Services.csv");
            count =0;
            svc.write("id,name\n");
            for(String s : services)
            {
                svc.write(count+","+s+"\n");
                serviceMap.put(s, count);
                count++;
            }
            svc.close();
            count =0;
            PrintWriter intervalWriter = new PrintWriter("./output/Intervals.csv");
            intervalWriter.write("id,intervalPeriod,startTime,endTime,date\n");
            for(Interval inter : intervals)
            {
                intervalWriter.write(count+","+ inter.intervalPeriod+","+inter.startTime+","+inter.endTime+","+inter.date+"\n");
                intervalMap.put(inter, count);
                count++;
            }
            intervalWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void writeAlertFile(List<AlarmTemplates> alarmTemplatesList) {
        try {
            PrintWriter writer = new PrintWriter("./output/Alerts.csv");
            writer.write("id,incidentId,message,source,ci,service\n");
            for(int i=0; i<alarmTemplatesList.size(); i++)
            {
                AlarmTemplates templates = alarmTemplatesList.get(i);
                writer.write(i+","+templates.getIncidentId()+","+templates.message
                        +","+templates.source+","+templates.host+","+templates.service+"\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
