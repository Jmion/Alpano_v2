/*
 *	Author:      Martin Cibils
 *	Date:        20 mars 2017
 */


package ch.epfl.alpano.dem;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public class PanoramaParametersTestMJ {
    @Test(expected = NullPointerException.class)
    public void nullPointerExpectionOnConstrucor(){
        new PanoramaParameters(null, 1, 1, 1, 1, 1, 1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionOnConstructor1(){
        new PanoramaParameters(new GeoPoint(0, 0),15, 2*Math.PI, 1., 50, 5, 5);
    }
    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionOnConstructor2(){
        new PanoramaParameters(new GeoPoint(0, 0),15, 1., 0, 50, 5, 5);
    }
    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionOnConstructor3(){
        new PanoramaParameters(new GeoPoint(0, 0),15, 1, 1., 0, 5, 5);
    }
    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionOnConstructor4(){
        new PanoramaParameters(new GeoPoint(0, 0),15, 1, 1., 50, 0, 5);
    }
    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionOnConstructor5(){
        new PanoramaParameters(new GeoPoint(0, 0),15, 1, 1., 50, 5, 0);
    }
    @Test
    public void constructorIsJustRight(){
        new PanoramaParameters(new GeoPoint(0, 0), 15,0 , 2*Math.PI, 1, 1, 1);
    }
    @Test
    public void verticalFieldOfViewGivesTheRightValue(){
        PanoramaParameters g =  new PanoramaParameters(new GeoPoint(Math.PI/2,Math.PI/4), 15, Math.PI/2, Math.PI, 15, 15, 15);
        double delta = g.horizontalFieldOfView()/(g.width() - 1);
        assertEquals(delta*(g.height()-1), g.verticalFieldOfView(),0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void ErrorOnAzimuthForX1(){
        PanoramaParameters g =  new PanoramaParameters(new GeoPoint(Math.PI/2,Math.PI/4), 15, Math.PI/2, Math.PI, 15, 15, 15);
        g.azimuthForX(-1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void ErrorOnAzimuthForX2(){
        PanoramaParameters g =  new PanoramaParameters(new GeoPoint(Math.PI/2,Math.PI/4), 15, Math.PI/2, Math.PI, 15, 15, 15);
        g.azimuthForX(g.width());
    }
    @Test
    public void azimuthForXReturnsTheRightValue(){
        PanoramaParameters g =  new PanoramaParameters(new GeoPoint(Math.PI/2,Math.PI/4), 15, Math.PI/2, Math.PI, 15, 15, 15);
        double x = 1;
        double delta = g.horizontalFieldOfView()/(g.width() - 1);

        double newAzimuth = (x - (g.width()-1)/2.)*delta+ g.centerAzimuth(); 
        assertEquals(newAzimuth, g.azimuthForX(x),0.05);
    }
    @Test
    public void yForAltitudeReturnTheRightValue(){
        PanoramaParameters g =  new PanoramaParameters(new GeoPoint(Math.PI/2,Math.PI/4), 15, Math.PI/2, Math.PI, 15, 15, 15);
        double a =1;
        double delta = g.horizontalFieldOfView()/(g.width() - 1);
        assertEquals((g.height()-1)/2. - a/delta, g.yForAltitude(a),0.000005);
    }
    
  

}
