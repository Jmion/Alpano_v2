package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
/**
 * This class creates the picture given a painter and the parameters of the panorama
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class PanoramaRendererExecutionPool {
    private static DoubleProperty collumsCalculatedProperty = new SimpleDoubleProperty(); 
    private static int collumsCalculatedPerThread;
    private static AtomicInteger collumsCalculated= new AtomicInteger(0);

    
    /**
     * Renders the image for a given panorama. This is a multithreaded method designed for rapid rendering.
     * 
     * @param pano Parameters of the panorama. The information about the image to create.
     * @param imgPainter Painter of the image.
     * @return Image of the panorama
     */
    public static Image renderPanorama(Panorama pano, ImagePainter imgPainter){
//        resetStateOfExecution();      //redundant call to reset if implemented corectly. This is not a very costly operations so it is better to gurante that it is called now rather than never
        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        ArrayList<Callable<Void>> listCallable = new ArrayList<>();
        int width = pano.parameters().width();
        int height = pano.parameters().height();    
        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();
        collumsCalculatedPerThread = Math.max(width/70,1);
        
        //adding all but the last callable object. This is due to the integer division.
        for(int v = 0; v < width/collumsCalculatedPerThread ; ++v){
            final int w = v*collumsCalculatedPerThread;
            listCallable.add(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for(int x = w; x < w+collumsCalculatedPerThread; ++x){
                        for(int y = 0; y < height; ++y){
                            pw.setColor(x, y, imgPainter.colorAt(x, y));
                        }
                    }
                    collumsCalculated.addAndGet(collumsCalculatedPerThread);
                    updateExecutionStateProperty();
                    return null;
                }
            });
        }
        
        //adding the end of the render. By doing so it ensures that any part not rendered by the previous loop gets added now.
        listCallable.add(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                int w = (width/collumsCalculatedPerThread)*collumsCalculatedPerThread;
                
                for(int x = w; x < width; ++x){
                    for(int y = 0; y < height; ++y){
                        pw.setColor(x, y, imgPainter.colorAt(x, y));
                    }
                }
                collumsCalculated.addAndGet(width-w);
                updateExecutionStateProperty();
                return null;
            }
        });

        try {
            exec.invokeAll(listCallable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        exec.shutdown();
        try {
            exec.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return img;
        }
    /**
     * Ensures that the stateOfExecution is up to date with the number of collums calculated.
     */
    public static synchronized void updateExecutionStateProperty(){
        collumsCalculatedProperty.setValue(collumsCalculated);
    }
    
    /**
     * Retrives the number of collums that the render has calculated so far. 
     * The call to resetStateOf execution must be done if we are rendering multiple panorama to ensure that
     * any thread using stateOfExecution for progress displays the correct progress.
     * @return the number of collums that have been rendered.
     */
    public static DoubleProperty stateOfEecution(){
        return collumsCalculatedProperty;
    }
    /**
     * Resets the stateOfExecution by setting it back to 0. This puts the number of collums calculated back to 0.
     */
    protected static void resetStateOfExecution(){
        collumsCalculated.set(0);
        updateExecutionStateProperty();
    }
}
