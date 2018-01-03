package ch.epfl.alpano;

import static java.lang.Math.toRadians;

import java.util.Locale;


public class manualTest {

    public static void main(String[] args) {
        System.out.println(Azimuth.canonicalize(Math.PI*3));
        System.out.println(Azimuth.canonicalize(Math.PI));
        
        GeoPoint lausanne = new GeoPoint(toRadians(6.631), toRadians(46.521));
        GeoPoint moscow = new GeoPoint(toRadians(37.623), toRadians(55.753));
        GeoPoint london = new GeoPoint(toRadians(-0.140132009983), toRadians(51.5016013));
        System.out.println(lausanne.distanceTo(moscow));
        System.out.println();
        
        System.out.println(Math.toDegrees(lausanne.azimuthTo(moscow)));
        
        
        System.out.println();
        System.out.println("look at london from lausanne "+Math.toDegrees(lausanne.azimuthTo(london)));
        System.out.println("Lausanne"+lausanne.toString());
        System.out.println("Moscow" + moscow.toString());
        
        Locale l = null;
        System.out.printf(l, "[%d..%d]",10,15); 
        
        System.out.println();
        System.out.println("x: "+Math.floor(-1.23));
        System.out.println("y: "+ Math.floor(4.8754));
    }

}
