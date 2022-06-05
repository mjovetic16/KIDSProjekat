package cli.command;

import app.AppConfig;

import java.util.MissingResourceException;

public class StatusCommand implements CLICommand{
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

    }

    public void getStatusSingle(String jobName){

    }

    public void getStatusForNode(String jobName, String nodeID){

    }
}
