package app.models;

import app.ServentInfo;

public class Node {

    private ServentInfo serventInfo;
    private String ID;


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
