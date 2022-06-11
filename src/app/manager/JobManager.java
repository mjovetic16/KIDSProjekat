package app.manager;

import app.AppConfig;
import app.Cancellable;
import app.manager.handler.ConnectionHandler;
import app.manager.handler.JobHandler;
import app.manager.handler.ResultHandler;
import app.manager.handler.StatusHandler;
import app.manager.worker.FractalWorker;
import app.models.job.ActiveJob;
import app.models.message.Response;

import java.util.Arrays;

public class JobManager implements Runnable, Cancellable {

    private ConnectionHandler connectionHandler;

    private ResultHandler resultHandler;

    private StatusHandler statusHandler;

    private JobHandler jobHandler;

    private FractalWorker fractalWorker;



    public JobManager() {
        this.connectionHandler = new ConnectionHandler();
        this.statusHandler = new StatusHandler();
        this.jobHandler = new JobHandler();
        this.resultHandler = new ResultHandler();
        this.fractalWorker = null;
    }

    @Override
    public void stop() {



    }

    @Override
    public void run() {

    }


    public void stopJob(){

        try{
            //log(getFractalWorker()+"");

            if(getFractalWorker()!=null) {
//                log("true");
                getFractalWorker().stopJob();
//                log("Here");

            }else{
//                log("false");
            }


        }catch (Exception e){
            errorLog("Error in stopJob()",e);
        }

        stop();

    }


    public void startJob(Response response){

//            log("Start job");



        try{

            AppConfig.setActiveJob((ActiveJob) response.getData());
//            log("Setovan");


            ActiveJob activeJob = AppConfig.getActiveJob();

            setResultHandler(new ResultHandler(this, AppConfig.getActiveJob()));

            FractalWorker f1 = new FractalWorker(activeJob,"fractal/images/parts/"+activeJob.getJob().getName());
            setFractalWorker(f1);
            f1.run();

            log("My job ID is: "+activeJob.getMyNode().getID());



        }catch (Exception e){
            errorLog("Start job exception",e);
        }



    }










    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;

//        log("Result handler set: "+getResultHandler());
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }

    public void setStatusHandler(StatusHandler statusHandler) {
        this.statusHandler = statusHandler;
    }

    public JobHandler getJobHandler() {
        return jobHandler;
    }

    public void setJobHandler(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    public FractalWorker getFractalWorker() {
        return fractalWorker;
    }

    public void setFractalWorker(FractalWorker fractalWorker) {
        this.fractalWorker = fractalWorker;

//        log("Set fractal worker: "+fractalWorker);
    }

    public void log(String s){

        AppConfig.timestampedStandardPrint("[JobManager]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[JobManager]: "+s);
        AppConfig.timestampedErrorPrint("[JobManager]: "+e.toString());
        AppConfig.timestampedErrorPrint("[JobManager]: "+ Arrays.toString(e.getStackTrace()));
    }


    @Override
    public String toString() {
        return "JobManager{" +
                "connectionHandler=" + connectionHandler +
//                ", resultHandler=" + resultHandler +
                ", statusHandler=" + statusHandler +
                ", jobHandler=" + jobHandler +
//                ", fractalWorker=" + fractalWorker +
                '}';
    }
}
