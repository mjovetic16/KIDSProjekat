package app.models.job;

import app.models.Node;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class ActiveJob implements Serializable {


    @Serial
    private static final long serialVersionUID = 1191422444574620316L;


    private Job job;

    private boolean active;

    private boolean set;

    private HashMap<String, Node> jobNodes = new HashMap();

    private Section section;

    private int jobNodesLimit;


    private Node myNode;


    public ActiveJob() {

        this.active = false;
        this.set = false;
    }

    public ActiveJob(Job job, boolean active, HashMap<String, Node> jobNodes, Section section) {
        this.job = job;
        this.active = active;
        this.jobNodes = jobNodes;
        this.section = section;
        this.jobNodesLimit = 0;
        this.set = false;
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
        this.jobNodes = Node;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Node getMyNode() {
        return myNode;
    }

    public void setMyNode(Node myNode) {
        this.myNode = myNode;
    }

    public int getJobNodesLimit() {
        return jobNodesLimit;
    }

    public void setJobNodesLimit(int jobNodesLimit) {
        this.jobNodesLimit = jobNodesLimit;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public ActiveJob copy(){
        ActiveJob a = new ActiveJob();
        a.setJob(job);
        a.setActive(active);
        a.setSection(section);
        a.setJobNodes(jobNodes);
        a.setMyNode(myNode);
        a.setJobNodesLimit(jobNodesLimit);
        a.setSet(set);

        return a;

    }

    @Override
    public String toString() {
        return "ActiveJob{" +
                "job=" + job +
                ", active=" + active +
                ", jobNodes=" + jobNodes +
                ", section=" + section +
                ", myNode="+myNode+
                ", isSet="+set+

                '}';
    }
}
