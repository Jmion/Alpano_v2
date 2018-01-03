package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;


/**
 * A DiscreteElevationModel is a model that has information about elevations at discrete points. These points will be
 * called index's. This model only contains the elevation at hole indexes. An Index is a second of a degree. 
 * For elevation at floating point values see {@link ContinuousElevationModel}.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public interface DiscreteElevationModel {
    
    /**
     * Represents the number of sample per degrees of a MNT discrete. 
     */
    static int SAMPLES_PER_DEGREE = 3600;
    
    /**
     * Represents the number of sample per radian of a MNT discrete.
     */
    static double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * 360 / (2*Math.PI);
    
    
    /**
     * Converts an angle into it's index.
     * @param angle The angle in radian.
     * @return The index.
     */
    static double sampleIndex(double angle) {
        return angle*SAMPLES_PER_RADIAN;
    }
    
    
    /**
     * Gives the extend of the MNT.
     * @return The extent of the MNT.
     */
    abstract Interval2D extent();
   
    
    /**
     * Gives the elevation of a point using his coordinate x and y.
     * @param x The index on the x-axis.
     * @param y The index of the y-axis.
     * @return The altitude of the given index. Altitude is given in meters.
     * @throws IllegalArgumentException If the given indexes are outside of the range of the MNT.
     */
    abstract double elevationSample(int x, int y) throws IllegalArgumentException;
    
    
    /**
     * Creates an union that represents a discrete MNT.
     * @param that The discreteElevation that you want to make the union with.
     * @return The discrete MNT that represents the union of the two extend.
     * @throws IllegalArgumentException If you can not make an union of the two extends.
     */
    default DiscreteElevationModel union(DiscreteElevationModel that) throws IllegalArgumentException{
        Preconditions.checkArgument(this.extent().isUnionableWith(that.extent()), "The dem elevations models can't be unioned.");
        return new CompositeDiscreteElevationModel(this, that);
    }
    
}
