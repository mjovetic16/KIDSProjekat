package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.types.UpdateMessage;
import servent.message.types.WelcomeMessage;

import java.util.Arrays;

public class UpdateMessageHandler implements MessageHandler{


    private Message clientMessage;

    public UpdateMessageHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        try {
            UpdateMessage message = (UpdateMessage) clientMessage;

            //TODO Update

            log("Current servent state:"+message.getNewNodeData());
            log("Current node count: "+message.getNewNodeData().getServentCount());

            AppConfig.setNeighbors(message.getNewNodeData().getNeighbors());
            AppConfig.setServentCount(message.getNewNodeData().getServentCount());




        }catch (Exception e){
            errorLog("Error",e);
        }

    }


    public void log(String s){

        AppConfig.timestampedStandardPrint("[UpdateMessageHandler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[UpdateMessageHandler]: "+s);
        AppConfig.timestampedErrorPrint("[UpdateMessageHandler]: "+e.toString());
        AppConfig.timestampedErrorPrint("[UpdateMessageHandler]: "+ Arrays.toString(e.getStackTrace()));
    }
}
