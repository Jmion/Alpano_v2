package ch.epfl.alpano.gui;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * This class allows for the calculation of the labels of the panorama.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 *
 */
public final class Labelizer {
    private final ContinuousElevationModel cem;
    private final List<Summit> summitList;
    private static final  int EPSILON_SEARCH_INTERVAL = 64;
    private static final int ROTATE_ANGLE = -60;
    private static final int ARRAY_SIZE_INITIALIZATION = 20;
    private static final int LABEL_SPACING = 20;
    private static final int PIXELS_ABOVE_HIGHEST_SUMMIT_TEXT = 22;
    private static final int SPACING_BETWEEN_LINE_AND_TEXT = 2;
    private static final int TOLLERANCE_TO_SUMMIT = 200;
    private static final int VERTICAL_SUMMIT_LIMMIT = 170;
    
    
    /**
     * Builds a Labelizer. Needs access to the terrain model to know if the summit is visible. 
     * 
     * @param cdem Terrain model.
     * @param summitList List of summit that are going to be considered to be added to the panorama.
     * @throws NullPointerException in case the CEM is null.
     */
    public Labelizer(ContinuousElevationModel cdem, List<Summit> summitList){
        this.cem = requireNonNull(cdem);
        this.summitList = Collections.unmodifiableList(new ArrayList<>(summitList));
    }

    /**
     * Calculates the list of all the summits that are visible and then transforms it into Nodes to represent the label.
     * These Nodes are always added in pairs of 2. one node represents the line that points to the summit while the
     * other is the name and altitude of the summit.
     * 
     * @param parameter Parameters of the panorama.
     * @return List of nodes that are positioned to form labels of the panorama.
     */
    public List<Node> labels(PanoramaParameters parameter){
        List<VisibleSummit> visiSummitList = sortList(parameter);
        return Collections.unmodifiableList(creatNodesFromList(visiSummitList, parameter));
    }
    
    
    /**
     * Created the list of nodes given a list of summits that are visible. Nodes can be used for display in a JavaFX.
     * 
     * @param visList List of visible summits.
     * @param parameter The parameters of the panorama that will be displayed.
     * @return List of nodes containing lines and text fields representing labels.
     */
    private List<Node> creatNodesFromList(List<VisibleSummit> visList, PanoramaParameters parameter){
        //Initializing a bit bigger than default because it is extremely likely that we will have 10 summit and therefore 20 Nodes.
        List<Node> nodeList = new ArrayList<>(ARRAY_SIZE_INITIALIZATION);
        
        BitSet freeDomainForLabel = new BitSet(parameter.width());

        //setting the first and last values of the domain to true to so that no labels are add here.
        freeDomainForLabel.set(0, LABEL_SPACING-1, true);
        freeDomainForLabel.set(parameter.width()-LABEL_SPACING-1, parameter.width()-1, true);
        
        int heightOfTextLabels=0;
        if(!visList.isEmpty()){
            heightOfTextLabels = visList.get(0).getyIdx()-PIXELS_ABOVE_HIGHEST_SUMMIT_TEXT;
        }
                
        for(VisibleSummit v : visList){
            if(freeDomainForLabel.nextSetBit(v.getxIdx())-v.getxIdx()>=LABEL_SPACING){
                freeDomainForLabel.set(v.getxIdx(),v.getxIdx()+LABEL_SPACING, true);
                Text t = new Text(v.getName()+" ("+(int)Math.round(v.getElevation())+" m)");
                t.getTransforms().addAll(new Translate(v.getxIdx(), heightOfTextLabels), new Rotate(ROTATE_ANGLE,0,0));
                nodeList.add(t);
                nodeList.add(new Line(v.getxIdx(), heightOfTextLabels+SPACING_BETWEEN_LINE_AND_TEXT , v.getxIdx(), v.getyIdx()));
            }
        }
        return nodeList;
    }

