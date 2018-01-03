package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.improveRoot;
import static ch.epfl.alpano.Math2.sq;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class PanoramaComputerExecutionPool {

    
    private final ContinuousElevationModel dem;
    private static final double K = 0.13;
    private static final double epsilon= 64;
    private static final double dx = 4;
    private static final double rayToGroundDistance = (1-K)/(2*Distance.EARTH_RADIUS);
    private AtomicInteger collumsCalculated= new AtomicInteger(0);
    private DoubleProperty collumsCalculatedProperty = new SimpleDoubleProperty(); 
    private int collumsCalculatedPerThread;
    private Panorama.Builder panoB;
    private PanoramaParameters parameters;
    
    /**
     * Creates a panormaComputer.
     * 
     * @param dem The DEM model that will be used for calculating the panorama.
     */
    public PanoramaComputerExecutionPool(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }


    /**
     * Calculates the slope, distance, elevation, slope, longitude, latitude for each sample (pixel).
     * <p> 
     * For large panorama this method can take some time to complete since it is very computationally demanding.
     * 
     * @param parameters The parameters of the panorama.
     * @return The panorama with all the fields filled in accordingly to the data provided by the dem.
     */
    public Panorama computePanorama(PanoramaParameters parameters){
        //reseting the couter for a new calculation.
        collumsCalculated.set(0);
        collumsCalculatedPerThread = Math.max(parameters.width()/300, 1);
        
        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ArrayList<Callable<Void>> listCallable = new ArrayList<>();
        panoB = new Panorama.Builder(parameters);
        this.parameters = parameters;
        
        for(int v = 0; v < parameters.width()/collumsCalculatedPerThread ; ++v){
            final int w = v*collumsCalculatedPerThread;
            listCallable.add(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    
                    for(int x = w; x < w+collumsCalculatedPerThread; ++x){
                        calculateCollumn(x);
                    }
                    collumsCalculated.addAndGet(collumsCalculatedPerThread);
                    updateExecutionStateProperty();
                    return null;
                }
            });
        }

        /*adding the end of the panorama. The size of the panorama that remains to be calculated 
         * will depend on the how much is left after the integer division of the width by the number of collums
         * calculated per thread.
         */
        listCallable.add(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                int w = (parameters.width()/collumsCalculatedPerThread)*collumsCalculatedPerThread;
                
                for(int x = w; x < parameters.width(); ++x){
                    calculateCollumn(x);
                }
                collumsCalculated.addAndGet(parameters.width()-w);
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
     * Retrives the curernt number of collums of pixels that have been treated.
     * @return Number of collums that have already been calculated. Will be smaller or equal to width of Panorama.
     */
    public DoubleProperty stateOfEecution(){
        return collumsCalculatedProperty;
    }

    /**
     * Ensures that the cullumyCalculatedProperty is up to date with the atomic integer collumsCalculated
     */
    public synchronized void updateExecutionStateProperty(){
        collumsCalculatedProperty.setValue(collumsCalculated);
    }
    
    /**
     * Calculates and fills the panorama builder for a specific collum of the panorama.
     * @param x the collume that will be rendered
     */
    private void calculateCollumn(int x){
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
