package app.models.message;

import app.ServentInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewNodeData implements Serializable {

    @Serial
    private static final long serialVersionUID = -1130437091212308563L;



    private int serventCount;

    private List<ServentInfo> neighbors;


    public int getServentCount() {
        return serventCount;
    }

    public void setServentCount(int serventCount) {
        this.serventCount = serventCount;
    }

    public List<ServentInfo> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<ServentInfo> neighbors) {
        this.neighbors = neighbors;
    }


    @Override
    public String toString() {
        return "NewNodeData{" +
                "serventCount=" + serventCount +
                ", neighbors=" + neighbors +
                '}';
    }
}
