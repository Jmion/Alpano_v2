package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PanoramaUserParametersBastien {

    @Test
    public void testAccesseur() {
        assertEquals(68087, PredefinedPanoramas.ALP_JURA.observerLongitude(),0);
        assertEquals(470085, PredefinedPanoramas.ALP_JURA.observerLatitude(),0);
        assertEquals(1380, PredefinedPanoramas.ALP_JURA.observerElevation(),0);
        assertEquals(162, PredefinedPanoramas.ALP_JURA.centerAzimuth(),0);
        assertEquals(27, PredefinedPanoramas.ALP_JURA.horizontalFieldOfView(),0);
        assertEquals(2500, PredefinedPanoramas.ALP_JURA.width(),0);
        assertEquals(800, PredefinedPanoramas.ALP_JURA.height(),0);
        assertEquals(300, PredefinedPanoramas.ALP_JURA.maxDistance(),0);
        assertEquals(0, PredefinedPanoramas.ALP_JURA.superSamplingExponent(),0);
    }
    
    @Test
    public void equalsTest1(){
        PanoramaUserParameters p = new PanoramaUserParameters(68_087,470_085,1380,162,27,300,2500,800,0);
        assertTrue(p.equals(PredefinedPanoramas.ALP_JURA));
    }

    @Test
    public void equalsTest2(){
        PanoramaUserParameters p = new PanoramaUserParameters(68_086,470_085,1380,162,27,300,2500,800,0);
        assertFalse(p.equals(PredefinedPanoramas.ALP_JURA));
    }
    
    @Test
    public void equalsTest3(){
        String t="toto";
        assertFalse(PredefinedPanoramas.ALP_JURA.equals(t));
    }
    
    @Test
    public void sanitizedMin(){
        PanoramaUserParameters underMin = new PanoramaUserParameters(59_999,449_999,299,-1,0,9,29,9,-1);
        PanoramaUserParameters min = new PanoramaUserParameters(60_000,450_000,300,0,1,10,30,10,0);
        assertTrue(min.equals(underMin));
    }
    
    @Test
    public void sanitizedMax(){
        PanoramaUserParameters overMax = new PanoramaUserParameters(12_001,480_001,10001,360,361,601,16001,4001,2);
        PanoramaUserParameters max = new PanoramaUserParameters(12_000,480_000,10000,359,360,600,16000,4000,2);
        assertTrue(max.equals(overMax));
    }
    
    @Test
    public void verticalFieldOfViewTest(){
        PanoramaUserParameters p = new PanoramaUserParameters(68_087,470_085,1380,162,2,300,30,4000,0);
        assertTrue(((p.height()-1)/(p.width()-1))*p.horizontalFieldOfView() <= 170);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void illegalConstruction1(){
        new PanoramaUserParameters(new HashMap<UserParameter, Integer>());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void illegalConstruction2(){
        Map<UserParameter, Integer> m = new HashMap<>();
        m.put(UserParameter.CENTER_AZIMUTH, 50);
        new PanoramaUserParameters(m);
    }
}