package app.models.job;

import java.io.Serial;
import java.io.Serializable;

public class StatusData implements Serializable {


    @Serial
    private static final long serialVersionUID = -1131454584219554462L;

    private long iterNumber;

    private String fractalID;

    private String jobName;


    public long getIterNumber() {
        return iterNumber;
    }

    public void setIterNumber(long iterNumber) {
        this.iterNumber = iterNumber;
    }

    public String getFractalID() {
        return fractalID;
    }

    public void setFractalID(String fractalID) {
        this.fractalID = fractalID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
