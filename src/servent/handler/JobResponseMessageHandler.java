package servent.handler;

import app.AppConfig;
import app.manager.JobManager;
import app.models.message.ResponseType;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.MessageType;

public class JobResponseMessageHandler implements MessageHandler{


    private Message clientMessage;

    private JobManager jobManager;


    public JobResponseMessageHandler(Message clientMessage, JobManager jobManager) {
        this.clientMessage = clientMessage;
        this.jobManager = jobManager;
    }



    @Override
    public void run() {

        try{
            JobResponseMessage message = (JobResponseMessage)clientMessage;


            //Handler je dobio poruku
            if(message.getResponse().getResponseType()== ResponseType.JOB_RESPONSE){

                jobManager.getJobHandler().recordResponse(message.getResponse());


            }else if(message.getResponse().getResponseType()== ResponseType.ACCEPTED_OR_REJECTED_JOB_RESPONSE){
               //Node je dobio poruku da li je odobren ili ne

                //Ako je prihvacen startuje posao
                if(message.getResponse().isAccepted()) {
                    jobManager.startJob(message.getResponse());
                }else{
                    //Ako nije prihvacen resetuje se
                    jobManager.getJobHandler().clear();
                }

            }






        }catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
        }

    }
}
