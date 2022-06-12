package cli.command;

import app.AppConfig;
import app.BootstrapServer;
import app.manager.JobManager;
import cli.CLIParser;
import servent.SimpleServentListener;

import java.util.Arrays;

public class QuitCommand implements CLICommand{

    private CLIParser parser;
    private SimpleServentListener listener;

    private JobManager jobManager;

    private BootstrapServer bs;

    public QuitCommand(CLIParser parser, SimpleServentListener listener, JobManager jobManager) {
        this.parser = parser;
        this.listener = listener;
        this.jobManager = jobManager;
    }

    @Override
    public String commandName() {
        return "quit";
    }

    @Override
    public void execute(String args) {
        try{
            AppConfig.timestampedStandardPrint("Quitting...");

//            log(jobManager.getFractalWorker()+"");
            jobManager.stopJob();
            parser.stop();
            listener.stop();
        }catch (Exception e){
            errorLog("Execute stop command error",e);
        }

    }



    public void log(String s){

        AppConfig.timestampedStandardPrint("[QuitCommand]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[QuitCommand]: "+s);
        AppConfig.timestampedErrorPrint("[QuitCommand]: "+e.toString());
        AppConfig.timestampedErrorPrint("[QuitCommand]: "+ Arrays.toString(e.getStackTrace()));
    }
}
