package servent.message;

import app.AppConfig;
import app.ServentInfo;
import app.models.job.ActiveJob;

import java.io.Serial;
import java.util.List;

public class JobRequestMessage extends BasicMessage {

    @Serial
    private static final long serialVersionUID = 8356598464911042447L;

    private ActiveJob activeJob;

    public JobRequestMessage(ServentInfo sender, ServentInfo receiver, ActiveJob activeJob) {
        super(MessageType.JOB_REQUEST, sender, receiver);
        this.activeJob = activeJob;

    }

    @Override
    public void sendEffect() {


    }

    public ActiveJob getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(ActiveJob activeJob) {
        this.activeJob = activeJob;
    }
}
