package app.models;

import java.util.HashMap;
import java.util.List;

public class Job {

    private String name;

    private int n;

    private double p;

    private int w;

    private int h;

    private HashMap<String,Dot> A;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public HashMap<String, Dot> getA() {
        return A;
    }

    public void setA(HashMap<String, Dot> a) {
        A = a;
    }


    @Override
    public String toString() {
        return "Job{" +
                "name='" + name + '\'' +
                ", n=" + n +
                ", p=" + p +
                ", w=" + w +
                ", h=" + h +
                ", A=" + A +
                '}';
    }
}
