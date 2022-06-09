package app.manager.handler;

import app.AppConfig;
import app.ServentInfo;
import app.manager.JobManager;
import app.models.Node;
import app.models.job.*;
import app.models.message.Response;
import app.models.message.ResponseType;
import servent.message.Message;
import servent.message.ResultMessage;
import servent.message.util.MessageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ResultHandler {

    private ActiveJob activeJob;

    private JobManager jobManager;

    private ActiveJob requestedJob;

    private ConcurrentHashMap<String,Response> responseMap = new ConcurrentHashMap<>();

    private String path;

    public ResultHandler() {
    }

    public ResultHandler(JobManager jobManager, ActiveJob activeJob) {
        this.jobManager = jobManager;
        this.path = "fractal/images/"+"empty"+".png";
    }

    public void start(String args){

        Job job = null;

        String[] arguments = args.split(" ");
        if(arguments.length>1){
            //TODO Result samo za zadati id
            return;
        }else{

            sendResultRequestMessages(arguments[0]);
        }

//        if(job == null){
//            errorLog("Result not found for argument: "+args,new Exception());
//            return;
//        }





    }

    public void reset(){
        activeJob = null;
        responseMap = null;
    }

    public void recordResponse(Response response){
        try{

            if(requestedJob==null){

                Result res = ((Result) response.getData());

                setRequestedJob(res.getActiveJob());

            }

            if(checkIfResponsesDone()){

                return;
            }


            responseMap.put(response.getSender().getServentInfo().getId()+"",response);
            //log(response.getSender().getServentInfo().getId()+"");


            if(checkIfResponsesDone()){
                drawResult(path);
            }
            log("6");

        }
        catch (Exception e){
            errorLog(e.getMessage(),e);
        }

    }

    public boolean checkIfResponsesDone(){
        //log("Not done yet rm:"+ responseMap.size());
        //log("Not done yet ac:"+ activeJob.getJobNodes().size());

        if(requestedJob==null)return false;

        return responseMap.size() >= requestedJob.getJobNodes().size();
    }



    public void sendResultRequestMessages(String argument){

        //TODO kome se salje?

        //TODO Nula kad trazi nema setovan active job

//        log("In send rquest");
        activeJob = AppConfig.getActiveJob();;


        log(AppConfig.getActiveJob().isActive()+"");





//        log(activeJob.getJobNodes().size()+"");
//        log(activeJob+"");

        for(ServentInfo neighbor : AppConfig.getServentInfoList()){


            Response myResponse = new Response();
            Node meNode = new Node("NOT_SET",AppConfig.myServentInfo);
            myResponse.setSender(meNode);
            myResponse.setResponseType(ResponseType.RESULT_REQUEST);
            myResponse.setData(argument);

            Message resultRequestMessage = new ResultMessage(
                    AppConfig.myServentInfo, neighbor, myResponse);

            MessageUtil.sendMessage(resultRequestMessage);


            //log("Sent result request message");

//            log("Sent message: "+resultRequestMessage);

        }

//        log("Out of send rquest");



    }

    public void drawResult(String path){

        log("1");

        BufferedImage image = new BufferedImage(requestedJob.getJob().getW(), requestedJob.getJob().getH(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = image.createGraphics ();

        log("2");

        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0, 0, requestedJob.getJob().getW(), requestedJob.getJob().getH());

        log("3");
        for(Response r: responseMap.values()){


            Color randColor = new Color((int)(Math.random() * 0x1000000));
            graphics2D.setPaint(randColor);

            Result result = (Result) r.getData();
            HashMap<String, Dot> filledDotsMap = result.getFilledDotMap();


            for (Dot d : filledDotsMap.values()) {
                graphics2D.drawOval(d.getX(), d.getY(), 1, 1);
            }

        }
        log("4");


        try {
            ImageIO.write(image, "png", new File(path));

        } catch (IOException e) {
            errorLog("Error writing file",e);
        }

        log("5");

        log("Done drawing job: " + requestedJob.getJob().getName() +" with dots:"+requestedJob.getJob().getA().values());

        graphics2D.dispose();

        reset();
    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[Result Handler]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[Result Handler]: "+s);
        AppConfig.timestampedErrorPrint("[Result Handler]: "+e.toString());
    }

    public void sendResult(Response response) {

//        log("Send result enter");
        Response myResponse = new Response();
//        log((this.jobManager==null)+"");
//        log((getJobManager()==null)+"");

        myResponse.setResponseType(ResponseType.RESULT_RESPONSE);
        myResponse.setData(jobManager.getFractalWorker().returnResult());
        //log("Before sending result my node is:"+AppConfig.getActiveJob().getMyNode());
        myResponse.setSender(AppConfig.getActiveJob().getMyNode());


        Message resultResponseMessage = new ResultMessage(
                AppConfig.myServentInfo, response.getSender().getServentInfo(), myResponse);

        MessageUtil.sendMessage(resultResponseMessage);

        //log("Sent result response message");


    }


    public ActiveJob getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(ActiveJob activeJob) {
        this.activeJob = activeJob;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public void setJobManager(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    public ActiveJob getRequestedJob() {
        return requestedJob;
    }

    public void setRequestedJob(ActiveJob requestedJob) {
        this.requestedJob = requestedJob;
        this.path = "fractal/images/"+requestedJob.getJob().getName()+".png";
    }

    @Override
    public String toString() {
        return "ResultHandler{" +
                "activeJob=" + activeJob +
                ", jobManager=" + jobManager +
                ", responseMap=" + responseMap +
                ", path='" + path + '\'' +
                '}';
    }
}
