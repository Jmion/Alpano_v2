
package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;;

/**
 * This class creates the picture given a painter and the parameters of the panorama
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 * @deprecated Since Alapano V1.0. This class is deprecated because of the inefficiencies of how multithreaing was implemented. Replaced by {@link PanoramaRendererExecutionPool} 
 */
@Deprecated
public interface PanoramaRendererMultithread {
    /**
     * 
     * @param pano Parameters of the panorama. The information about the image to create.
     * @param imgPainter Painter of the image.
     * @return Image of the panorama
     */
    static Image renderPanorama(Panorama pano, ImagePainter imgPainter){
        int width = pano.parameters().width();
        int height = pano.parameters().height();
        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();
        
        /*Storing the number of threads that the system has for computation.
         * This value will depend on the machine that is executing the code
         */
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        //List to store the created threads. Initiated with the right size.
        List<Thread> threadList = new ArrayList<>(numberOfThreads);
        //The interval that each thread will treat.
        int xIntervalPerThread = width/numberOfThreads; 
        
        for(int i = 0; i < numberOfThreads; ++i){
            //Initializing the threds. Differentiating final call in case the width/numberOfThreads is a division with leftover.
            if(i<numberOfThreads-1)
                threadList.add(new PanoranaRendererThread(i*xIntervalPerThread, (i+1)*xIntervalPerThread, pw, imgPainter, height));
            /*The last thread to be created will take the remaining 
             * interval instead of the intervall that each thread threats.
             * This is to make sure that the entire image gets computed. 
             * If this wasn't done and the integer division to calculate
             * the xIntervalPerThread was not disivible by the 
             * numberOfThreads we would not calculate part of the image.
             */
            else
                threadList.add(new PanoranaRendererThread(i*xIntervalPerThread, width, pw, imgPainter, height));
            threadList.get(i).start();
        }
        try {
            //joining the threads. This action tells java to not continue until all of the threads are finished execution.
            for(Thread t : threadList)
                t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return img;
    }
    
    class PanoranaRendererThread extends Thread{
        private final int startx, endx, height;
        private ImagePainter imgPainter;
        private PixelWriter pw;
        
        public PanoranaRendererThread(int startx, int endx, PixelWriter pw, ImagePainter imgPainter, int height){
            this.startx = startx;
            this.endx = endx;
            this.imgPainter = imgPainter;
            this.height = height;
            this.pw = pw;
        }
        
        @Override
        public void run(){
            for(int x = startx; x < endx; ++x){
                for(int y = 0; y < height; ++y){
                    pw.setColor(x, y, imgPainter.colorAt(x, y));
                }
            }
        }
    }
}

