package app.models;

import app.ServentInfo;

import java.util.HashMap;

public class ActiveJob {

    private Job job;

    private boolean active;

    private HashMap<String, Node> jobNodes = new HashMap();

    private Section section;


    public ActiveJob() {

        this.active = false;
    }

    public ActiveJob(Job job, boolean active, HashMap<String, Node> jobNodes, Section section) {
        this.job = job;
        this.active = active;
        this.jobNodes = jobNodes;
        this.section = section;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HashMap<String, Node> getJobNodes() {
        return jobNodes;
    }

    public void setJobNodes(HashMap<String, Node> Node) {
        this.jobNodes = jobNodes;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