    /**
     * A summit is considered visible if the following conditions are meat: 
     * <p>
     * <ol>
     * <li> Is within the viewing distance.
     * <li> Is within the horizontal field of view.
     * <li> Is within the vertical field of view.
     * <li> The summit is lower than 170 pixels from the top. Checking this here is better because we do not check with the roots for obstacles.
     * <li> Summit is visible. For a summit to be visible we are checking that there isn't terrain hiding the summit.
     * </ol>
     * 
     * @param parameter Parameters of the panorama that will be displayed
     * @return List of visible summits
     */
    private List<VisibleSummit> sortList(PanoramaParameters parameter){
        
        /*
         *Comparator that sorts by the y-axis index. 
         */
        Comparator<VisibleSummit> comparator1 = (VisibleSummit o1, VisibleSummit o2) -> ((Integer)o1.getyIdx()).compareTo(((Integer)o2.getyIdx()));
        
        /*
         * Comparator that sorts the summits by there elevation.
         */
        Comparator<VisibleSummit> comparator2 = (VisibleSummit o1, VisibleSummit o2) -> ((Float)o2.getElevation()).compareTo((Float)o1.getElevation());
        
        List<VisibleSummit> returnedSetOfVisSummit = new ArrayList<>();
        
        for(int i = 0; i< summitList.size(); ++i){
            
            Summit summit = summitList.get(i);


            double distance  = parameter.observerPosition().distanceTo(summit.position());
           
            if(distance>parameter.maxDistance()){
                continue;
            }

            //checking that it is within the horizontal field of view.
            double azimuth = (parameter.observerPosition().azimuthTo(summit.position()));    //the direction in which the summit is.
            double angleFromCenter = Math2.angularDistance(parameter.centerAzimuth(), azimuth);
            
            if(!(Math.abs(angleFromCenter)*2d <= parameter.horizontalFieldOfView())){
                continue;
            }
            VisibleSummit visibleSummit = new VisibleSummit();
            visibleSummit.setxIdx((int)Math.round(parameter.xForAzimuth(azimuth)));

            ElevationProfile elevProfile = new ElevationProfile(cem, parameter.observerPosition(), azimuth, distance);
            //This function is used to take into account that the earth is round. It is the a better way to get the observers elevation at the summit.
            double elevationHorzAtSummit = PanoramaComputer.rayToGroundDistance(elevProfile, parameter.observerElevation(), 0).applyAsDouble(distance);
            double tanElevationOfSummit = -elevationHorzAtSummit/distance;
            double altitudeOfSummit = Math.atan2(-elevationHorzAtSummit, distance);   //angle
            
            //checking that it is in the vertical field of view
            if(Math.abs(altitudeOfSummit)> parameter.verticalFieldOfView()/2d){
                continue;
            }

            //checking that the y-cord is greater than 170.
            int yIndex = (int)Math.round(parameter.yForAltitude(altitudeOfSummit));
            if(yIndex<VERTICAL_SUMMIT_LIMMIT){
                continue;
            }
            visibleSummit.setyIdx(yIndex);

            DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(elevProfile, parameter.observerElevation(), tanElevationOfSummit);
            double value = firstIntervalContainingRoot(f, 0, distance-TOLLERANCE_TO_SUMMIT, EPSILON_SEARCH_INTERVAL);
            if(value != Double.POSITIVE_INFINITY){
                continue;
            }


            visibleSummit.setName(summit.name()).setElevation(summit.elevation());
            returnedSetOfVisSummit.add(visibleSummit);
        }
        returnedSetOfVisSummit.sort(comparator1.thenComparing(comparator2));
        return returnedSetOfVisSummit;
    }

    
    /**
     * A VisibleSummit is a immutable class that will represent a Summit that we want to represent on the panorama.
     * @author Jeremy
     *
     */
    private static class VisibleSummit{
        /**
         * Name of the Summit
         */
        private  String name;
        /**
         * Location of the summit in the x-axis
         */
        private  int xIdx;
        /**
         * Index of the summit in the y-axis
         */
        private int yIdx;
        /**
         * Elevation of the summit in meters.
         */
        private float elevation;

        /**
         * Retrieves the name of the Summit.
         * @return Name of the Summit.
         */
        public String getName() {
            return name;
        }

        /**
         * Retrieves the x index of the summit.
         * @return x axis location in pixel of the location of the summit
         */
        public int getxIdx() {
            return xIdx;
        }

        /**
         * Retrieves the y index of the summit
         * @return y axis location in pixel of the location of the summit
         */
        public int getyIdx() {
            return yIdx;
        }

        /**
         * Retrieves the elevation in meters.
         * @return elevation given in meters.
         */
        public float getElevation() {
            return elevation;
        }

        /**
         * Sets the name of the vissible summit.
         * @param name Name of the summit
         * @return this
         */
        public VisibleSummit setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the value of the y index attribute.
         * @param yIdx Y index coordinate(pixel).
         * @return this
         */
        public VisibleSummit setyIdx(int yIdx) {
            this.yIdx = yIdx;
            return this;
        }
        
        /**
         * Sets the value of the x index attribute.
         * @param xIdx X index coordinate(pixel).
         * @return this
         */
        public VisibleSummit setxIdx(int xIdx) {
            this.xIdx = xIdx;
            return this;
        }

        /**
         * Sets the elevation of the visible summit. Given in meter.
         * @param elevation Of the visible summit given in meters.
         * @return this
         */
        public VisibleSummit setElevation(float elevation) {
            this.elevation = elevation;
            return this;
        }
    }

}