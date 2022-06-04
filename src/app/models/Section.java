package app.models;

import java.util.HashMap;

public class Section {

    private String size;

    private int depth;

    private HashMap<String,Dot> dots;


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public HashMap<String, Dot> getDots() {
        return dots;
    }

    public void setDots(HashMap<String, Dot> dots) {
        this.dots = dots;
    }
}
