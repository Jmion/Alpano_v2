/*
 *	Author:      Martin Cibils
 *	Date:        15 mars 2017
 */


package ch.epfl.alpano;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

public class DrawAllHgt {
    final static double WIDTH = toRadians(0.5);
    final static int IMAGE_SIZE = 300;
    final static double MIN_ELEVATION = 200;
    final static double MAX_ELEVATION = 1_500;

    public static void main(String[] as) throws Exception {
        String tabFile[] = {
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
                "N47E011.hgt"};
        
        for(String f:tabFile){
            draw(f,toRadians(Integer.parseInt(f.substring(4, 7))+0.25),toRadians(Integer.parseInt(f.substring(1, 3))+0.25));
        }
    }

    private static void draw(String f, double origin_lon, double origin_lat) throws Exception{
        File hgt_file = new File(f);
        DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(hgt_file);
        ContinuousElevationModel cDEM =
                new ContinuousElevationModel(dDEM);

        double step = WIDTH / (IMAGE_SIZE - 1);
        BufferedImage i = new BufferedImage(IMAGE_SIZE,
                IMAGE_SIZE,
                TYPE_INT_RGB);
        for (int x = 0; x < IMAGE_SIZE; ++x) {
            double lon = origin_lon + x * step;
            for (int y = 0; y < IMAGE_SIZE; ++y) {
                double lat = origin_lat + y * step;
                GeoPoint p = new GeoPoint(lon, lat);
                double el =
                        (cDEM.elevationAt(p) - MIN_ELEVATION)
                        / (MAX_ELEVATION - MIN_ELEVATION);
                i.setRGB(x, IMAGE_SIZE - 1 - y, gray(el));
            }
        }
        ImageIO.write(i, "png", new File("MJ_" + f.substring(0,7)+".png"));

    }

    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
    }
}