package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
import app.models.Node;
import app.models.job.*;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.JobRequestMessage;
import servent.message.JobResponseMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JobHandler {

    private ActiveJob activeJob;

    private ConcurrentHashMap<String,Response> responseMap;

    private int limit = 0;




    public JobHandler() {
    }


    public void start(String args){

        Job job = null;

        if(args.equals("all")){
            return;
        }else{
            activeJob = new ActiveJob();
            activeJob.setActive(false);

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
//        tempAmount = job.getN();
//        tempAmount = 5;

        limit = AppConfig.getServentCount()/AppConfig.getJobList().size();
        if(limit<job.getN())limit = 1;
        log("The limit is: "+limit+" The servent count is:"+AppConfig.getServentCount());

        activeJob.setJobNodesLimit(limit);

        activeJob = AppConfig.getActiveJob();

        responseMap = new ConcurrentHashMap<>();
//
//        Response response = new Response();
//
//        response.setResponseType(ResponseType.JOB_RESPONSE);
//        response.setSender(new Node("0",AppConfig.myServentInfo));
//        response.setAccepted(true);
//
//        responseMap.put(AppConfig.myServentInfo.getId()+"",response);

        sendJobRequestMessages();

//        if(limit==1){
//            jobDivide();
//        }

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

        return responseMap.size()>=AppConfig.getActiveJob().getJobNodesLimit();
    }



    public void sendJobRequestMessages(){

        for(int neighborID : AppConfig.myServentInfo.getNeighbors()){

            ServentInfo neighbor = AppConfig.getInfoById(neighborID);

            Message jobRequestMessage = new JobRequestMessage(
                    AppConfig.myServentInfo, neighbor, activeJob);

            MessageUtil.sendMessage(jobRequestMessage);

        }

    }

    public void sendResponseAcceptNode(ActiveJob activeJob, ServentInfo recepient){

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

        int size = responseMap.size();
        realJobDivide(size);
        //tempStupidJobDevide();
    }



    public List<Dot> returnNewDots(List<Dot> oldDots, double p, int indexOfPivotDot){

        List<Dot> newDots = new ArrayList<>();
        Dot dot1 = oldDots.get(indexOfPivotDot);
        newDots.add(dot1);


        for(Dot d: oldDots){
            if(d==dot1)continue;

            Dot newDot = new Dot();


            int newX = (int) ((1-p)*dot1.getX() + p*d.getX());
            int newY = (int) ((1-p)*dot1.getY() + p*d.getY());

            newDot.setX(newX);
            newDot.setY(newY);

            newDots.add(newDot);
        }

        return newDots;
    };


    public void realJobDivide(int numberOfNodesToDevide){


        Job job = AppConfig.getActiveJob().getJob();

        int n = job.getN();
        int subN = n-1 ;




        Queue<JobNodeData> jobNodeDataQueue = new LinkedList<>();
        List<Dot> oldDots = job.getA().values().stream().toList();

        if(limit<n){
//            List<Dot> newDots = returnNewDots(oldDots,job.getP(),0);
            JobNodeData jobNodeData = new JobNodeData(0+"",oldDots);


            jobNodeDataQueue.add(jobNodeData);
            numberOfNodesToDevide--;
        }else{
            for(int i1 = 0;i1<n;i1++){
                List<Dot> newDots = returnNewDots(oldDots,job.getP(),i1);

                JobNodeData jobNodeData = new JobNodeData(i1+"",newDots);


                jobNodeDataQueue.add(jobNodeData);


                numberOfNodesToDevide--;
            }
        }


        int pointer = 0;
        List<JobNodeData> bufferList = new ArrayList<>();
        log(numberOfNodesToDevide+"");
        numberOfNodesToDevide++;
        while(numberOfNodesToDevide>0){
            JobNodeData currentNode = jobNodeDataQueue.peek();


            JobNodeData newNode = new JobNodeData();
            newNode.setId(currentNode.getId()+pointer);
            newNode.setDots(returnNewDots(currentNode.getDots(),job.getP(),pointer));

            bufferList.add(newNode);

            if(bufferList.size()>=job.getN()){
                jobNodeDataQueue.addAll(bufferList);
                jobNodeDataQueue.poll();
                pointer = 0;
                bufferList.clear();
                //numberOfNodesToDevide++;
                continue;
            }

            pointer++;
            numberOfNodesToDevide--;

        }


        log(jobNodeDataQueue.toString());



        HashMap<String, Node> jobNodesServentMap = new HashMap<>();
        HashMap<String, JobNodeData>  jobNodesDataMap= new HashMap<>();


        for(Response res: responseMap.values()){

            JobNodeData jobNodeDataForCurrentNode = jobNodeDataQueue.poll();


            jobNodesServentMap.put(jobNodeDataForCurrentNode.getId(),res.getSender());
            jobNodesDataMap.put(jobNodeDataForCurrentNode.getId(),jobNodeDataForCurrentNode);

        }



        for(JobNodeData jobNodeData: jobNodesDataMap.values()){

            ActiveJob jobToSend = new ActiveJob();

            jobToSend.setActive(true);
            jobToSend.setJob(job);

            Section section = new Section();

            HashMap<String,Dot> dotMap = new HashMap<>();
            for(Dot d: jobNodeData.getDots()){
                dotMap.put(d.toString(),d);
            }
            section.setDots(dotMap);

            jobToSend.setSection(section);
            jobToSend.setJobNodes(jobNodesServentMap);



            Node node = new Node();
            node.setServentInfo(jobNodesServentMap.get(jobNodeData.getId()).getServentInfo());
            node.setID(jobNodeData.getId());

            jobToSend.setMyNode(node);


            if(node.getServentInfo().getId()==AppConfig.myServentInfo.getId()){
                AppConfig.setActiveJob(jobToSend);
            }


            sendResponseAcceptNode(jobToSend, jobNodesServentMap.get(jobNodeData.getId()).getServentInfo());


        }





    }

    public void tempStupidJobDevide(){


        HashMap<String, Node> jobNodesMap = new HashMap<>();

        //UZASNO resenje ali radi
        HashMap<Integer, String> jobNameMap = new HashMap<>();

        int i = 0;
        int j = 0;

        //Calculate IDS
        for(Response res: responseMap.values()){
            String id = j+"";


            jobNodesMap.put(id,res.getSender());
            jobNameMap.put(res.getSender().getServentInfo().getId(),id);


            //log("In jobdevide the res.getSender is :" +res.getSender());


//            activeJob.setMyNode(new Node(id,res.getSender().getServentInfo()));


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

                int newX = (int) ((1-p)*dot1.getX() + p*d.getX());
                int newY = (int) ((1-p)*dot1.getY() + p*d.getY());

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



            String id = jobNameMap.get(response.getSender().getServentInfo().getId());

            Node n = new Node();
            n.setServentInfo(response.getSender().getServentInfo());
            n.setID(jobNodesMap.get(id).getID());

            activeJob.setMyNode(n);

//            log("When setting job nodes: "+jobNodesMap);
//            log(activeJob+"");


//            AppConfig.timestampedStandardPrint("AC"+activeJob.getSection().getDots().values());

            if(response.getSender().getID().equals("0")){
                AppConfig.setActiveJob(activeJob);
            }


            sendResponseAcceptNode(activeJob, response.getSender().getServentInfo());

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
