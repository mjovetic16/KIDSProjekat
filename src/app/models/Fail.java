package app.models;

public class Fail {

    private int weak;

    private int strong;


    public int getWeak() {
        return weak;
    }

    public void setWeak(int weak) {
        this.weak = weak;
    }

    public int getStrong() {
        return strong;
    }

    public void setStrong(int strong) {
        this.strong = strong;
    }

    @Override
    public String toString() {
        return "Fail{" +
                "weak=" + weak +
                ", strong=" + strong +
                '}';
    }
}
