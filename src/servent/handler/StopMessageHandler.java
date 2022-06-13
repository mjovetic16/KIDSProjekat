package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import servent.message.Message;

import java.util.Arrays;

public class StopMessageHandler implements MessageHandler{


    private Message clientMessage;

    private JobManager jobManager;

    public StopMessageHandler(Message clientMessage, JobManager jobManager) {
        this.clientMessage = clientMessage;
        this.jobManager = jobManager;
    }

    @Override
    public void run() {

        jobManager.stopJob();

    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[StopMessageHandler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[StopMessageHandler]: "+s);
        AppConfig.timestampedErrorPrint("[StopMessageHandler]: "+e.toString());
        AppConfig.timestampedErrorPrint("[StopMessageHandler]: "+ Arrays.toString(e.getStackTrace()));
    }
}
