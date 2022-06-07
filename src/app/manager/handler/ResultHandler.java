package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
import app.manager.JobManager;
import app.models.Node;
import app.models.job.ActiveJob;
import app.models.job.Dot;
import app.models.job.Job;
import app.models.job.Section;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.JobRequestMessage;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ResultHandler {

    private ActiveJob activeJob;

    private JobManager jobManager;

    private ConcurrentHashMap<String,Response> responseMap;


    public ResultHandler(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    public void start(String args){

        Job job = null;

        if(args.equals("all")){
            return;
        }else{
            activeJob = new ActiveJob();
            activeJob.setActive(true);

            for(Job j: AppConfig.getJobList()){
                if(j.getName().equals(args)){
                    job = j;
                }
            }
        }

        if(job == null){
            AppConfig.timestampedErrorPrint("Result not found for argument: "+args);
            return;
        }



        sendResultRequestMessages();

    }

    public void reset(){
        activeJob = null;
        responseMap = null;
    }

    public void recordResponse(Response response){
        try{

            if(checkIfResponsesDone()){

                return;
            }


            responseMap.put(response.getSender().getServentInfo().getId()+"",response);


            if(checkIfResponsesDone()){


            }


        }
        catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
        }

    }

    public boolean checkIfResponsesDone(){
//        return responseMap.size()>=tempAmount;

        return false;
    }



    public void sendResultRequestMessages(){

//        for(int neighborID : AppConfig.myServentInfo.getNeighbors()){
//
//            ServentInfo neighbor = AppConfig.getInfoById(neighborID);
//
//            Message jobRequestMessage = new JobRequestMessage(
//                    AppConfig.myServentInfo, neighbor, activeJob);
//
//            MessageUtil.sendMessage(jobRequestMessage);
//
//        }

    }

    public void drawResult(){


        reset();
    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[Result Handler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[Result Handler]: "+s);
        AppConfig.timestampedErrorPrint("[Result Handler]: "+e.toString());
    }
}
