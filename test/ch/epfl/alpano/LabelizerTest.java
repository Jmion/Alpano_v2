package ch.epfl.alpano;

import java.io.File;
import java.util.List;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.Labelizer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import ch.epfl.alpano.summit.GazetteerParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;

public final class LabelizerTest extends Application {
    
    final static File HGT_FILE = new File("N46E007.hgt");
    final static File HGT_FILE1 = new File("N46E006.hgt");
    
    final static PanoramaParameters PARAMS = PredefinedPanoramas.ALP_JURA.panoramaParameters();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE); DiscreteElevationModel dDem1 = new HgtDiscreteElevationModel(HGT_FILE1);
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM.union(dDem1));
            
            //Labelizer Test
            
            double time = System.currentTimeMillis();
            
            Labelizer label = new Labelizer(cDEM, GazetteerParser.readSummitsFrom(new File("alps.txt")));
            List<Node> nodeList = label.labels(PARAMS);
            nodeList.forEach(System.out::println);
            
            System.out.println("Time for calculation : "+(System.currentTimeMillis()-time));
        

        Platform.exit();
    }
}