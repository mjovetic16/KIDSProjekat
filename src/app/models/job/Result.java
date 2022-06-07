package app.models.job;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = -264843505940015240L;

    private HashMap<String,Dot> filledDotMap;

    private long iterationNumber;

    public Result(HashMap<String, Dot> filledDotMap, long iterationNumber) {
        this.filledDotMap = filledDotMap;
        this.iterationNumber = iterationNumber;
    }

    public HashMap<String, Dot> getFilledDotMap() {
        return filledDotMap;
    }

    public void setFilledDotMap(HashMap<String, Dot> filledDotMap) {
        this.filledDotMap = filledDotMap;
    }

    public long getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(long iterationNumber) {
        this.iterationNumber = iterationNumber;
    }
}
