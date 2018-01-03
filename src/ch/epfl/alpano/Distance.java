package ch.epfl.alpano;

/**
 * This class allows for the conversion of distances to angles, and angles to distances. 
 * All distances are measured on earth's surface. The angle is representative of the angle 
 * that would be measured at the center of the earth between the 2 points which the distance is measured. 
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public interface Distance {
    
    /**
     * The radius of the earth.
     */
    double EARTH_RADIUS = 6_371_000;
  
    
    /**
     * Converts the distance (measured on earths surface) between 2 points to the angle (radiant).
     * <p>
     * This angle can be measured as the angle between the 2 points in the perspective of the center of the earth.
     * This is the reverse operation of toMeters.
     * 
     * @param distanceInMeters  The most direct distance between two points on earth. Distance measured on earths surface.
     * @return The angle between the 2 points in the perspective of the center of the earth.
     */
    public static double toRadians (double distanceInMeters){
        return (distanceInMeters / EARTH_RADIUS);
    }
    
    
    /**
     * Converts an angle which is measured from the center of the earth between 2 points on earth's surface
     * to the distance that separates them. The distance is measured on earths surface (curve).
     * 
     * @param distanceInRadians Measure of the angle in radiant to be converted to a distance.
     * @return The distance that separates the 2 points on earth that have distanceInRadians between them.
     */
    public static double toMeters(double distanceInRadians){
       return (EARTH_RADIUS * distanceInRadians);  
    }
}
