package app.models.job;

import app.ServentInfo;

import java.io.Serial;
import java.io.Serializable;

public class JobRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1873290232332533525L;

    private ActiveJob activeJob;

    private ServentInfo serventInfo;


    public ActiveJob getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(ActiveJob activeJob) {
        this.activeJob = activeJob;
    }

    public ServentInfo getServentInfo() {
        return serventInfo;
    }

    public void setServentInfo(ServentInfo serventInfo) {
        this.serventInfo = serventInfo;
    }
}
