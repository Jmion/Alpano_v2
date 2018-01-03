package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
/**
 * This class is used to update all the values whenever the user changes them. 
 * If the values are wrong (out of bounds) there are put back in the bounds so the program can calculate them correctly.
 * This is possible because each value entered by the user are being watch.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 **/
public final class PanoramaParametersBean {
    private final Map<UserParameter, ObjectProperty<Integer>> myMap;
    private final ObjectProperty<PanoramaUserParameters> parameters;
    /**
     * This constructor initialize a map and a PanoramaUserParamater. <p>
     * Each attributes of the PanoramaUserParameter in argument are being observed, put in the map and synchronized with the PanoramaUserParameter whenever the values of the map changes.
     * @param myPan The PanoramaUserParamater used to initialize.
     */
    public PanoramaParametersBean(PanoramaUserParameters myPan){
        parameters = new SimpleObjectProperty<>(myPan);
        myMap = new EnumMap<>(UserParameter.class);
        for(UserParameter u : UserParameter.values()){
            ObjectProperty<Integer> obj =  new SimpleObjectProperty<>(myPan.get(u));
            obj.addListener((b, o, n) -> Platform.runLater(this::synchronizeParameters));
            myMap.put(u, obj);
        }     
    }
    private void synchronizeParameters(){
        PanoramaUserParameters p = new PanoramaUserParameters(observerLongitudeProperty().getValue(), observerLatitudeProperty().getValue(), observerElevationProperty().getValue(),
                centerAzimuthProperty().getValue(), horizontalFieldOfViewProperty().getValue(), maxDistanceProperty().getValue(), 
                widthProperty().getValue(), heightProperty().getValue(), superSamplingExponentProperty().getValue());
        parameters.set(p);
        for(UserParameter u : UserParameter.values()){
            myMap.get(u).set(p.get(u));
        }        
        
    }
    /**
     * 
     * @return parameters A read-only PanoramaUserParamters. The one used to do the calculus.
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parameters;
    }
    
//    public ObjectProperty<PanoramaUserParameters> parametersPropertyModifiable(){
//        return parameters;
//    }
    /**
     * 
     * @return The longitude of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> observerLongitudeProperty(){
        return myMap.get(UserParameter.OBSERVER_LONGITUDE);
    }
    /**
     * 
     * @return The latitude of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> observerLatitudeProperty(){
        return myMap.get(UserParameter.OBSERVER_LATITUDE);
    }
    /**
     * 
     * @return The elevation of the observer of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> observerElevationProperty(){
        return myMap.get(UserParameter.OBSERVER_ELEVATION);
    }
    /**
     * 
     * @return The center azimuth of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> centerAzimuthProperty(){
        return myMap.get(UserParameter.CENTER_AZIMUTH);
    }
    /**
     * 
     * @return The horizontal field of view of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return myMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    /**
     * 
     * @return The  max distance of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> maxDistanceProperty(){
        return myMap.get(UserParameter.MAX_DISTANCE);
    }
    /**
     * 
     * @return The width of the PanoramaUserParamater of the map.
     */
    public ObjectProperty<Integer> widthProperty(){
        return myMap.get(UserParameter.WIDTH);
    }
    /**
     * 
     * @return The height of the PanoramaUserParamter of the map.
     */
    public ObjectProperty<Integer> heightProperty(){
        return myMap.get(UserParameter.HEIGHT);
    }
    /**
     * 
     * @return The super sampling exponent of the map.
     */
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return myMap.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
}
