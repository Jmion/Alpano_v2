package ch.epfl.alpano;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
<<<<<<< HEAD
=======
import ch.epfl.alpano.gui.PanoramaRendererExecutionPool;
import ch.epfl.alpano.gui.PanoramaRendererMultithread;
>>>>>>> progressBar2
import ch.epfl.alpano.gui.PredefinedPanoramas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;



final class DrawPanorama {

    final static File HGT_FILE = new File("N46E007.hgt");
    final static File HGT_FILE1 = new File("N46E006.hgt");
    
    final static int IMAGE_WIDTH = 6000;
    final static int IMAGE_HEIGHT = 2000;

    final static double ORIGIN_LON = toRadians( 6.90189);
    final static double ORIGIN_LAT = toRadians(46.6354);
    final static int ELEVATION = 2000;
    final static double CENTER_AZIMUTH = toRadians(210);
    final static double HORIZONTAL_FOV = toRadians(140);
    final static int MAX_DISTANCE = 100_000;

//    final static PanoramaParameters PARAMS =
//            new PanoramaParameters(new GeoPoint(ORIGIN_LON,
//                    ORIGIN_LAT),
//                    ELEVATION,
//                    CENTER_AZIMUTH,
//                    HORIZONTAL_FOV,
//                    MAX_DISTANCE,
//                    IMAGE_WIDTH,
//                    IMAGE_HEIGHT);

    final static PanoramaParameters PARAMS = PredefinedPanoramas.NIESEN.panoramaParameters();

    public static void main(String[] as) throws Exception {
        double t1 =System.currentTimeMillis();
<<<<<<< HEAD
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE); 
        DiscreteElevationModel dDem1 = new HgtDiscreteElevationModel(HGT_FILE1);
=======
            DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE); DiscreteElevationModel dDem1 = new HgtDiscreteElevationModel(HGT_FILE1);
>>>>>>> progressBar2
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM.union(dDem1));
            Panorama p;           
            //End Labelizer Test
            double time = System.currentTimeMillis();
            //adding code so that it is easy to switch between multithreaded mode and single threaded mode.
            System.out.println("Start of panoramaCompute.");
<<<<<<< HEAD
            p = new PanoramaComputer(cDEM).computePanorama(PARAMS);
=======
            if(MULTITHREADED)
                p = new PanoramaComputerExecutionPool(cDEM).computePanorama(PARAMS);
            else
                p = new PanoramaComputer(cDEM).computePanorama(PARAMS);
>>>>>>> progressBar2
            System.out.println("End of panoramaCompute. Time : "+(System.currentTimeMillis()-time));

            time = System.currentTimeMillis();
            System.out.println("ChannelPainter start.");
            ChannelPainter distance = p::distanceAt;
            ChannelPainter slope = p::slopeAt;
            ChannelPainter brightness = slope.mul(2f).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
            ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
            ChannelPainter saturation = distance.div(200000).clamped().inverted();
            ChannelPainter hue = distance.div(100000).cycling().mul(360);
            System.out.println("End of ChannelPainter. Time : "+(System.currentTimeMillis()-time));
            System.out.println("Start of imagePainter hsb.");
            time = System.currentTimeMillis();
            ImagePainter l = ImagePainter.hsb(hue, saturation, brightness, opacity);
            System.out.println("End of imagePainter. Time : "+(System.currentTimeMillis()-time));
            
            Image i;
            time=System.currentTimeMillis();
            System.out.println("render launch.");
<<<<<<< HEAD
=======
            if(MULTITHREADED)
                i = PanoramaRendererExecutionPool.renderPanorama(p, l);
            else
>>>>>>> progressBar2
                i = PanoramaRenderer.renderPanorama(p, l);
            System.out.println("render end. Time : "+(System.currentTimeMillis()-time));
            time = System.currentTimeMillis();
            
            
            
            System.out.println("Writting image to disk.");
            ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("niesen-color.png"));
            System.out.println("End of writting image to disk. Time : "+(System.currentTimeMillis()-time));
<<<<<<< HEAD
        
        System.out.println("Time for drawing pano was in ms: " + (System.currentTimeMillis()-t1));
    }
=======
            System.out.println("Time for drawing pano was in ms: " + (System.currentTimeMillis()-t1));
        }
>>>>>>> progressBar2

    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
    }
}