package servent.message;

import app.ServentInfo;
import app.models.job.ActiveJob;

import java.io.Serial;

public class ResultMessage extends BasicMessage{

    @Serial
    private static final long serialVersionUID = 4452467686344097362L;

    private ActiveJob activeJob;

    public ResultMessage(ServentInfo sender, ServentInfo receiver, ActiveJob activeJob) {
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
