package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.models.message.NewNodeData;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.types.UpdateMessage;
import servent.message.types.WelcomeMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class WelcomeMessageHandler implements MessageHandler{


    private Message clientMessage;



    public WelcomeMessageHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }



    @Override
    public void run() {

        try {
            WelcomeMessage message = (WelcomeMessage) clientMessage;

            NewNodeData newNodeData = message.getNewNodeData();


            AppConfig.setNeighbors(newNodeData.getNeighbors());
            AppConfig.setServentCount(newNodeData.getServentCount());



            for(ServentInfo neigbor: AppConfig.getNeighbors()){

                UpdateMessage updateMessage = new UpdateMessage(AppConfig.myServentInfo,neigbor,newNodeData);

                MessageUtil.sendMessage(updateMessage);

            }




        }catch (Exception e){
            errorLog("Error",e);
        }



    }




    public void log(String s){

        AppConfig.timestampedStandardPrint("[WelcomeMessageHandler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[WelcomeMessageHandler]: "+s);
        AppConfig.timestampedErrorPrint("[WelcomeMessageHandler]: "+e.toString());
        AppConfig.timestampedErrorPrint("[WelcomeMessageHandler]: "+ Arrays.toString(e.getStackTrace()));
    }
}
