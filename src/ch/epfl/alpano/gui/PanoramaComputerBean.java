package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static java.lang.Math.pow;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputerExecutionPool;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * A panoramaComputer bean allows for observation of it's parameters. It represents the current information that is being calculated.
 * @author Martin Cibils (261746)
 *@author Jeremy Mion (261178)
 *
 */
public final class PanoramaComputerBean extends Thread{
   
    final private ObjectProperty<Panorama> panorama;
    final private ObjectProperty<PanoramaUserParameters> parameter;
    final private ObjectProperty<Image> image;

    final private ObservableList<Node> labels;
    final private Labelizer labelizer;
    final private PanoramaComputerExecutionPool panoComputer;
    
    final private IntegerProperty currentPanoWidthProperty;

    
    
    /**
     * Creates the ComputerBean and sets the listener so that when the parameter changes the other variables are recalculated accordingly.
     * @param cDem Continuous elevation model of the area where you will want to compute panorama.
     * @param summitList List of all the summits that you want to label.
     */
    public PanoramaComputerBean(ContinuousElevationModel cDem, List<Summit> summitList){
        labels = observableArrayList();
        panorama = new SimpleObjectProperty<>();
        parameter = new SimpleObjectProperty<>();
        panoComputer = new PanoramaComputerExecutionPool(cDem);
        image = new SimpleObjectProperty<>();
        labelizer = new Labelizer(cDem, summitList);
        currentPanoWidthProperty = new SimpleIntegerProperty();
        
        //updating panorama is parameters change
        parameter.addListener((o,old,newV) -> {
           Thread thr = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    parameter.set(newV);        //updating parameters
                    currentPanoWidthProperty.set((int) (newV.get(WIDTH)*pow(2,newV.superSamplingExponent())));      //updates the width property.
                    PanoramaRendererExecutionPool.resetStateOfExecution(); // This allows for proper reinitialisation of the progress bar. The second par of the progress bar will be reset when calling computePanorama
                    Panorama tempPano = panoComputer.computePanorama(getParameters().panoramaParameters());
                    javafx.application.Platform.runLater(()->panorama.set(tempPano));   //calculating panorama
                    List<Node> tempLabels = labelizer.labels(parameter.get().panoramaDisplayParameters());
                    javafx.application.Platform.runLater(() ->labels.setAll(tempLabels));                    
                    //creating the channel painters
                    ChannelPainter distance = panorama.get()::distanceAt;
                    ChannelPainter slope = panorama.get()::slopeAt;
                    ChannelPainter brightness = slope.mul(2f).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
                    ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
                    ChannelPainter saturation = distance.div(200000).clamped().inverted();
                    ChannelPainter hue = distance.div(100000).cycling().mul(360);
                    ImagePainter l = ImagePainter.hsb(hue, saturation, brightness, opacity);
                    Image tempImage = PanoramaRendererExecutionPool.renderPanorama(getPanorama(), l);
                    javafx.application.Platform.runLater(()->image.set(tempImage));
                    //Ensuring that even if the update of the property happens to go wrong the progress bar disapears at the end of the computation of the panorama.
                    PanoramaRendererExecutionPool.updateExecutionStateProperty();
                    panoComputer.updateExecutionStateProperty();
                }
            });
           thr.start();
        });
    }
    
    /**
     * Retrieves the ObjectProperty of PanoramaUserParameters.
     * @return property of userParameters.
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parameter;
    }
    
    /**
     * Retrieves the panoramaUserParameters.
     * @return UserParameters
     */
    public PanoramaUserParameters getParameters(){
        return parameter.get();
    }
    /**
     * Sets the UserParameters.
     * @param newParameters PanorameUserParameters to be set to the Property.
     */
    public void setParameters(PanoramaUserParameters newParameters){
        parameter.set(newParameters);
    }

    /**
     * Retrieves ReadOnly panorama.
     * @return read only property of the panoarama.
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
        return  panorama;
    }
    /**
     * Retrives the panorama.
     * @return the panorama contained in the panoramProperty
     */
    public Panorama getPanorama(){
        return panorama.get();
    }
    /**
     * Retrives the image as a read only property
     * @return the image as a read only property.
     */
    public ReadOnlyObjectProperty<Image> imageProperty(){
        return image;
    }
    /**
     * Retrieves the image of that is contained in the bean.
     * @return the image.
     */
    public Image getImage(){
        return image.get();
    }
    
    /**
     * Retrives the labels.
     * @return retrives the obervable list of labels that represent the lines and text labels that are added to summits.
     */
    public ObservableList<Node> getLabels(){
        return labels;
    }
    
    public DoubleBinding getStateOfExecution(){
        return (panoComputer.stateOfEecution().divide(currentPanoWidthProperty).multiply(0.8).add(PanoramaRendererExecutionPool.stateOfEecution().divide(currentPanoWidthProperty).multiply(0.2)));
    }
}
