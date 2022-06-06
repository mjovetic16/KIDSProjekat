package app.manager.worker;

import app.AppConfig;
import app.Cancellable;
import app.models.job.ActiveJob;
import app.models.job.Dot;
import app.models.job.Section;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FractalWorker implements Runnable, Cancellable {

    private ActiveJob activeJob;
    private String imagePath;

    private AtomicBoolean active = new AtomicBoolean(false);

    private ConcurrentLinkedQueue<Dot> filledDotList;

    public FractalWorker(ActiveJob activeJob, String imagePath) {
        this.activeJob = activeJob;
        this.imagePath = imagePath;
        this.filledDotList = new ConcurrentLinkedQueue<>();
    }

    public FractalWorker(){

    }


    @Override
    public void run() {

        active.set(true);

        Section section = activeJob.getSection();

        HashMap<String, Dot> dots = section.getDots();
        List<Dot> dotsList = dots.values().stream().toList();


        double p = activeJob.getJob().getP();

        Dot currentDot = new Dot();

        Random rand = new Random();


        currentDot.setX(dotsList.get(0).getX());
        currentDot.setY(dotsList.get(0).getY());


        int tempCounter = 0;
        while(active.get()){

            Dot randomDot = dotsList.get(rand.nextInt(dotsList.size()));

            // (1-l)A + (l)B    //l=distance
            int newX = (int) ((1-p)*randomDot.getX() + p*currentDot.getX());
            int newY = (int) ((1-p)*randomDot.getY() + p*currentDot.getY());

            currentDot.setX(newX);
            currentDot.setY(newY);

            filledDotList.add(currentDot.copy());



            if(tempCounter==200000)stop();
            tempCounter++;

        }

    }

    @Override
    public void stop() {
        active.set(false);
        try {


            testDraw(imagePath);


        } catch (Exception e) {
            errorLog("testDraw exception",e);
        }
    }


    public void testDraw(String path) throws Exception{

            BufferedImage image = new BufferedImage(activeJob.getJob().getW(), activeJob.getJob().getH(), BufferedImage.TYPE_INT_ARGB);



            final Graphics2D graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setPaint(Color.WHITE);
            graphics2D.fillRect(0, 0, activeJob.getJob().getW(), activeJob.getJob().getH());


            Color randColor = new Color((int)(Math.random() * 0x1000000));
            graphics2D.setPaint(randColor);
            for (Dot d : filledDotList) {
                graphics2D.drawOval(d.getX(), d.getY(), 1, 1);
            }


            graphics2D.dispose();


            try {
                ImageIO.write(image, "png", new File(path));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            AppConfig.timestampedStandardPrint("Done drawing: " + activeJob.getJob().getName() +" with dots:"+activeJob.getSection().getDots().values());

    }


    public void log(String s){

        AppConfig.timestampedStandardPrint("[Fractal Worker]: "+s);
    }

    public void errorLog(String s, Exception e){

        AppConfig.timestampedErrorPrint("[Fractal Worker]: "+s);
        AppConfig.timestampedErrorPrint("[Fractal Worker]: "+e.toString());
    }

}



//        //BLUE base dots
//        graphics2D.setPaint(Color.BLUE);
//        for(Dot d: activeJob.getJob().getA().values()){
//            graphics2D.drawOval ( d.getX(), d.getY(), 3, 3 );
//            graphics2D.drawString(d.toString(),d.getX(),d.getY());
//        }