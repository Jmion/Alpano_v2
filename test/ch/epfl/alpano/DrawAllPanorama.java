package ch.epfl.alpano;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

public class DrawAllPanorama {
    //final static File HGT_FILE = new File("N46E007.hgt");
    
    final static String [] HGT_FILE = {
            "N45E006.hgt", 
             "N45E007.hgt",
            "N45E008.hgt",
             "N45E009.hgt",
            "N45E010.hgt",
            "N45E011.hgt",
             "N46E006.hgt",
             "N46E007.hgt",
             "N46E008.hgt",
             "N46E009.hgt",
             "N46E010.hgt",
             "N46E011.hgt",
             "N47E006.hgt",
             "N47E007.hgt",
             "N47E008.hgt",
             "N47E009.hgt",
             "N47E010.hgt",
             "N47E011.hgt"
             };

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 1000;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(90);
    final static int MAX_DISTANCE = 100_000;

    

    public static void main(String[] as) throws Exception {
        
        for(String s : HGT_FILE){
            
            DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(new File(s) );
                 ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
                 
                 PanoramaParameters PARAMS = new PanoramaParameters(new GeoPoint(toRadians(new Double(s.substring(5,7)+"."+s.substring(6,7)+"5")), toRadians(new Double(s.substring(1,3)+"."+s.substring(2,3)+"5"))), ELEVATION, CENTER_AZIMUTH, HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);
                 
                 Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

                 BufferedImage i = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, TYPE_INT_RGB);

                 for (int x = 0; x < IMAGE_WIDTH; ++x) {
                   for (int y = 0; y < IMAGE_HEIGHT; ++y) {
                     float d = p.distanceAt(x, y);
                     int c = (d == Float.POSITIVE_INFINITY)
                       ? 0x87_CE_EB
                       : gray((d - 2_000) / 15_000);
                     i.setRGB(x, y, c);
                   }
                 }

                 ImageIO.write(i, "png", new File("Panorama_"+s+".png"));
               }
            
        
      
      
      
    }
    
    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
      }
    
    
}
