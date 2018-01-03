package ch.epfl.alpano;


import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.fromMath;
import static ch.epfl.alpano.Math2.haversin;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.util.Locale;


/**
 * GeoPoint represents a location on earth. 
 * <p>
 * This point is expressed in WGS 84 format. For the accuracy required in this project the WGS
 * coordinates are interpreted as points on a sphere. This is technically inaccurate since the
 * earth is a ellipsoidal shape. Since the deformation of the earth is quite small the 
 * inaccuracies that arise from the assumption that the earth is a sphere are negligible. 
 * The flattening of the earth at the poles is minimal, approximately 0.3%. This flattening will
 * not cause significant errors in the calculations.
 * <p>
 * A lot of online map services such as Google Maps, Bing Maps, do the same assumption.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class GeoPoint {
    private final double longitude, latitude;
    
    /**
     * Represents a point on the surface of the globe in a spherical coordinate system.
     * 
     * @param longitude angle in radian.
     * @param latitude angle in radian.
     * @throws IllegalArgumentException if longitude is not within [-pi ; pi ] and latitude not within [ -(pi/2) ; pi/2 ]
     */
    public GeoPoint(double longitude, double latitude){
        Preconditions.checkArgument(longitude >= -PI && longitude <= PI && latitude >= -(PI/2) && latitude <= PI/2);
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    
    /**
     * Returns the longitude.
     * 
     * @return longitude of this GeoPoint. Angle is in radian.
     * @see latitude
     */
    public double longitude(){
        return longitude;
    }
    
    
    /**
     * Returns the latitude.
     * 
     * @return latitude of this GeoPoint. Angle is in radian.
     * @see longitude 
     */
    public double latitude(){
        return latitude;
    }
    
    
    /**
     * Calculates the distance between 2 points on earth.
     * 
     * @param that Second point.
     * @return The distance between 2 points on the globe.
     */
    public double distanceTo(GeoPoint that){
        double cos1 = cos(latitude());
        double cos2 = cos(that.latitude());
        double havLong = haversin(longitude()-that.longitude());
        double havLat = haversin(latitude()-that.latitude());
        double a = 2*asin(sqrt(havLat+cos1*cos2*havLong));
        return Distance.toMeters(a);
    }
    
    
    /**
     * Calculates the angle (azimuth) in radian that somebody in "this" needs to look to be looking at that. 
     * 
     * @param that The point to where you want to look towards.
     * @return the Angle in radiant in the direction you need to look from this to look towards that. This angle is given in azimuth (clockwise from 0 to 2 pi)
     */
    public double azimuthTo(GeoPoint that){
        double nom = sin(longitude()-that.longitude())*cos(that.latitude());
        double denom = cos(latitude())*sin(that.latitude())-sin(latitude())*cos(that.latitude())*cos(longitude()-that.longitude());
        return fromMath(canonicalize(atan2(nom, denom)));
    }
    
    
    @Override
    public String toString(){
        Locale l = null;
        return String.format(l, "(%.4f,%.4f)", toDegrees(longitude()), toDegrees(latitude())); 
    }
    
    
}
