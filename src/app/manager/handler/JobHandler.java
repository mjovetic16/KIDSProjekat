package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
import app.models.Node;
import app.models.job.ActiveJob;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.JobRequestMessage;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JobHandler {

    private ActiveJob activeJob;

    private ConcurrentHashMap<String,Response> responseMap;

    private int tempAmount = 0;


    public JobHandler() {
    }


    public void start(){

        //TODO number of possible nodes to join job
        tempAmount = 3;

        activeJob = AppConfig.getActiveJob();

        responseMap = new ConcurrentHashMap<>();

        Response response = new Response();

        response.setResponseType(ResponseType.JOB_RESPONSE);
        response.setSender(new Node(AppConfig.myServentInfo,"NOT_SET"));
        response.setAccepted(true);

        responseMap.put(AppConfig.myServentInfo.getId()+"",response);

        sendJobRequestMessages();

    }

    public void reset(){
        activeJob = null;
        responseMap = null;
    }

    public void recordResponse(Response response){

        if(checkIfResponsesDone())return;


        responseMap.put(response.getSender().getID()+"",response);

        if(checkIfResponsesDone()){
            sendResponses();
        }

    }

    public boolean checkIfResponsesDone(){
        return responseMap.size()>=tempAmount;
    }



    public void sendJobRequestMessages(){

        for(int neighborID : AppConfig.myServentInfo.getNeighbors()){

            ServentInfo neighbor = AppConfig.getInfoById(neighborID);

            Message jobRequestMessage = new JobRequestMessage(
                    AppConfig.myServentInfo, neighbor, activeJob);

            MessageUtil.sendMessage(jobRequestMessage);

        }

    }

    public void sendResponses(){

        for(int neighborID : AppConfig.myServentInfo.getNeighbors()){

            ServentInfo neighbor = AppConfig.getInfoById(neighborID);

            boolean accepted = responseMap.containsKey(neighborID + "");


            Response response = new Response();
            response.setResponseType(ResponseType.ACCEPTED_OR_REJECTED_JOB_RESPONSE);
            response.setSender(new Node(AppConfig.myServentInfo,"NOT_SET"));
            response.setAccepted(accepted);

            Message jobResponseMessage = new JobResponseMessage(
                    AppConfig.myServentInfo, neighbor, response);

            MessageUtil.sendMessage(jobResponseMessage);

        }


    }






}
