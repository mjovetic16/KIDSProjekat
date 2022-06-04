package servent.workers;

import app.AppConfig;
import app.Cancellable;
import app.models.ActiveJob;
import app.models.Dot;
import app.models.Section;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            int newX = (int) ((1-p)*currentDot.getX() + p*randomDot.getX());
            int newY = (int) ((1-p)*currentDot.getY() + p*randomDot.getY());

            currentDot.setX(newX);
            currentDot.setY(newY);

            filledDotList.add(currentDot.copy());



            if(tempCounter==100000)stop();
            tempCounter++;

        }

    }

    @Override
    public void stop() {
        active.set(false);
        testDraw();
    }


    public void testDraw(){
        final BufferedImage image = new BufferedImage ( 1920, 1080, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D graphics2D = image.createGraphics ();
        graphics2D.setPaint ( Color.WHITE );
        graphics2D.fillRect ( 0,0,1920,1080 );
        graphics2D.setPaint ( Color.RED );
        for(Dot d:filledDotList){
            graphics2D.drawRect ( d.getX(), d.getY(), 1, 1 );
        }

        graphics2D.dispose ();

        try {
            ImageIO.write ( image, "png", new File( "fractal/images/"+activeJob.getJob().getName()+".png" ) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AppConfig.timestampedStandardPrint("Done drawing");

    }


}