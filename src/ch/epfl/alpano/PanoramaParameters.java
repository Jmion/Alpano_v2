package ch.epfl.alpano;

import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
/**
 * This class stores the information about the panorama that we want to generate. It stores the <b> observers elevation, max viewing distance, width, height, the center azimuth
 * (center direction the panorama), horizontal field of view, DELTA. </b>
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */


public final class PanoramaParameters {
    private final GeoPoint observerPosition;
    private final int observerElevation, maxDistance, width, height;
    private final double centerAzimuth, horizontalFieldOfView, DELTA, verticalFieldOfView;
    
    
    /**
     * Build an instance of PanoramaParameter.
     * 
     * @param observerPosition location from where we want the panorama to be generated.
     * @param observerElevation altitude of the the observer.
     * @param centerAzimuth direction in which the center of the panorama looks in.
     * @param horizontalFieldOfView The angle than we want to be able to observe. (ex. 
     * @param maxDistance the maximum distance that the panorama needs to be generated
     * @param width in pixels of the picture to generate.
     * @param height in pixels of the picture to generate.
     * @throws NullPointerException if observationPosition is null.
     * @throws IllegalArgumentException If centerAzimuth is not canonical.
     * @throws IllegalArgumentException If horizontalFieldOfView is not contained in ]0,2pi].
     * @throws IllegalArgumentException If length, width, or distance are not strictly positive
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView,
            int maxDistance, int width, int height){
        checkArgument(isCanonical(centerAzimuth), "centerAzimuth angle is not canonical.");
        checkArgument( horizontalFieldOfView > 0 && horizontalFieldOfView <= PI2, "horizontal filed of view not contained within 0 (exluded) to 2PI (included)");
        checkArgument(maxDistance > 0, "maxDistance smaller than 0");
        checkArgument(width > 0, "Width is not strictly bigger than 0");
        checkArgument(height > 0, "Height is not strictly bigger than 0");
        this.observerPosition = requireNonNull(observerPosition);
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.verticalFieldOfView = this.horizontalFieldOfView*(this.height-1d)/(this.width-1);
        this.DELTA = horizontalFieldOfView()/(width()-1);
    }
    
    
    /**
     * Retrieves the position of the observer. As a GeoPoint.
     * 
     * @return Position of the observer.
     * @see GeoPoint
     */
    public GeoPoint observerPosition(){
        return observerPosition;
    }
    
    
    /**
     * Retrieves the elevation of the observer. In meters.
     * 
     * @return Elevation of the observer.
     */
    public int observerElevation(){
        return observerElevation;
    }
    
    
    /**
     * Retrieves the maximum viewing distance of the panorama. Further than this distance we cannot see. In meters.
     * 
     * @return max viewing distance.
     */
    public int maxDistance(){
        return maxDistance;
    }
    
    
    /**
     * Retrieves the width of the panorama. In pixels.
     * 
     * @return width of the panorama to generate.
     */
    public int width(){
        return width;
    }
    
    
    /**
     * Retrieves the height of the panorama. In pixels
     * 
     * @return height of the panorana to generate.
     */
    public int height(){
        return height;
    }
    
    
    /**
     * Retrieves the azimuth of the center of the panorama. Angle in radiant
     * 
     * @return Azimuth of the center of the panorama. This is the main direction that the observer is looking in
     */
    public double centerAzimuth(){
        return centerAzimuth;
    }
    
    
    /**
     * Retrieves the horizontal filed of view of the panorama. Angle in radiant.
     * 
     * @return horizontal angle that the panorama covers.
     */
    public double horizontalFieldOfView(){
        return horizontalFieldOfView;
    }
    
    
    /**
     * Retrieves the vertical filed of view of the panorama. Angle in radiant
     * 
     * @return vertical angle that the panorama covers
     */
    public double verticalFieldOfView(){
        return verticalFieldOfView;
    }
    
    
    /**
     * Calculates the canonical azimuth corresponding to the index of the pixel (horizontal).
     * 
     * @param x the pixel that will be used to calculate the azimuth of.
     * @return azimuth of the pixel x. azimuth in canonicalized.
     * @throws IllegalArgumentException If x is not contained within [0, width-1]
     */
    public double azimuthForX(double x){
        checkArgument(x>=0 && x <= (width-1), "Value of x is not within the required range.");
        double pixelFromCenter = x-((width()-1)/2d) ;
        return Azimuth.canonicalize(centerAzimuth()+( pixelFromCenter*DELTA));
    }
    
    
    /**
     * Calculates the index of the pixel given an azimuth angle (horizontal).
     * 
     * @param a azimuth angle.
     * @return index of the pixel at the given angle. (horizontal)
     * @throws IllegalArgumentException If the angle "a" is not within the filed of view of the panorama.
     */
    public double xForAzimuth(double a){
        double angleFromCenter = Math2.angularDistance(centerAzimuth, a);
        checkArgument(Math.abs(angleFromCenter)*2d <= horizontalFieldOfView(), "The point that is being requested is outisde of the filed of view.");
        double pixelFromCenter = angleFromCenter/DELTA;
        return ((width-1)/2.0)+pixelFromCenter;
    }
    
    
    /**
     * Retrieves the altitude corresponding to the index of a pixel in the vertical axis y.
     * 
     * @param y Index of the pixel in the y-axis.
     * @return Altitude of the pixel. Angle in radiant. Can be negative.
     * @throws IllegalArgumentException If "y" is not contained in [0, height-1]
     */
    public double altitudeForY(double y){
        checkArgument(y >= 0 && y <= height()-1 , "y-coord pixel out of bound.");
        double middle = (height()-1.)/2.;
        double yFromCenter = (middle-y);
        return DELTA * yFromCenter;
    }
    
    
    /**
     * Retrieves the vertical index of the pixel.
     * 
     * @param a elevation.
     * @return index of the vertical pixel corresponding to the angle a.
     * @throws IllegalArgumentException if the angle does not belong to the visible area.
     */
    public double yForAltitude(double a){
        checkArgument(a >= (verticalFieldOfView()/-2.) && a <= (verticalFieldOfView()/2.), "the angle does not belong to the visible area");
        return (a/DELTA - (height()-1d)/2d)*(-1);
    }
    
    
    /**
     * Determines if the pixel index passed is a valid pixel. 
     * 
     * @param x index of the pixel in the x-axis.
     * @param y index of the pixel in the y-axis.
     * @return true if the index pair is an index within the panorama.
     */
    boolean isValidSampleIndex(int x, int y){
       return(!(x < 0 || x > width()-1 || y < 0 || y > height()-1));
    }
    
    
    /**
     * Calculates the linear index. 
     * <p>
     * The pixels are numbered from the top left, to the top right.
     * When it reaches the right side of the image it returns to the line bellow it and continues numbering.
     * All the pixels in the image will have a unique linearSampleIndex.
     * 
     * @param x index of the pixel in the x-axis
     * @param y index of the pixel in the y-axis
     * @return linear index of the pixel.
     */
    int linearSampleIndex(int x, int y){
        return x+y*width();
    }
    
}
