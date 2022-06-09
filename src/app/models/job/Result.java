package app.models.job;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = -264843505940015240L;

    private HashMap<String,Dot> filledDotMap;
    private long iterationNumber;

    private ActiveJob activeJob;

    public Result(HashMap<String, Dot> filledDotMap, long iterationNumber, ActiveJob activeJob) {
        this.filledDotMap = filledDotMap;
        this.iterationNumber = iterationNumber;
        this.activeJob = activeJob;
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



    public ActiveJob getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(ActiveJob activeJob) {
        this.activeJob = activeJob;
    }
}
