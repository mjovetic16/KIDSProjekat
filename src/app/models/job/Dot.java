package app.models.job;

import java.io.Serial;
import java.io.Serializable;

public class Dot implements Serializable {

    @Serial
    private static final long serialVersionUID = -1519400727879612058L;


    private int x;

    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public Dot() {
    }

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Dot{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public String toStringSimplified() {
        return "("+x+", "+y+")";
    }

    public Dot copy(){
        return new Dot(x,y);
    }
}
