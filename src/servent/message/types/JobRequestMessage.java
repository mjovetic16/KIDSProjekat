package servent.message.types;

import app.ServentInfo;
import app.models.job.ActiveJob;
import servent.message.MessageType;

import java.io.Serial;

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
