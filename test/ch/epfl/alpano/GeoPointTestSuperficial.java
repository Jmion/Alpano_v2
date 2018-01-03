package ch.epfl.alpano;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTestSuperficial {
    
    GeoPoint lausanne = new GeoPoint(toRadians(6.631), toRadians(46.521));
    GeoPoint moscow = new GeoPoint(toRadians(37.623), toRadians(55.753));
    GeoPoint london = new GeoPoint(toRadians(-0.140132009983), toRadians(51.5016013));
    GeoPoint coinSudeOuestRLC = new GeoPoint(toRadians(6.56728
            ), toRadians( 46.51780));
    GeoPoint sometDeEiger = new GeoPoint(toRadians(8.00537), toRadians(46.57757));
    GeoPoint Lausanne = new GeoPoint(0.11576,0.81186);// 6.63282 , 46.516 
    GeoPoint NewYork = new GeoPoint(-1.29165,0.71060);// -74.00597, 40.71427
    GeoPoint Montpellier = new GeoPoint(0.06767, 0.76115);// 3.87723, 43.61092 
    GeoPoint LevinNZ = new GeoPoint(3.05913,-0.70919);// 175.275 ,-40.63333 
    GeoPoint Toulouse = new GeoPoint(0.02520,0.76104);//1.44367, 43.60426 
    GeoPoint Zurich =  new GeoPoint(0.14923, 0.82670);//8.55, 47.3666
    GeoPoint Geneve = new GeoPoint(toRadians(6.14569), toRadians(46.20222));

    
    //no sure of the amount of error
    @Test
    public void distanceToWorksOnKnownValues (){
        assertEquals(2367000, lausanne.distanceTo(moscow),500 );
    }
    
    @Test
    public void distanceFromEPFLToEiger (){
        assertEquals(110176, coinSudeOuestRLC.distanceTo(sometDeEiger),1);
    }
    
    @Test
    public void azimutFromEPFLToEiger () {
        assertEquals(86.67, Math.toDegrees(coinSudeOuestRLC.azimuthTo(sometDeEiger)),1);
    }
    
    @Test
    public void distanceToWorksWith2SameInputs (){
        assertEquals(0, lausanne.distanceTo(lausanne),10e-4);
    }
    
    @Test
    public void toStringWorksOnKnownInput(){
        assertEquals("(6.6310,46.5210)", lausanne.toString());
        assertEquals("(-0.1401,51.5016)", london.toString());
    }
    
    @Test
    public void longitudeTest(){
        assertEquals(toRadians(6.631) , lausanne.longitude(),0);
        assertEquals(toRadians(-0.140132009983), london.longitude(),0);
    }
    
    @Test
    public void latitudeTest(){
        assertEquals(toRadians(55.753), moscow.latitude(),0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void constrcutorFailsWhenWrongArgumentOnLagitude() {
        new GeoPoint(Math.PI, Math.PI/2);
        new GeoPoint(0, 0);
        new GeoPoint(-Math.PI,-Math.PI/2);
        new GeoPoint(3.14, 0);
        new GeoPoint(2*Math.PI,0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void constrcutorFailsWhenWrongArgumentOnLatitude() {
        new GeoPoint(0, Math.PI);
    }
    @Test
    public void returnsTheRightLongitude(){
        assertEquals(0, (new GeoPoint(0, 0)).longitude(),0);
        assertEquals(Math.PI, (new GeoPoint(Math.PI, 0).longitude()),0);
        assertEquals(-Math.PI, (new GeoPoint(-Math.PI, 0)).longitude(),0);
        assertEquals(Math.PI/4, (new GeoPoint(Math.PI/4, 0)).longitude(),0);
    }
    @Test
    public void returnsTheRightLatitude(){
        assertEquals(0, (new GeoPoint(0, 0)).latitude(),0);
        assertEquals(Math.PI/2, (new GeoPoint(0, Math.PI/2).latitude()),0);
        assertEquals(-Math.PI/2, (new GeoPoint(0, -Math.PI/2)).latitude(),0);
        assertEquals(Math.PI/4, (new GeoPoint(0, Math.PI/4)).latitude(), 0);
    }
    @Test
    public void distanceToAsTheRightValue(){

        assertEquals(19233000, Montpellier.distanceTo(LevinNZ),300);
        assertEquals(196000, Montpellier.distanceTo(Toulouse),300);
        assertEquals(6235000, NewYork.distanceTo(Lausanne),300);
    }
    @Test
    public void azymuthToIsWorkingFine(){
        assertEquals(Math.toRadians(57.08), Lausanne.azimuthTo(Zurich),0.1);
        assertEquals(49.28, Math.toDegrees(Geneve.azimuthTo(Lausanne)),3);        
    }

    


}
