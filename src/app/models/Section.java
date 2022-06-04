package app.models;

import java.util.HashMap;

public class Section {

    private int size;

    private int depth;

    private HashMap<String,Dot> dots;



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
