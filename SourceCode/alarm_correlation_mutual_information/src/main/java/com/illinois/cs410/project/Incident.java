package com.illinois.cs410.project;

public class Incident {
    private int id;
    private int templates;
    private int randomCount;

    public Incident(int id, int templates) {
        this.id = id;
        this.templates = templates;
        this.randomCount = this.templates - getModLog2(this.templates) +1 ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplates() {
        return templates;
    }

    public void setTemplates(int templates) {
        this.templates = templates;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }
    private int getModLog2(int scenario) {
        if(scenario == 0)
            return 0;

        return (int)(Math.log(scenario) / Math.log(2));
    }

    public void updateRandomNumber() {
        this.randomCount = getModLog2(this.templates);
    }
}
