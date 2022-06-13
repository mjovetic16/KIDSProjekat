package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
import app.models.Node;
import app.models.job.Job;
import app.models.job.StatusData;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.Message;
import servent.message.types.ResultMessage;
import servent.message.types.StatusMessage;
import servent.message.util.MessageUtil;

import java.util.Arrays;
import java.util.HashMap;

public class StatusHandler {


    private HashMap<ServentInfo, Response> responseMap;


    public StatusHandler() {

        responseMap = new HashMap<>();


    }

    public void getAll() {

        for(ServentInfo neighbor : AppConfig.getNeighbors()){

            Response myResponse = new Response();
            Node meNode = new Node("NOT_SET",AppConfig.myServentInfo);
            myResponse.setSender(meNode);
            myResponse.setResponseType(ResponseType.STATUS_REQUEST);
            myResponse.setData(null);

            Message statusRequestMessage = new StatusMessage(
                    AppConfig.myServentInfo, neighbor, myResponse);

            MessageUtil.sendMessage(statusRequestMessage);

        }
    }


    public void getJobStatus(String jobName,String fractalID){

        for(ServentInfo neighbor : AppConfig.getNeighbors()){

            Response myResponse = new Response();
            Node meNode = new Node("NOT_SET",AppConfig.myServentInfo);
            myResponse.setSender(meNode);
            myResponse.setResponseType(ResponseType.STATUS_REQUEST);

            StatusData sd = new StatusData();
            sd.setJobName(jobName);
            sd.setFractalID(fractalID);
            myResponse.setData(sd);

            Message statusRequestMessage = new StatusMessage(
                    AppConfig.myServentInfo, neighbor, myResponse);

            MessageUtil.sendMessage(statusRequestMessage);

        }


    }

    public void recordResponse(Response response){
//        log("Got this one: "+response.getSender().getServentInfo());
        responseMap.put(response.getSender().getServentInfo(),response);

        if(checkIfResponsesDone()){
            writeStatus();
        }

    }


    public boolean checkIfResponsesDone(){
//        log("Response size: "+responseMap.size());
        return responseMap.size() == AppConfig.getNeighbors().size();
    }

    public void writeStatus(){

        log("Writing status");

        for(Response r: responseMap.values()){

            Object data = r.getData();

            if(data==null)continue;

            StatusData sd = (StatusData) data;

            log("Job: "+sd.getJobName()+" | FractalID: "+sd.getFractalID()+" | Number of Dots: "+sd.getIterNumber());


        }


    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[Status]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[Status]: "+s);
        AppConfig.timestampedErrorPrint("[Status]: "+e.toString());
        AppConfig.timestampedErrorPrint("[Status]: "+ Arrays.toString(e.getStackTrace()));
    }




}
