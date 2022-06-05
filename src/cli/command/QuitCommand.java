package cli.command;

import app.AppConfig;
import app.manager.JobManager;
import cli.CLIParser;
import servent.SimpleServentListener;

public class QuitCommand implements CLICommand{

    private CLIParser parser;
    private SimpleServentListener listener;

    private JobManager jobManager;

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
        AppConfig.timestampedStandardPrint("Quitting...");
        jobManager.stop();
        parser.stop();
        listener.stop();
    }
}
