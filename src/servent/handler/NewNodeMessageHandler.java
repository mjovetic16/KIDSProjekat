package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.models.message.NewNodeData;
import servent.message.Message;
import servent.message.types.NewNodeMessage;
import servent.message.types.WelcomeMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewNodeMessageHandler implements MessageHandler{

    private Message clientMessage;

    public NewNodeMessageHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        log("GOT new node message");
        log("New node message arrived :"+clientMessage+"");


        try {
            NewNodeMessage message = (NewNodeMessage) clientMessage;

            NewNodeData newNodeData = new NewNodeData();

            List<ServentInfo> neigborList = AppConfig.getNeighbors();
            neigborList.add(clientMessage.getOriginalSenderInfo());

            AppConfig.setNeighbors(neigborList);
            AppConfig.setServentCount(AppConfig.getServentCount()+1);


            newNodeData.setNeighbors(neigborList);
            newNodeData.setServentCount(AppConfig.getServentCount());


            WelcomeMessage welcomeMessage = new WelcomeMessage(AppConfig.myServentInfo,clientMessage.getOriginalSenderInfo(),newNodeData);
            MessageUtil.sendMessage(welcomeMessage);

            log("Sending welcome message: "+welcomeMessage);



        }catch (Exception e){
            errorLog("Error",e);
        }



    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[NewNodeMH]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[NewNodeMH]: "+s);
        AppConfig.timestampedErrorPrint("[NewNodeMH]: "+e.toString());
        AppConfig.timestampedErrorPrint("[NewNodeMH]: "+ Arrays.toString(e.getStackTrace()));
    }
}
