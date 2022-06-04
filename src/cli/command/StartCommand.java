package cli.command;

import app.AppConfig;
import app.models.ActiveJob;
import app.models.Job;
import app.models.Section;
import servent.workers.FractalWorker;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements CLICommand{



    @Override
    public String commandName() {
        return "start";
    }

    @Override
    public void execute(String args) {


        try {

            testRun();

        } catch (Exception e) {
            AppConfig.timestampedErrorPrint("Exception in start command");
            AppConfig.timestampedErrorPrint(e.toString());
        }
    }


    public void testRun() throws Exception{
        ArrayList<Job> jobs = (ArrayList<Job>) AppConfig.getJobList();

        Job job = jobs.get(0);
        ActiveJob activeJob = new ActiveJob();

        activeJob.setActive(true);
        activeJob.setJob(job);

        Section section = new Section();
        section.setDepth(1);
        section.setDots(job.getA());

        activeJob.setSection(section);

        FractalWorker fractalWorker = new FractalWorker(activeJob,"");
        fractalWorker.run();

    }
}
