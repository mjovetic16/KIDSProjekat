package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import app.manager.handler.ResultHandler;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.ResultMessage;

import java.util.Arrays;

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


            //Ako je primljen zahtev za rezultat
            if(message.getResponse().getResponseType()==ResponseType.RESULT_REQUEST){

//                log("Send answer");
//                log((jobManager==null)+"");
//                log(jobManager.toString());
//                log((jobManager.getResultHandler())+"");
//                ResultHandler resultHandler = jobManager.getResultHandler();
//                log("Zadnji"+resultHandler);

                log("Got request message from"+message+"");
                jobManager.getResultHandler().sendResult(message.getResponse());

            //Ako je primljen odgovor na zahtev za rezultat
            }else if(message.getResponse().getResponseType()==ResponseType.RESULT_RESPONSE){

//                log("Got answer");

                jobManager.getResultHandler().recordResponse(message.getResponse());

            }




        }catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
        }

    }


    public void log(String s){

        AppConfig.timestampedStandardPrint("[ResultMessageHandler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[ResultMessageHandler]: "+s);
        AppConfig.timestampedErrorPrint("[ResultMessageHandler]: "+e.toString());
        AppConfig.timestampedErrorPrint("[ResultMessageHandler]: "+ Arrays.toString(e.getStackTrace()));
    }
}
