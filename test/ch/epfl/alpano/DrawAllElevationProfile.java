package ch.epfl.alpano;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Math.toRadians;
import javax.imageio.ImageIO;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

final class DrawAllElevationProfile {
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
    final static double MAX_ELEVATION = 1_500;
    final static int LENGTH = 111_000;
    final static double AZIMUTH = toRadians(27.97);
    //final static double LONGITUDE = toRadians(6.15432);
    //final static double LATITUDE = toRadians(46.20562);
    final static int WIDTH = 800, HEIGHT = 100;


    public static void main(String[] as) throws Exception {
        for(String f:HGT_FILE){
            
            DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(new File(f));
            System.out.println(new Double(f.substring(1,3))+", "+new Double(f.substring(5,7)));
            draw(f, dDEM, toRadians(new Double(f.substring(5,7))), toRadians(new Double(f.substring(1,3))));
        
        
        }
      
    }
    
   
    
    private static void draw(String f, DiscreteElevationModel dDEM, double LONGITUDE, double LATITUDE) throws Exception{
        ContinuousElevationModel cDEM =
                new ContinuousElevationModel(dDEM);
              GeoPoint o =
                new GeoPoint(LONGITUDE, LATITUDE);
              ElevationProfile p =
                new ElevationProfile(cDEM, o, AZIMUTH, LENGTH);

              int BLACK = 0x00_00_00, WHITE = 0xFF_FF_FF;

              BufferedImage i =
                new BufferedImage(WIDTH, HEIGHT, TYPE_INT_RGB);
              for (int x = 0; x < WIDTH; ++x) {
                double pX = x * (double) LENGTH / (WIDTH - 1);
                double pY = p.elevationAt(pX);
                int yL = (int)((pY / MAX_ELEVATION) * (HEIGHT - 1));
                for (int y = 0; y < HEIGHT; ++y) {
                  int color = y < yL ? BLACK : WHITE;
                  i.setRGB(x, HEIGHT - 1 - y, color);
                }
              }

              ImageIO.write(i, "png", new File("MJ_Profile"+f+".png"));
        
    }
  }