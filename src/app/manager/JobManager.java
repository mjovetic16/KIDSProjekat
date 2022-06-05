package app.manager;

import app.Cancellable;
import app.manager.handler.ConnectionHandler;
import app.manager.handler.JobHandler;
import app.manager.handler.ResultHandler;
import app.manager.handler.StatusHandler;
import app.manager.worker.FractalWorker;

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




}
