package app.models;

import app.ServentInfo;

import java.io.Serial;
import java.io.Serializable;

public class Node implements Serializable {

    @Serial
    private static final long serialVersionUID = -8423619568724095092L;

    private ServentInfo serventInfo;
    private String ID;


    public Node() {
    }

    public Node(ServentInfo serventInfo, String ID) {
        this.serventInfo = serventInfo;
        this.ID = ID;
    }

    public ServentInfo getServentInfo() {
        return serventInfo;
    }

    public void setServentInfo(ServentInfo serventInfo) {
        this.serventInfo = serventInfo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
