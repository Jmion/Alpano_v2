package ch.epfl.alpano.gui;

/**
 * The enum represents the limits of the userParameter.
 * <p>
 * It will offer only one method that will allow the parameter passed to it to be
 * <i> sanitized </i>. When called it will ensure that the value returned is the closest
 * valid value possible.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public enum UserParameter {
    /**
     * Longitude of the observer. Must be contained 6° and 12°.
     */
    OBSERVER_LONGITUDE(60_000, 120_000), 
    
    /**
     * Latitude of the observer. Must be contained between 45° and 48°.
     */
    OBSERVER_LATITUDE(450_000, 480_000),
    
    /**
     * Elevation of the observer. Must be contained between 300m and 10_000m
     */
    OBSERVER_ELEVATION(300, 10_000),
    
    /**
     * Center Azimuth. Must is between 0° and 359°.
     */
    CENTER_AZIMUTH(0, 359),
    
    /**
     * Horizontal field of view of the image. Between 1° and 360°.
     */
    HORIZONTAL_FIELD_OF_VIEW(1, 360), 
    
    /**
     * Maximum viewing distance. Between 10km and 600km.
     */
    MAX_DISTANCE(10, 600), 
    
    /**
     * Width in pixels of the image. Between 30 pixels and 16_000 pixels.
     */
    WIDTH(30, 16_000),
    
    /**
     * Height in pixels of the image. Between 10 pixels and 4_000 pixels.
     */
    HEIGHT(10, 4000),
    
    /**
     * SuperSampling ratio. Must be either 0, 1, or 2.
     */
    SUPER_SAMPLING_EXPONENT(0, 2);
    
    private int min, max;
    
    /**
     * Initializes the enum constants.
     * 
     * @param min Value of the minimum of enum constante.
     * @param max Value of the maximum of the enum constante.
     */
    private UserParameter(int min, int max){
        this.min = min;
        this.max = max;
    }
    
    /**
     * Will return a value that is within the limits of the computational data that we have available for the panorama.
     * 
     * @param v The value corresponding to the parameter.
     * @return The closest valid value.
     */
    public int sanitize(int v){
        if(v>max)
            return max;
        else if(v<min)
            return min;
        return v;
    }

}
