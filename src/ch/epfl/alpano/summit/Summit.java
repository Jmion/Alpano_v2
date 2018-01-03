

package ch.epfl.alpano.summit;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.GeoPoint;
/**
 * Represents a summit with a name, a position and an elevation. 
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class Summit {
    
    private final String name;
    private final GeoPoint position;
    private final int elevation;
    /**
     * Create a summit with a name, a position and an elevation. 
     * @param name The name of the summit.
     * @param position The position of the summit.
     * @param elevation The elevation of the summit in meters.
     */
    public Summit(String name, GeoPoint position, int elevation){
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }
    
    
    /**
     * Retrieves the name of the summit.
     * @return The name.
     */
    public String name(){
        return name;
    }
    
    
    /**
     * Retrieves the elevation in meters of the summit.
     * @return The elevation in meters.
     */
    public int elevation(){
        return elevation;
    }
    
    
    /**
     * Retrieves the GeoPoint of the summit.
     * @return The GeoPoint of the summit.
     */
    public GeoPoint position(){
        return position;
    }
    
    
    @Override
    public String toString(){
        return (name + " " + position + " " + elevation);
    }
    
}
