package cli.command;

import app.AppConfig;
import app.manager.JobManager;

import java.util.MissingResourceException;

public class StatusCommand implements CLICommand{

    private JobManager jobManager;


    public StatusCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public String commandName() {
        return "status";
    }

    @Override
    public void execute(String args) {


        try {

            if(args==null) {

                getStatusAll();
                return;

            } else if (args.split(" ").length>1) {

                String[] arguments = args.split(" ");
                getStatusForNode(arguments[0],arguments[1]);
                return;

            }else if(args.split(" ").length==1){

                getStatusSingle(args);
                return;

            }

            throw new Exception("Bad arguments: "+args);


        }catch (Exception e){

            AppConfig.timestampedErrorPrint("Error in Status command");
            AppConfig.timestampedErrorPrint(e.toString());

        }

    }


    public void getStatusAll(){

        jobManager.getStatusHandler().getAll();

    }

    public void getStatusSingle(String jobName){

        jobManager.getStatusHandler().getJobStatus(jobName, "NO_ID");
    }

    public void getStatusForNode(String jobName, String nodeID){

        jobManager.getStatusHandler().getJobStatus(jobName,nodeID);

    }
}
