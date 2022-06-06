package app.models.job;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Section implements Serializable {

    @Serial
    private static final long serialVersionUID = -5592091854643443266L;


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

    @Override
    public String toString() {
        return "Section{" +
                "size=" + size +
                ", depth=" + depth +
                ", dots=" + dots.values() +
                '}';
    }
}
