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

public class JobManager implements Runnable, Cancellable {

    private ConnectionHandler connectionHandler;

    private ResultHandler resultHandler;

    private StatusHandler statusHandler;

    private JobHandler jobHandler;

    private FractalWorker fractalWorker;



    public JobManager() {
        this.connectionHandler = new ConnectionHandler();
        this.resultHandler = new ResultHandler();
        this.statusHandler = new StatusHandler();
        this.jobHandler = new JobHandler();
        this.fractalWorker = new FractalWorker();
    }

    @Override
    public void stop() {

        fractalWorker.stop();
    }

    @Override
    public void run() {

    }


    public void stopJob(){
        fractalWorker.stop();
    }


    public void startJob(Response response){

            //log(response+"");

        try{

            AppConfig.setActiveJob((ActiveJob) response.getData());


            ActiveJob activeJob = AppConfig.getActiveJob();

            FractalWorker fractalWorker1 = new FractalWorker(activeJob,"fractal/images/"+activeJob.getJob().getName()+".png");
            fractalWorker1.run();


        }catch (Exception e){
            AppConfig.timestampedErrorPrint(e.toString());
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
    }

    public void log(String s){

        AppConfig.timestampedStandardPrint("[JobManager]: "+s);
    }
}
