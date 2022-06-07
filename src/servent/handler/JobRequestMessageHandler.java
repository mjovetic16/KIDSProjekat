package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import app.manager.handler.JobHandler;
import app.models.Node;
import app.models.message.Response;
import servent.message.JobRequestMessage;
import servent.message.Message;

import java.security.spec.ECField;

public class JobRequestMessageHandler implements MessageHandler{

    private JobManager jobManager;

    private Message clientMessage;

    public JobRequestMessageHandler(Message clientMessage, JobManager jobManager) {
        this.jobManager = jobManager;
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        try{

            JobRequestMessage message = (JobRequestMessage)clientMessage;


            jobManager.getJobHandler().setNewJob(message.getOriginalSenderInfo(),message.getActiveJob());





        }catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
        }

    }
}