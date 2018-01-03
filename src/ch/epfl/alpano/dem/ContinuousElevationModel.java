package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.bilerp;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.GeoPoint;

/**
 * Continuous elevations models (CEM) is an extension of a discrete elevation model (DEM). A CEM allows for the interpolation of values between the discrete values
 * provided in a DEM. A CEM allows access to slope, and elevation at any point contained within the extent of the CEM.
 * <p>
 * CEM use a linear interpolation approach to calculate values of the CEM based of the data provided by the DEM
 * <p>
 * In the following doc "MNT" is used to refer to a 
 * 
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public final class ContinuousElevationModel {
    private DiscreteElevationModel dem;
    private static double d = toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN); 
    
    
    /**
     * Creates a CEM (continuous elevation model).
     * 
     * @param dem The DEM to be used to interpolate the CEM
     * @throws NullPointerException If the discrete elevation model if null.
     */
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = requireNonNull(dem);
    }
    
    
    /**
     * Give the altitude of a specific point using bilinear interpolation to calculate it.
     * 
     * @param p The point we want the altitude of.
     * @return The altitude in meters.
     */
    public double elevationAt(GeoPoint p){
        int x = (int)Math.floor(sampleIndex(p.longitude()));
        int y = (int)Math.floor(sampleIndex(p.latitude()));
        return bilerp(elevationOnIndexGiven(x,y), elevationOnIndexGiven(x+1, y), elevationOnIndexGiven(x, y+1), elevationOnIndexGiven(x+1,y+1), sampleIndex(p.longitude())-x, sampleIndex(p.latitude())-y);
    }
    
    
    /**
     * Gives the slope of a specific point using the bilinear interpolation
     * 
     * @param p The point which were want to know the slope.
     * @return The slope in radian.
     */
    public double slopeAt(GeoPoint p){
        int x = (int)Math.floor(sampleIndex(p.longitude()));
        int y = (int)Math.floor(sampleIndex(p.latitude()));
        return bilerp(giveSlopeOnIndexGiven(x, y), giveSlopeOnIndexGiven(x+1, y), giveSlopeOnIndexGiven(x, y+1), giveSlopeOnIndexGiven(x+1, y+1), sampleIndex(p.longitude()) - x, sampleIndex(p.latitude()) - y);
    }
    
    
    /**
     * Gives the slope of the extension of the discrete DEM.
     * 
     * @param x The first index of the point we want to know the slop.
     * @param y The second index of the point we want to know the slop.
     * @return The slope in radiant, it is a double.
     */
    private double giveSlopeOnIndexGiven(int x, int y){
        double deltaZa = elevationOnIndexGiven(x, y)-elevationOnIndexGiven(x+1, y);
        double deltaZb = elevationOnIndexGiven(x, y)-elevationOnIndexGiven(x, y+1);
        double teta = Math.acos(d/Math.sqrt(sq(deltaZa)+sq(deltaZb)+sq(d)));
        return teta;
    }
    
    
    /**
     * Gives the altitude of the extension of the discrete DEM.
     * 
     * @param x The first index of the point we want to know the altitude.
     * @param y The second index of the point we want to know the altitude.
     * @return The altitude in meters, in double.
     */
    private double elevationOnIndexGiven(int x, int y){
        if(dem.extent().contains(x, y))
            return dem.elevationSample(x,y);
        return 0.;
    } 
}
