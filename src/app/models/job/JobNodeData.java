package app.models.job;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class JobNodeData implements Serializable {

    @Serial
    private static final long serialVersionUID = -3051374605173530949L;


    private String id;

    private List<Dot> dots;

    public JobNodeData(String id, List<Dot> dots) {
        this.id = id;
        this.dots = dots;
    }

    public JobNodeData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    @Override
    public String toString() {
        return "JobNodeData{" +
                "id='" + id + '\'' +
                '}';
    }
}
