package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.manager.JobManager;
import app.models.Node;
import app.models.job.Result;
import app.models.job.StatusData;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.Message;
import servent.message.types.StatusMessage;
import servent.message.util.MessageUtil;

import java.util.Arrays;

public class StatusMessageHandler implements MessageHandler{

    private Message clientMessage;
    private JobManager jobManager;

    public StatusMessageHandler(Message clientMessage, JobManager jobManager) {
        this.clientMessage = clientMessage;
        this.jobManager = jobManager;
    }

    @Override
    public void run() {

        try {

            StatusMessage statusMessage = (StatusMessage) clientMessage;

            if(statusMessage.getResponse().getResponseType()== ResponseType.STATUS_REQUEST){


                if(AppConfig.getActiveJob()!=null){
                    if(AppConfig.getActiveJob().isActive()){
                        Result r = jobManager.getFractalWorker().returnResult();

                        String jobName = AppConfig.getActiveJob().getJob().getName();
                        String fractalID= AppConfig.getActiveJob().getMyNode().getID();
                        long iter = r.getIterationNumber();
                        ServentInfo recepient = statusMessage.getResponse().getSender().getServentInfo();


                        sendRealStatus(jobName,fractalID,iter,recepient);



                    }else{

                        sendEmptyStatus(((StatusMessage) clientMessage).getResponse().getSender().getServentInfo());
                    }
                }else{
                    sendEmptyStatus(((StatusMessage) clientMessage).getResponse().getSender().getServentInfo());

                }


            }else{
                jobManager.getStatusHandler().recordResponse(statusMessage.getResponse());

            }




        }catch (Exception e){
            errorLog("Exception",e);
        }



    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[StatusMH]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[StatusMH]: "+s);
        AppConfig.timestampedErrorPrint("[StatusMH]: "+e.toString());
        AppConfig.timestampedErrorPrint("[StatusMH]: "+ Arrays.toString(e.getStackTrace()));
    }

    public void sendEmptyStatus(ServentInfo recepient){

        Response myResponse = new Response();
        Node meNode = new Node("NOT_SET",AppConfig.myServentInfo);
        myResponse.setSender(meNode);
        myResponse.setResponseType(ResponseType.STATUS_RESPONSE);

        myResponse.setData(null);

        Message statusRequestMessage = new StatusMessage(
                AppConfig.myServentInfo, recepient, myResponse);

        MessageUtil.sendMessage(statusRequestMessage);
    }


    public void sendRealStatus(String jobName, String fractalID, long iter, ServentInfo recepient){
        Response myResponse = new Response();
        Node meNode = new Node("NOT_SET",AppConfig.myServentInfo);
        myResponse.setSender(meNode);
        myResponse.setResponseType(ResponseType.STATUS_RESPONSE);

        StatusData sd = new StatusData();
        sd.setJobName(jobName);
        sd.setFractalID(fractalID);
        sd.setIterNumber(iter);

        myResponse.setData(sd);

        Message statusRequestMessage = new StatusMessage(
                AppConfig.myServentInfo, recepient, myResponse);

        MessageUtil.sendMessage(statusRequestMessage);

    }

}
