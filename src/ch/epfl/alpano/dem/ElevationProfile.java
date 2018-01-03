

package ch.epfl.alpano.dem;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.scalb;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;

/*
 * The &lt; represents the "<" symbol. The &gt; represents the ">" symbol.
 */

/**
 * Creates an elevation profile which the precise value for every 4096 meters and does an interpolation 
 * for the others points. Hence, we can get any elevation, slope, or GeoPoint of any point 
 * in the elevation profile.
 * <p>
 * Mathematically an elevation profile is a continuous function. It will represent the elevation 
 * along the great circle in a given direction.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public final class ElevationProfile  {
    
    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth, length;
    private final GeoPoint[] tab;
    private final static int scaleFactor = -12;
    
    
    /**
     * Builds a altimetric profile based on the continuous elevation model starting from the origin,
     * , and that follows the great circle in the direction of the of the azimuth. This profile will have
     * will have a given length.
     * 
     * @param elevationModel The elevation model that contains the information about the elevation.
     * @param origin The starting point of the profile.
     * @param azimuth The direction in which the profile is made.
     * @param length The length of the profile.
     * @throws IllegalArgumentException If the azimuth angle is not canonical.
     * @throws IllegalArgumentException If length is smaller than or equal to 0.
     * @throws NullPointerException If the elevationModel or the origin is null.
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){
        Preconditions.checkArgument(length > 0);
        Preconditions.checkArgument(Azimuth.isCanonical(azimuth));
        this.elevationModel = requireNonNull(elevationModel);
        this.origin = requireNonNull(origin);
        this.azimuth = azimuth;
        this.length = length;  
        tab = createGeoPointArr();
    }
    
    
    /**
     * Altitude at given position.
     * 
     * @param x The distance from origin.
     * @return Altitude at position x.
     * @throws IllegalArgumentException If x &lt; 0 or x &gt; length. 
     */
    public double elevationAt(double x){
        isIn(x);
        return elevationModel.elevationAt((positionAt(x)));
    }
    
    
    /**
     * Creates a GeoPoint that is located at a distance x of the origin.
     * 
     * @param x The distance from the origin.
     * @return GeoPoint The GeoPoint (longitude and latitude) of the point at a distance x.
     * @throws IllegalArgumentException If x &lt; 0 or x &gt; length. 
     */
    public GeoPoint positionAt(double x){
        isIn(x);
        int index = (int) Math.floor(scalb(x, scaleFactor));
        int indexPlus = index+1;
        if(indexPlus >= tab.length){
            return tab[index];
        }
        double phi0 = tab[index].latitude();
        double lambda0  = tab[index].longitude();
        double phi1 = tab[indexPlus].latitude();
        double lambda1 = tab[indexPlus].longitude();
        double lambLerp = Math2.lerp(lambda0, lambda1, scalb(x, scaleFactor)-index);
        double phiLerp = Math2.lerp(phi0, phi1, scalb(x, scaleFactor)-index);
       
        return new GeoPoint(lambLerp, phiLerp);
    }
    
    
    /**
     * The slope of the ground at the point at distance x.
     * 
     * @param x The distance along the great circle.
     * @return The slope of the terrain.
     * @throws IllegalArgumentException If x &lt; 0 or x &gt; length. 
     */
    public double slopeAt(double x){
        isIn(x);
        return elevationModel.slopeAt(positionAt(x));
    }
    
    
    /**
     * Checks that the x is contained within the elevation profile.
     * 
     * @param x The distance from the origin.
     * @throws IllegalArgumentException If x &lt; 0 or x &gt; length throws exception. 
     */
    private void isIn(double x){
        Preconditions.checkArgument(x >= 0,"The length can not be negative.");
        Preconditions.checkArgument(x <= length, "The length if noo high compare to the length of our ElevationProfile.");
    }
    
    
    /**
     * Creates an array of geopoints. 
     * <p>
     * The GeoPoints are spaced out every 4096 meters along the great circle. This 
     * method will yield an array of geopoints along a great circle that are separated by 4096m between each other.
     * 
     * @return The array that has been filled.
     */
    private GeoPoint[] createGeoPointArr(){
        GeoPoint[] geoArr = new GeoPoint[(int) (Math.ceil(scalb(length, scaleFactor))+1)];
        double phi;
        double lambda; 
        double phi0 = origin.latitude();
        double lambda0 = origin.longitude();
        double alpha = Azimuth.toMath(azimuth);
        double x;
        geoArr[0] = origin;
        for(int i = 1; i < geoArr.length; i++){
            x = Distance.toRadians(scalb(i,12));
            phi = asin(sin(phi0)*cos(x)+cos(phi0)*sin(x)*cos(alpha));
            lambda = Math2.floorMod((lambda0-asin((sin(alpha)*sin(x))/cos(phi))+PI), 2*PI)-PI;
            geoArr[i] = new GeoPoint(lambda, phi);
        }
        return geoArr;
    } 
}
