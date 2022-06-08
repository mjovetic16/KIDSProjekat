package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
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
import java.util.concurrent.ConcurrentLinkedQueue;

public class JobHandler {

    private ActiveJob activeJob;

    private ConcurrentHashMap<String,Response> responseMap;

    private int tempAmount = 0;




    public JobHandler() {
    }


    public void start(String args){

        Job job = null;

        if(args.equals("all")){
            return;
        }else{
            activeJob = new ActiveJob();
            activeJob.setActive(true);

            for(Job j:AppConfig.getJobList()){
                if(j.getName().equals(args)){
                    job = j;
                }
            }
        }

        if(job == null){
            AppConfig.timestampedErrorPrint("Job not found for argument: "+args);
            return;
        }

        activeJob.setJob(job);

        AppConfig.setActiveJob(activeJob);



        //TODO number of possible nodes to join job
        tempAmount = 3;

        activeJob = AppConfig.getActiveJob();

        responseMap = new ConcurrentHashMap<>();

        Response response = new Response();

        response.setResponseType(ResponseType.JOB_RESPONSE);
        response.setSender(new Node("0",AppConfig.myServentInfo));
        response.setAccepted(true);

        responseMap.put(AppConfig.myServentInfo.getId()+"",response);

        sendJobRequestMessages();

    }

    public void reset(){
        activeJob = null;
        responseMap = null;
    }

    public void recordResponse(Response response){
        try{



            if(checkIfResponsesDone()){

                sendRejectResponse(response);

                return;
            }


            responseMap.put(response.getSender().getServentInfo().getId()+"",response);

            if(checkIfResponsesDone()){

                jobDivide();
            }


        }
        catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
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

    public void sendResponseAcceptNode(ActiveJob activeJob, Response r){

        ServentInfo recepient = r.getSender().getServentInfo();

        Response response = new Response();
        response.setResponseType(ResponseType.ACCEPTED_OR_REJECTED_JOB_RESPONSE);
        response.setSender(new Node("0",AppConfig.myServentInfo));
        response.setAccepted(true);
        response.setData(activeJob.copy());

       // log(activeJob+"");

        Message jobResponseMessage = new JobResponseMessage(
                AppConfig.myServentInfo, recepient, response);

        MessageUtil.sendMessage(jobResponseMessage);

    }

    public void sendRejectResponse(Response response){

        ServentInfo recepient = AppConfig.getInfoById(response.getSender().getServentInfo().getId());

        response.setResponseType(ResponseType.ACCEPTED_OR_REJECTED_JOB_RESPONSE);
        response.setSender(new Node("NOT_SET",AppConfig.myServentInfo));
        response.setAccepted(false);

        Message jobResponseMessage = new JobResponseMessage(
                AppConfig.myServentInfo, recepient, response);

        MessageUtil.sendMessage(jobResponseMessage);

    }

    public void jobDivide(){

        tempStupidJobDevide();




    }

    public void tempStupidJobDevide(){


        HashMap<String, Node> jobNodesMap = new HashMap<>();

        int i = 0;
        int j = 0;

        //Calculate IDS
        for(Response res: responseMap.values()){
            String id = j+"";

            jobNodesMap.put(id,res.getSender());


            log("In jobdevide the res.getSender is :" +res.getSender());
            activeJob.setMyNode(new Node(id,res.getSender().getServentInfo()));


            j++;

        }


        //Make active jobs, set their dots and send them in response messages
        for(Response response:responseMap.values()){


            Job job = AppConfig.getActiveJob().getJob();


            HashMap<String, Dot> dotMap = new HashMap<>();
            List<Dot> allDots =  job.getA().values().stream().toList();
            ArrayList<Dot> otherDots = new ArrayList<>();

            Dot dot1 = allDots.get(i);


            dotMap.put(dot1.toString(),dot1);

            for(Dot d: allDots){
                if(d==dot1)continue;

                Dot newDot = new Dot();

                double p = job.getP();

                int newX = (int) ((1-p)*d.getX() + p*dot1.getX());
                int newY = (int) ((1-p)*d.getY() + p*dot1.getY());

                newDot.setX(newX);
                newDot.setY(newY);

                dotMap.put(newDot.toString(),newDot);
            }


            activeJob.setActive(true);
            activeJob.setJob(job);

            Section section = new Section();
            section.setDepth(1);

            section.setDots(dotMap);

            activeJob.setSection(section);
            activeJob.setJobNodes(jobNodesMap);

//            log("When setting job nodes: "+jobNodesMap);
//            log(activeJob+"");


//            AppConfig.timestampedStandardPrint("AC"+activeJob.getSection().getDots().values());

            if(response.getSender().getID().equals("0")){
                AppConfig.setActiveJob(activeJob);
            }


            sendResponseAcceptNode(activeJob, response);

            i++;
        }
    }

    public void clear(){
        //Skida se aktivan job

        ActiveJob activeJob1 = AppConfig.getActiveJob();
        activeJob1.setActive(false);

        AppConfig.setActiveJob(activeJob1);

    }


    public void setNewJob(ServentInfo sender,ActiveJob activeJob) {
        //Ako je vec postavljen neki job na ovaj node odbija se request
        //Ako nije prihvata se


        if(AppConfig.getActiveJob().isActive()){

            Response response = new Response();
            response.setResponseType(ResponseType.JOB_RESPONSE);
            response.setSender(new Node("NOT_SET",AppConfig.myServentInfo));
            response.setAccepted(false);

            Message jobResponseMessage = new JobResponseMessage(
                    AppConfig.myServentInfo, sender, response);

            MessageUtil.sendMessage(jobResponseMessage);

            return;

        }else{

            AppConfig.setActiveJob(activeJob);

            Response response = new Response();
            response.setResponseType(ResponseType.JOB_RESPONSE);
            response.setSender(new Node("NOT_SET",AppConfig.myServentInfo));
            response.setAccepted(true);

            Message jobResponseMessage = new JobResponseMessage(
                    AppConfig.myServentInfo, sender, response);

            MessageUtil.sendMessage(jobResponseMessage);


        }




    }




    public void log(String s){

        AppConfig.timestampedStandardPrint("[JobHandler]: "+s);
    }
}
