package cli.command;

import app.manager.JobManager;

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

    }
}
