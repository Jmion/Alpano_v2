package ch.epfl.alpano.dem;

import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;
import static java.lang.Math.toRadians;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class ExceptionPart4Test {

    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName1() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006"));
        h.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName2() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E06.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName3() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("U45E006.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName4() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("S45X06.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName5() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E06.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName6() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("T45E06.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName7() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N48806.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildHGTWrongName8() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006_testFalseLength.hgt"));
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testElevationSampleOut() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        
        h.elevationSample((int)sampleIndex(toRadians(8)), (int)sampleIndex(toRadians(49)));
        
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildElevationProfile1() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        new ElevationProfile(cDEM, o, toRadians(-90), 50);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildElevationProfile2() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        new ElevationProfile(cDEM, o, toRadians(30), 0);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBuildElevationProfile3() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        new ElevationProfile(cDEM, o, toRadians(30), -1);
        h.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testBuildElevationProfile4() throws Exception {
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        new ElevationProfile(null, o, toRadians(30), 50);
    }
    
    @Test(expected = NullPointerException.class)
    public void testBuildElevationProfile5() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        new ElevationProfile(cDEM, null, toRadians(30), 50);
        h.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testBuildElevationProfile6() throws Exception {
        new ElevationProfile(null, null, toRadians(30), 50);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testElevationAt1() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.elevationAt(41);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testElevationAt2() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.elevationAt(-1);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPositionAt1() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.positionAt(185);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPosition2() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.positionAt(-8);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSlopeAt1() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.elevationAt(42);
        h.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSlopeAt2() throws Exception {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        ContinuousElevationModel cDEM = new ContinuousElevationModel(h);
        GeoPoint o = new GeoPoint(toRadians(45), toRadians(45));
        ElevationProfile p = new ElevationProfile(cDEM, o, toRadians(30), 40);
        p.elevationAt(-2);
        h.close();
    }
}
