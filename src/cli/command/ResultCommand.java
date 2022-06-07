package cli.command;

import app.AppConfig;
import app.manager.JobManager;

import java.util.Arrays;

public class ResultCommand implements CLICommand{


    private JobManager jobManager;

    public ResultCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public String commandName() {
        return "result";
    }

    @Override
    public void execute(String args) {

        log("Finding result for: "+args);
        jobManager.getResultHandler().start(args);
    }

    public void log(String s){

        AppConfig.timestampedStandardPrint("[ResultCommand]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[ResultCommand]: "+s);
        AppConfig.timestampedErrorPrint("[ResultCommand]: "+e.toString());
        AppConfig.timestampedErrorPrint("[ResultCommand]: "+ Arrays.toString(e.getStackTrace()));
    }

}
