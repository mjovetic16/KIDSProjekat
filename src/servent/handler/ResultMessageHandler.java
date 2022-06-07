package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import app.models.message.ResponseType;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.ResultMessage;

public class ResultMessageHandler implements MessageHandler{

    private Message clientMessage;

    private JobManager jobManager;


    public ResultMessageHandler(Message clientMessage, JobManager jobManager) {
        this.clientMessage = clientMessage;
        this.jobManager = jobManager;
    }



    @Override
    public void run() {

        try{
            ResultMessage message = (ResultMessage) clientMessage;






        }catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
        }

    }
}
