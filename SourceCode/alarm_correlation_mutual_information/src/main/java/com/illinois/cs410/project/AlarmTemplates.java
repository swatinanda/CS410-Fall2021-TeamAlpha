package com.illinois.cs410.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.BitSet;

public class AlarmTemplates {
    Long id;
    String message;
    String host;
    String source;
    String service;
    ArrayList<Interval> intervals;
    int incidentId;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getHost() {
        return host;
    }

    public String getSource() {
        return source;
    }

    public String getService() {
        return service;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public AlarmTemplates(Long id, String message, String host, String source, String service) {
        this.id = id;
        this.message = message;
        this.host = host;
        this.source = source;
        this.service = service;
        this.intervals = new ArrayList<>();
    }

    public AlarmTemplates() {
    }

    public void setIntervals(BitSet bits) {
        String b = bits.toString();
        String[] bitArray = b.replace("}","").replace("{","").split(",");
        for(int k=0; k<bitArray.length; k++)
        {
            String bit = bitArray[k].trim();
            if(!bit.isEmpty()) {
                getIntervalData(Integer.parseInt(bitArray[k].trim()));
                this.intervals.add(getIntervalData(Integer.parseInt(bitArray[k].trim())));
            }
        }
    }

    private Interval getIntervalData(int i) {
        if(i>=288)
            return null;
        LocalDateTime previousDay = LocalDateTime.now().minusDays(1);
        String date = previousDay.getMonthValue() +"-"+previousDay.getDayOfMonth()+"-"+previousDay.getYear();
        int hr = i * 5/60;
        int mnt = (i*5 % 60);
        String startTime = getFormat(hr) +":"+getFormat(mnt);
        int newMnt = mnt + 5;
        if(newMnt == 60) {
            newMnt = 0;
            hr = hr+1;
        }
        String endTime = getFormat(hr) +":"+getFormat(newMnt);
        String intervalData = startTime +"-"+endTime;
        return new Interval(startTime, endTime, intervalData, date);
    }

    private String getFormat(int number) {
        return number > 9 ? "" + number: "0" + number;
    }

    @Override
    public String toString() {
        return "AlarmTemplates{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", host='" + host + '\'' +
                ", source='" + source + '\'' +
                ", service='" + service + '\'' +
                ", incidentId=" + incidentId +
                '}';
    }
}
