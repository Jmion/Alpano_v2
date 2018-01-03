package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * A CompositeDiscreteElevationModel is a DEM that is the union of 2 DEM's. The purpose of this class is to
 * be able to create a DEM's that can cover shapes that might not be expressed in one DEM. 
 * <p>
 * For example if we had the Alps of Switzerland in 2 files one being the Alps in the Valais and the other the 
 * Alps in the Grison we can use this class to create a unique DEM that covers both teh Valais and the Grison.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
    private final DiscreteElevationModel dem1, dem2;
    private final Interval2D extentInterval;
    
    /**
     * Builds a DEM that represents the union between dem1 and dem2.
     * 
     * @param dem1 The discrete elevation model 1.
     * @param dem2 The discrete elevation model 2.
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        extentInterval = dem1.extent().union(dem2.extent());
     }

    @Override
    public Interval2D extent() {
            return extentInterval;
    }

    @Override
    public double elevationSample(int x, int y) throws IllegalArgumentException{
        Preconditions.checkArgument(extentInterval.contains(x, y), "Index not found in composite function.");
        if(dem1.extent().contains(x, y)){
            return dem1.elevationSample(x, y);
        }
        return dem2.elevationSample(x, y);
    }
}
