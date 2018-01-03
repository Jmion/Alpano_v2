package ch.epfl.alpano;

import static org.junit.Assert.*;
import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

import org.junit.Test;

public class GeoPointTestBastienPhil {
    @Test
    public void testCorrectValuesConstructor(){
        //cas limites
        new GeoPoint(-PI,0);
        new GeoPoint(PI,0);
        new GeoPoint(0,-PI/2);
        new GeoPoint(0,PI/2);
        
        //cas trivial
        new GeoPoint(PI/3,-PI/4);
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrongLongitudeConstructor1(){
        new GeoPoint(PI+1, 0);
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrongLatitudeConstructor1(){
        new GeoPoint(0, PI);
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrongLongitudeConstructor2(){
        new GeoPoint(-PI-1, 0);
    }
    
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWrongLatitudeConstructor2(){
        new GeoPoint(0, -3*PI/4);
    }
    
    @Test
    public void testLongitude(){
        GeoPoint g = new GeoPoint(PI/3,PI/4);
        
        assertEquals(g.longitude(), PI/3,0);
    }
    
    @Test
    public void testLatitude(){
        GeoPoint g = new GeoPoint(PI/3,PI/4);
        
        assertEquals(g.latitude(), PI/4,0);
    }
    
    @Test
    public void distanceToOnKnownValue1(){
        GeoPoint lausanne = new GeoPoint(toRadians(6.631),toRadians(46.521));
        GeoPoint moscou = new GeoPoint(toRadians(37.623),toRadians(55.753));
        
        assertEquals(2370000, lausanne.distanceTo(moscou),3000);
    }

    @Test
    public void distanceToOnKnownValue2(){
        GeoPoint bale = new GeoPoint(toRadians(7.57327),toRadians(47.5584));
        GeoPoint lausanne = new GeoPoint(toRadians(6.63282),toRadians(46.516));
        
        assertEquals(136000, lausanne.distanceTo(bale),70);
    }
    
    @Test
    public void azimuthToOnKnownValue(){

        GeoPoint lausanne = new GeoPoint(toRadians(6.631),toRadians(46.521));
        GeoPoint moscou = new GeoPoint(toRadians(37.623),toRadians(55.753));
        
        assertEquals(toRadians(52.95), lausanne.azimuthTo(moscou),0.0001);
    }
    
    @Test
    public void testToString(){
        GeoPoint g = new GeoPoint(toRadians(-7.6543),toRadians(54.3210));
        System.out.println(g.toString());
        assertTrue(g.toString().equals("(-7.6543,54.3210)"));
    }

}
