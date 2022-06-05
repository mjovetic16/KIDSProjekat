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
        Job job2 = jobs.get(1);
        Job job3 = jobs.get(2);
        Job job4 = jobs.get(3);


        ActiveJob activeJob = new ActiveJob();

        activeJob.setActive(true);
        activeJob.setJob(job);

        Section section = new Section();
        section.setDepth(1);
        section.setDots(job.getA());

        activeJob.setSection(section);


        ActiveJob activeJob2 = new ActiveJob();

        activeJob2.setActive(true);
        activeJob2.setJob(job2);

        Section section2 = new Section();
        section2.setDepth(1);
        section2.setDots(job2.getA());

        activeJob2.setSection(section2);


        ActiveJob activeJob3 = new ActiveJob();

        activeJob3.setActive(true);
        activeJob3.setJob(job3);

        Section section3 = new Section();
        section3.setDepth(1);
        section3.setDots(job3.getA());

        activeJob3.setSection(section3);



        ActiveJob activeJob4 = new ActiveJob();

        activeJob4.setActive(true);
        activeJob4.setJob(job4);

        Section section4 = new Section();
        section4.setDepth(1);
        section4.setDots(job4.getA());

        activeJob4.setSection(section4);




        FractalWorker fractalWorker1 = new FractalWorker(activeJob,"");
        FractalWorker fractalWorker2 = new FractalWorker(activeJob2,"");
        FractalWorker fractalWorker3 = new FractalWorker(activeJob3,"");
        FractalWorker fractalWorker4 = new FractalWorker(activeJob4,"");

        fractalWorker1.run();
        fractalWorker2.run();
        fractalWorker3.run();
        fractalWorker4.run();

    }
}
