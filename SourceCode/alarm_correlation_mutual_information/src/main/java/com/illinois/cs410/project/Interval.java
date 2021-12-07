package com.illinois.cs410.project;

import java.util.Objects;

public class Interval {
    String startTime;
    String endTime;
    String intervalPeriod;
    String date;

    public Interval(String startTime, String endTime, String intervalData, String date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalPeriod = intervalData;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return Objects.equals(intervalPeriod, interval.intervalPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervalPeriod);
    }

    @Override
    public String toString() {
        return "Interval{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", intervalPeriod='" + intervalPeriod + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
