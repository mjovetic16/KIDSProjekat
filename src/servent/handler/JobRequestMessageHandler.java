package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import servent.message.types.JobRequestMessage;
import servent.message.Message;

import java.util.Arrays;

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
            errorLog("Error in JobRequestMessageHandler",e);
        }

    }


    public void log(String s){

        AppConfig.timestampedStandardPrint("[JobRequestMessageHandler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[JobRequestMessageHandler]: "+s);
        AppConfig.timestampedErrorPrint("[JobRequestMessageHandler]: "+e.toString());
        AppConfig.timestampedErrorPrint("[JobRequestMessageHandler]: "+ Arrays.toString(e.getStackTrace()));
    }
}
