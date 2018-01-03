

package ch.epfl.alpano;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
/**
 * A Panorma contains information about samples measured from the continuous models used. A panorama doesn't represent an image.
 * A Panorama is 5 arrays that contain the following information: <b> distance, longitude, latitude, elevation, slope </b>. Each of these 
 * parameters are calculated for every sample.
 * <p>
 * A sample is taken for every single pixel of the image that we want to create.
 * <p> 
 * The information that is contained within a Panorama can be used to create an image but this class isn't an image.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class Panorama {
    
    private final PanoramaParameters myPan;
    private final float[] distance, longitude, latitude, elevation, slope;
    
    
    /**
     * This class represents a panorama.
     * 
     * @param myPan The parameters of the panorama.
     * @param distance Distances from the point of view to each pixel.
     * @param longitude Longitude of each pixel of the picture.
     * @param latitude Latitude of each pixel of the picture.
     * @param elevation Elevation of each pixel of the picture.
     * @param slope Slope of each pixel of the picture.
     */
    private Panorama(PanoramaParameters myPan, float[] distance, float[] longitude,float[] latitude, float[] elevation, float[] slope){
        this.myPan = myPan;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.slope = slope;
    }
    
    
    /**
     * Returns the parameters of the panorama.
     * 
     * @return the PanoramaParameters in question.
     */
    public PanoramaParameters parameters(){
        return myPan;
    }
    
    
    /**
     * Returns the distance from the point of view of a specific pixel of the picture.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @return The distance
     * @throws IndexOutOfBoundsException If the index is not valid.
     */
    public float distanceAt(int x, int y){
        checkValideSampleIndex(x, y);
        return distance[myPan.linearSampleIndex(x, y)];
    }
    
    
    /**
     * Returns the distance from the point of view of a specific pixel of the picture.
     * If the index is not valid, it returns the default value.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @param d The default value to return if the index is not correct.
     * @return The distance
     */
    public float distanceAt(int x, int y, float d){
        if(myPan.isValidSampleIndex(x, y))
            return distance[myPan.linearSampleIndex(x, y)];
        return d;
    }
    
    
    /**
     * Returns the longitude of a specific pixel of the picture.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @return The longitude
     * @throws IndexOutOfBoundsException If the index is not valid.
     */
    public float longitudeAt(int x, int y){
        checkValideSampleIndex(x, y);
        return longitude[myPan.linearSampleIndex(x, y)];
    }
    
    
    /**
     * Returns the latitude of a specific pixel of the picture.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @return The latitude.
     * @throws IndexOutOfBoundsException If the index is not valid.
     */
    public float latitudeAt(int x, int y){
        checkValideSampleIndex(x, y);
        return latitude[myPan.linearSampleIndex(x, y)];
    }
    
    
    /**
     * Returns the elevation of a specific pixel of the picture.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @return The elevation.
     * @throws IndexOutOfBoundsException If the index is not valid.
     */
    public float elevationAt(int x, int y){
        checkValideSampleIndex(x, y);
        return elevation[myPan.linearSampleIndex(x, y)];
    }
    
    
    /**
     * Returns the slope of a specific pixel of the picture.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @return The slope.
     * @throws IndexOutOfBoundsException If the index is not valid.
     */
    public float slopeAt(int x, int y){
        checkValideSampleIndex(x, y);
        return slope[myPan.linearSampleIndex(x, y)];
    }
    
    
    /**
     * Checks that the sample index is valid. 
     * <p> 
     * A valid index is one that is contained within the panorama.
     * 
     * @param x The horizontal coordinate of the pixel.
     * @param y The vertical coordinate of the pixel.
     * @throws IndexOutOfBoundsException if the index is not valid.
     */
    private void checkValideSampleIndex(int x, int y){
        if(!(myPan.isValidSampleIndex(x, y)))
            throw new IndexOutOfBoundsException("Coordinate are not valid!");
    }
    
    
    
    public final static class Builder{
        
        private final PanoramaParameters myPanBuild;
        private float[] distanceBuild, longitudeBuild,latitudeBuild,elevationBuild,slopeBuild;
        private boolean hasBeenBuild = false;
        
        
        /**
         * Creates a panorama builder.
         * All the arrays are filled with zero except for the distance with is fill with positive infinity.
         * <p>
         * The default values correspond to sky in the picture.
         * 
         * @param myPanBuild The panorama we take the informations from.
         * @throws NullPointerException if the panoramaParameters is null.
         */
        public Builder(PanoramaParameters myPanBuild){
            requireNonNull(myPanBuild);
            this.myPanBuild = myPanBuild;
            final int size = myPanBuild.width() * myPanBuild.height();
            distanceBuild = new float[size];
            longitudeBuild = new float[size];
            latitudeBuild = new float[size];
            elevationBuild = new float[size];
            slopeBuild = new float[size];
            Arrays.fill(distanceBuild, Float.POSITIVE_INFINITY);
        }
        
        
        /**
         * Sets the distance of a specific pixel.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @param distance  the distance from the point of view of the specific pixel.
         * @return The reference to this object.
         * @throws IllegalStateException If the panorama has already been built.
         * @throws IndexOutOfBoundsException If the index of the pixel is incorrect.
         */
        public Builder setDistanceAt(int x, int y, float distance){
            checkNotBuildAndValideSampleIndex(x, y);
            distanceBuild[myPanBuild.linearSampleIndex(x, y)] = distance;
            return this;
        }
        
        
        /**
         * Sets the longitude of a specific pixel.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @param longitude The longitude from the point of view of the specific pixel.
         * @return The reference to this object.
         * @throws IllegalStateException If the panorama has already been built.
         * @throws IndexOutOfBoundsException If the index of the pixel is incorrect.
         */
        public Builder setLongitudeAt(int x, int y, float longitude){
             checkNotBuildAndValideSampleIndex(x, y);
             longitudeBuild[myPanBuild.linearSampleIndex(x, y)] = longitude;
             return this;
         }
        
        
        /**
         * Sets the latitude of a specific pixel.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @param latitude The latitude from the point of view of the specific pixel.
         * @return The reference to this object.
         * @throws IllegalStateException If the panorama has already been built.
         * @throws IndexOutOfBoundsException If the index of the pixel is incorrect.
         */
        public Builder setLatitudeAt(int x, int y, float latitude){
             checkNotBuildAndValideSampleIndex(x, y);
             latitudeBuild[myPanBuild.linearSampleIndex(x, y)] = latitude;
             return this;
        }
        
        
        /**
         * Sets the elevation of a specific pixel.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @param elevation The elevation from the point of view of the specific pixel.
         * @return The reference to this object.
         * @throws IllegalStateException If the panorama has already been built.
         * @throws IndexOutOfBoundsException If the index of the pixel is incorrect.
         */
        public Builder setElevationAt(int x, int y, float elevation){
             checkNotBuildAndValideSampleIndex(x, y);
             elevationBuild[myPanBuild.linearSampleIndex(x, y)] = elevation;
             return this;
        }
        
        
        /**
         * Sets the slope of a specific pixel.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @param slope The slope from the point of view of the specific pixel.
         * @return The reference to this object.
         * @throws IllegalStateException If the panorama has already been built.
         * @throws IndexOutOfBoundsException If the index of the pixel is incorrect.
         */
        public Builder setSlopeAt(int x, int y, float slope){
            checkNotBuildAndValideSampleIndex(x, y);
            slopeBuild[myPanBuild.linearSampleIndex(x, y)] = slope;
            return this;
        }
        
        
        /**
         * Creates the final panorama. It can be only build once.
         * 
         * @return the panorama built.
         * @throws IllegalStateException if the the panorama has already been built.
         */
        public Panorama build(){
            if(hasBeenBuild)
                throw new IllegalStateException();
            hasBeenBuild =true;
            Panorama p =new Panorama(myPanBuild, distanceBuild, longitudeBuild, latitudeBuild, elevationBuild, slopeBuild);
            distanceBuild = null;
            longitudeBuild = null;
            latitudeBuild = null;
            elevationBuild = null;
            slopeBuild = null;
            return p; 
        }
        
        
        /**
         * Check that the panorama has not been build yet and that the coordinates of the pixel are valid.
         * 
         * @param x The horizontal coordinate of the pixel.
         * @param y The vertical coordinate of the pixel.
         * @throws IllegalStateException if the panorama has already been build.
         * @throws IndexOutOfBoundsException if the coordinates of the pixel are not valid.
         */
        private void checkNotBuildAndValideSampleIndex(int x, int y) {
            if(hasBeenBuild)
                throw new IllegalStateException();
             if(!myPanBuild.isValidSampleIndex(x, y))
                 throw new IndexOutOfBoundsException("Coordinate are not valid!");
        } 
    }
}
