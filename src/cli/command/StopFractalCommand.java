package cli.command;

import app.AppConfig;
import app.ServentInfo;
import app.manager.JobManager;
import servent.message.types.StopMessage;
import servent.message.util.MessageUtil;

public class StopFractalCommand implements CLICommand{

    private JobManager jobManager;

    public StopFractalCommand(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public String commandName() {
        return "stopfractal";
    }

    @Override
    public void execute(String args) {


        for(ServentInfo serventInfo: AppConfig.getNeighbors()){

            StopMessage stopMessage = new StopMessage(AppConfig.myServentInfo,serventInfo,args);

            MessageUtil.sendMessage(stopMessage);

        }


    }
}
