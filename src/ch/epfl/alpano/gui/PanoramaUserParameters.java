package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.gui.UserParameter.CENTER_AZIMUTH;
import static ch.epfl.alpano.gui.UserParameter.HEIGHT;
import static ch.epfl.alpano.gui.UserParameter.HORIZONTAL_FIELD_OF_VIEW;
import static ch.epfl.alpano.gui.UserParameter.MAX_DISTANCE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_ELEVATION;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LATITUDE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LONGITUDE;
import static ch.epfl.alpano.gui.UserParameter.SUPER_SAMPLING_EXPONENT;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static java.lang.Math.pow;
import static java.lang.Math.toRadians;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

/**
 * This class allows for the creation of Panorama parameters but insures that the given arguments are 
 * contained within the model of our terrain. If illegal arguments are given they will be 
 * <i> sanitized</i> which means that they will be brought within the domain of our model.
 * <p>
 * This class should be used for safe creation of PanoramaParameters. The advantage of using this
 * class instead of PanoramaParameters is that this class insures that the given arguments are going
 * to be within the DEM that we have available for computing the terrain.
 * 
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public final class PanoramaUserParameters {
    private final Map<UserParameter, Integer> paraMap;
    private static final int MAX_VERTICAL_VIEWING_ANGLE = 170;
    private static final int METERS_PER_KILOMETER = 1000;
    private static final double TENTHOUSANDS_OF_DEGREE_TO_DEGREE = 10000d;
    
    /**
     * Creates a new instance of this class
     *
     * @param paraMap A map containing all information for a panorama. This map needs to for keys all the Enum from UserParameter.
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> paraMap){
        this(
                paraMap.get(OBSERVER_LONGITUDE),
                paraMap.get(OBSERVER_LATITUDE),
                paraMap.get(OBSERVER_ELEVATION),
                paraMap.get(CENTER_AZIMUTH),
                paraMap.get(HORIZONTAL_FIELD_OF_VIEW),
                paraMap.get(MAX_DISTANCE),
                paraMap.get(WIDTH),
                paraMap.get(HEIGHT),
                paraMap.get(SUPER_SAMPLING_EXPONENT)
                );
    }
    
    //&#176; represents the ° symbol in html.
    
    /*
     * Creates a new instance of this class. 
     * 
     * @param observerLongitude The longitude of the observer in <b> 1&#176;/10_000 </b> of a degree.
     * @param observerLatitude The latitude of the observer in <b> 1&#176;/10_000 </b> of a degree.
     * @param obeserverElevation Elevation of the observer in meters.
     * @param centerAzimuth The angle of the center azimuth in degrees.
     * @param horizontalFieldOfView The angle of the horizontal field of view in degrees.
     * @param maxDistance The maximum viewing distance in Km.
     * @param width In pixels.
     * @param height In pixels.
     * @param superSamplingExponent If <b> 1 </b> 4 pixels are calculated and averaged per pixel displayed. If <b> 2 </b> 16 pixels are calculated and averaged per pixel displayed.
     */
    public PanoramaUserParameters(int observerLongitude, int observerLatitude, int obeserverElevation, int centerAzimuth, int horizontalFieldOfView, int maxDistance, int width, int height, int superSamplingExponent){
        this.paraMap = Collections.unmodifiableMap(new EnumMap<>((createParaMap(observerLongitude, observerLatitude, obeserverElevation, centerAzimuth, horizontalFieldOfView, maxDistance, width, height, superSamplingExponent))));
    }
    
    /**
     * Method intended to be used in the constructor of PanoramaUserParameters. 
     * Will create a map that can be passed to the constructor that takes a map.
     * 
     * @param args 9 integers
     * @return EnumMap that contains all the parameters.
     */
    static private Map<UserParameter, Integer> createParaMap(int ... args){
        //chose to receive a array of args because I can use them to initialize the map very easily with a for loop.
        assert args.length == 9 : "args has "+args.length+" instead of the expected 9.";
        Map<UserParameter, Integer> paraMapTemp = new EnumMap<>(UserParameter.class);
        int height = UserParameter.HEIGHT.sanitize(args[7]);
        int width = UserParameter.WIDTH.sanitize(args[6]);
        int horizontalFieldOfView = UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(args[4]);
        
        int maxHeight = (MAX_VERTICAL_VIEWING_ANGLE*(width-1)/horizontalFieldOfView)+1;
        if( height > maxHeight)
            args[7] = maxHeight;
        UserParameter[] userPara = UserParameter.values();
        for(int i=0; i<9; ++i)
            paraMapTemp.put(userPara[i], userPara[i].sanitize(args[i]));
        return paraMapTemp;
    }
    
    /**
     * Retrieves the value of the user parameter that was passed as argument.
     * 
     * @param userParam Parameter that
     * @return Value of the user parameter.
     */
    public int get(UserParameter userParam){
        return paraMap.get(userParam);
    }
    
    
    //&#176; represents the ° symbol in html.
    
    /**
     * Retrieve the observer's longitude. In <b> 1&#176;/10_000 </b> of a degree.
     * 
     * @return The longitude of the observer in <b> 1&#176;/10_000 </b> of a degree.
     */
    public int observerLongitude(){
        return get(OBSERVER_LONGITUDE);
    }
    
    /**
     * Retrieve the observer's latitude. In <b> 1&#176;/10_000 </b> of a degree.
     * 
     * @return The latitude of in <b> 1&#176;/10_000 </b> of a degree.
     */
    public int observerLatitude(){
        return get(OBSERVER_LATITUDE);
    }
    
    /**
     * Retrieve the observer's elevation. In meters.
     * 
     * @return The elevation of the observer in meters.
     */
    public int observerElevation(){
        return get(OBSERVER_ELEVATION);
    }
    
    /**
     * Retrieve the observer's center azimuth. In degrees.
     * 
     * @return The center azimuth. In degrees.
     */
    public int centerAzimuth(){
        return get(CENTER_AZIMUTH);
    }
    
    /**
     * Retrieve the horizontal field of view. In degrees.
     * 
     * @return The horizontal field of view in degrees.
     */
    public int horizontalFieldOfView(){
        return get(HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * Retrieve the maximal viewing distance in km.
     * 
     * @return The maximum viewing distance in km.
     */
    public int maxDistance(){
        return get(MAX_DISTANCE);
    }
    
    /**
     * Retrieve the width of the panorama. In samples.
     * 
     * @return Width of the panorama. In pixels.
     */
    public int width(){
        return get(WIDTH);
    }
    
    /**
     * Retrieve the height of the panorama. In samples.
     * 
     * @return Height of the panorama in pixels.
     */
    public int height(){
        return get(HEIGHT);
    }
    
    /**
     * Retrieves the super sampling exponent.
     * 
     * @return The super sampling argument.
     */
    public int superSamplingExponent(){
        return get(SUPER_SAMPLING_EXPONENT);
    }
    
    /**
     * Creates a PanoramaParameter that needs to be computed. This PanoramaPArameter takes into account the supper sampling.
     * <p>
     * Supper sampling is a method to allow for a nicer image to be calculated. The edges in the image will be smother.
     * The super sampling exponent will be used to scale the calculated image.
     * <p>
     * Let: 
     * <p>
     * w<sub>i</sub> = the width of the displayed image. <p>
     * h<sub>i</sub> = the height of the displayed image. <p>
     * w<sub>p</sub> = the width of the calculated image. <p>
     * h<sub>p</sub> = the height of the calculated image. <p>
     * s = the supper sampling value. <p>
     * w<sub>p</sub> = 2<sup>s</sup> * w<sub>i</sub><p>
     * h<sub>p</sub> = 2<sup>s</sup> * h<sub>i</sub>
     * 
     * @return The parameters of the panorama that need to be computed. Includes the supper sampling.
     * @see ch.epfl.alpano.PanoramaParameters
     */
    public PanoramaParameters panoramaParameters(){
        return givesThePanoramaParameter(superSamplingExponent());
    }
    
    /**
     * Creates a PanoramaParameter that needs to be computed.
     * 
     * @return The parameters of the panorama that need to be computed.
     * @see ch.epfl.alpano.PanoramaParameters
     */
    public PanoramaParameters panoramaDisplayParameters(){
        return givesThePanoramaParameter(0);
    }
    
    private PanoramaParameters givesThePanoramaParameter(int superSamplingExponent){
        return new PanoramaParameters(
                new GeoPoint(toRadians(get(OBSERVER_LONGITUDE)/TENTHOUSANDS_OF_DEGREE_TO_DEGREE), toRadians(get(OBSERVER_LATITUDE)/TENTHOUSANDS_OF_DEGREE_TO_DEGREE)),
                get(OBSERVER_ELEVATION),
                canonicalize(toRadians(get(CENTER_AZIMUTH))),
                toRadians(get(HORIZONTAL_FIELD_OF_VIEW)),
                get(MAX_DISTANCE)*METERS_PER_KILOMETER,
                (int)(get(WIDTH)*pow(2,superSamplingExponent)),
                (int)(get(HEIGHT)*pow(2, superSamplingExponent))
                );
    }
    
    @Override
    public boolean equals(Object obj){
        return obj instanceof PanoramaUserParameters ? ((PanoramaUserParameters)obj).paraMap.equals(paraMap) : false;
    }
    
    @Override
    public int hashCode(){
        return paraMap.hashCode();
    }
}
