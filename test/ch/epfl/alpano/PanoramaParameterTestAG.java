package ch.epfl.alpano;
import static java.lang.Math.PI;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaParameterTestAG {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnAzimuth() {
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, 2.1*PI, PI/6, 30000, 2500, 800);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnObserver() {
        PanoramaParameters p = new PanoramaParameters(null, 3600, PI, PI/6, 30000, 2500, 800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnHorizontalView() {
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, 0, 30000, 2500, 800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnMaxDistance() {
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/6, 0, 2500, 800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnWidth() {
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/6, 30000, 0, 800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnHeight() {
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/6, 30000, 2500, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void azimuthForXthrowsException(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        p.azimuthForX(2500);
    }
    
   
    
    @Test(expected = IllegalArgumentException.class)
    public void xForAzimuthThrowsException(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800); 
        p.xForAzimuth(4*PI/3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void yForAltitudeThrowsException(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        p.yForAltitude(p.verticalFieldOfView());
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void altitudeForYthrowsException(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        p.altitudeForY(800);
    }
    
    @Test
    public void constructorWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, 2*PI, 30000, 2500, 800);
    }
    
   
    
    @Test
    public void azimuthForXWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        PanoramaParameters p2 = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, 11*PI/6 , PI, 30000, 2500, 800);
        assertEquals(5*PI/6, p.azimuthForX(0), 1e-7);
        assertEquals(7*PI/6, p.azimuthForX(2499), 1e-7);
        assertEquals(7*PI/6, p.azimuthForX(2499), 1e-7);
        assertEquals(3.4560871, p.azimuthForX(2000), 1e-7);
        assertEquals(3.0370405, p.azimuthForX(1000), 1e-7);
        assertEquals(PI/3, p2.azimuthForX(2499), 1e-7);
        
    }
    
    @Test
    public void xForAzimuthWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        assertEquals(1874.25, p.xForAzimuth(13*PI/12), 1e-7);
        assertEquals(624.75, p.xForAzimuth(11*PI/12), 1e-7);
        assertEquals(2499, p.xForAzimuth(7*PI/6), 1e-7);
    }
    
    @Test
    public void altitudeForYWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        assertEquals(0, p.altitudeForY(399.5), 1e-7);
        assertEquals(p.verticalFieldOfView()/2, p.altitudeForY(0), 1e-7);
        assertEquals(0.1485520336, p.altitudeForY(45), 1e-7);
    } 
   
    
    @Test
    public void yForAltitudeWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        assertEquals(0, p.yForAltitude(p.verticalFieldOfView()/2), 1e-7);
        assertEquals(799, p.yForAltitude(-(p.verticalFieldOfView()/2)), 1e-7);
    }
        
    
    @Test
    public void linearSampleIndexWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        assertEquals(1999999, p.linearSampleIndex(2499, 799),0);
        assertEquals(0, p.linearSampleIndex(0, 0), 0);
        assertEquals(955439 ,p.linearSampleIndex(439, 382), 0);
        assertEquals(1499601, p.linearSampleIndex(2101, 599),0);
    }
    
    @Test
    public void isValidWorks(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(PI, PI/2), 3600, PI, PI/3, 30000, 2500, 800);
        assertTrue(p.isValidSampleIndex(2400, 50));
        assertTrue(p.isValidSampleIndex(2499, 799));
        assertTrue(p.isValidSampleIndex(0, 0));
        assertFalse(p.isValidSampleIndex(2500, 40));
        assertFalse(p.isValidSampleIndex(400, 800));
    }

}
