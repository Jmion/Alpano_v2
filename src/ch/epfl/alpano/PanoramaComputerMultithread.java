package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.improveRoot;
import static ch.epfl.alpano.Math2.sq;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

/**
 * This is a class that is used to calculate all the fields of a panorama. This class will use multiple threads
 * to calcualte the panorama.
 * @author Jeremy (261178)
 * @deprecated Since Alapano V1.0. This class is deprecated because of the inefficiencies of how multithreaing was implemented. Replaced by {@link PanoramaComputerExecutionPool} 
 */
@Deprecated
public final class PanoramaComputerMultithread {

    private final ContinuousElevationModel dem;
    private static final double K = 0.13;
    private static final double epsilon= 64;
    private static final double dx = 4;
    private static final double rayToGroundDistance = (1-K)/(2*Distance.EARTH_RADIUS);

    /**
     * Creates a panormaComputer.
     * 
     * @param dem The DEM model that will be used for calculating the panorama.
     */
    public PanoramaComputerMultithread(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }


    /**
     * Calculates the slope, distance, elevation, slope, longitude, latitude for each sample (pixel).
     * <p> 
     * For large panorama this method can take some time to complete since it is very computationally demanding.
     * <p>
     * This methode manages the thredas that are doing the computation.
     * 
     * @param parameters The parameters of the panorama.
     * @return The panorama with all the fields filled in accordingly to the data provided by the dem.
     */
    public Panorama computePanorama(PanoramaParameters parameters) {

        System.out.println("Computing with "+Runtime.getRuntime().availableProcessors()+" threads.");

        Panorama.Builder panoB = new Panorama.Builder(parameters);

        /*Storing the number of threads that the system has for computation.
         * This value will depend on the machine that is executing the code
         */
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        //List to store the created threads. Initiated with the right size.
        List<Thread> threadList = new ArrayList<>(numberOfThreads);
        //The interval that each thread will treat.
        int xIntervalPerThread = parameters.width()/numberOfThreads;        

//        ExecutorService executor = new Executor.newFixedThreadPool
        
        // Launching all the threads.
        for(int i = 0; i < numberOfThreads; ++i){
            if(i<numberOfThreads-1)
                threadList.add(new PanoramaComputerThread(dem, parameters, xIntervalPerThread*i, xIntervalPerThread*(i+1), panoB));
            /*The last thread to be created will take the remaining interval instead of the intervall that each thread threats.
             * This is to make sure that the entire image gets computed. If this wasn't done and the integer division to calculate
             * the xIntervalPerThread was not disivible by the numberOfThreads we would not calculate part of the image.
             */
            else
                threadList.add(new PanoramaComputerThread(dem, parameters, xIntervalPerThread*i, parameters.width(), panoB));
            
            //starting the threads
            threadList.get(i).start();
        }
        try {
            //joining the threads. This action tells java to not continue until all of the threads are finished execution.
            for(Thread t : threadList)
                t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //returns the panorama.
        return panoB.build();
    }
    
    /**
     * Represents the distance between the ground and the ray.
     * <p>
     * The mathematical formula used to calculate the distance between the ground and the ray takes into account atmospheric retraction.
     * The atmospheric refraction has for effect of making visible objects that wouldn't normally be visible.
     *  It also takes into account that the earth is round. 
     * 
     * @param profile Elevation profile of the ground
     * @param ray0 Initial altitude of the ray.
     * @param raySlope The tangent of the slope of the ray.
     * @return The distance between the elevation profile and the ray for a given x.
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        return x -> (ray0+x*raySlope-profile.elevationAt(x)+rayToGroundDistance*sq(x));
    }
    
    /**
     * This class allows for the instantiation of threads. These threads are to be used 
     * for the purpose of calculating the Panorama.
     * <p>
     * This class implements the concept of multithreading by extending threads.
     * <p>
     * Each thread will calculate a part of the overall panorama and then write it to the arrays
     * that contain all the information for the entire panorama. There is no need for syncronisation 
     * between threads since the information that is being stored/calculated does not depend on the output
     * of another thread.
     * 
     * @author Jeremy (261178)
     *
     */
    private final class PanoramaComputerThread extends Thread {

        private ContinuousElevationModel dem;
        private PanoramaParameters parameters;
        private int startx, endx;
        private Panorama.Builder panoB;
        
        /**
         * Creates a thread that can be used to calculate a panorama.
         * 
         * @param dem The terrain model that is being used to generate the image
         * @param parameters The parameters of the panorama that is being calculated.
         * @param startx The position in the x-axis where this thread is going to start its calculation.
         * @param endx The position in the x-axis where this thread is going to end its calculation.
         * @param panoB The reference to the panorama where the calculated values will be stored.
         */
        public PanoramaComputerThread(ContinuousElevationModel dem, PanoramaParameters parameters, int startx, int endx, Panorama.Builder panoB){
            this.dem = dem;
            this.parameters = parameters;
            this.startx = startx;
            this.endx = endx;
            this.panoB = panoB;
        }


        @Override
        public void run(){
            for(int x = startx; x < endx; ++x){
                ElevationProfile p = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x), parameters.maxDistance());
                double distanceX=0;
                for(int y = parameters.height()-1 ; y >= 0 && distanceX <= parameters.maxDistance(); --y){
                    double altitudeY = (float)parameters.altitudeForY(y);
                    DoubleUnaryOperator f = rayToGroundDistance(p, parameters.observerElevation(), tan(altitudeY));
                    distanceX = Math2.firstIntervalContainingRoot(f, distanceX, parameters.maxDistance(), epsilon);
                    if(distanceX != Double.POSITIVE_INFINITY){
                        distanceX = improveRoot(f, distanceX, distanceX+epsilon, dx);
                        GeoPoint geo = p.positionAt(distanceX);
                        panoB.setDistanceAt(x, y, (float) (distanceX/cos(altitudeY)))
                        .setElevationAt(x, y, (float)(p.elevationAt(distanceX)))
                        .setSlopeAt(x, y, (float)p.slopeAt(distanceX))
                        .setLatitudeAt(x, y,(float) geo.latitude() )
                        .setLongitudeAt(x, y, (float) geo.longitude());
                    }
                }
            }
        }
    }
}


