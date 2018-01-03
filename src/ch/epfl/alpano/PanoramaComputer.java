package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.improveRoot;
import static ch.epfl.alpano.Math2.sq;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
/**
 * This is a class that is used to calculate all the fields of a panorama.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */


public final class PanoramaComputer {
    
    private final ContinuousElevationModel dem;
    private static final double K = 0.13;
    private static final double EPSILON= 64;
    private static final double DX = 4;
    private static final double rayToGroundDistance = (1-K)/(2*Distance.EARTH_RADIUS);

    /**
     * Creates a panormaComputer.
     * 
     * @param dem The DEM model that will be used for calculating the panorama.
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
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
        Panorama.Builder panoB = new Panorama.Builder(parameters);
        for(int x = 0; x < parameters.width(); ++x){
            ElevationProfile p = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x), parameters.maxDistance());
            double distanceX=0;
            for(int y = parameters.height()-1 ; y >= 0 && distanceX <= parameters.maxDistance(); --y){
                double altitudeY = (float)parameters.altitudeForY(y);
                DoubleUnaryOperator f = rayToGroundDistance(p, parameters.observerElevation(), tan(altitudeY));
                distanceX = Math2.firstIntervalContainingRoot(f, distanceX, parameters.maxDistance(), EPSILON);
                if(distanceX != Double.POSITIVE_INFINITY){
                    distanceX = improveRoot(f, distanceX, distanceX+EPSILON, DX);
                    GeoPoint geo = p.positionAt(distanceX);
                    panoB.setDistanceAt(x, y, (float) (distanceX/cos(altitudeY)))
                        .setElevationAt(x, y, (float)(p.elevationAt(distanceX)))
                        .setSlopeAt(x, y, (float)p.slopeAt(distanceX))
                        .setLatitudeAt(x, y,(float) geo.latitude() )
                        .setLongitudeAt(x, y, (float) geo.longitude());
                }
            }
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


}

