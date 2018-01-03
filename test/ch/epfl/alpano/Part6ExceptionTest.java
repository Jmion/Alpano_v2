package ch.epfl.alpano;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

public class Part6ExceptionTest {
    
    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;
    final static PanoramaParameters PARAMS =
            new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                                                ORIGIN_LAT),
                                   ELEVATION,
                                   CENTER_AZIMUTH,
                                   HORIZONTAL_FOV,
                                   MAX_DISTANCE,
                                   IMAGE_WIDTH,
                                   IMAGE_HEIGHT);
    
    final static File HGT_FILE = new File("N46E007.hgt");
    final static DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
    final static ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
    final static Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);
    final static Builder b = new Builder(PARAMS);
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundPanoramaDistanceAt1() {
        p.distanceAt(501, 201);
    }

    @Test
    public void checkSampleIndexOutOfBoundPanoramaDistanceAt2() {
        assertEquals(15.2f, p.distanceAt(501, 201,15.2f),0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundPanoramaLongitudeAt() {
        p.longitudeAt(501, 201);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundPanoramaLattitudeAt() {
        p.latitudeAt(501, 201);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundPanoramaElevationAt() {
        p.elevationAt(501, 201);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundPanoramaSlopeAt() {
        p.slopeAt(501, 201);
    }
    
    @Test(expected = NullPointerException.class)
    public void checkConstructorBuilder() {
        new Panorama.Builder(null);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundBuilderSetDistanceAt() {
        b.setDistanceAt(501, 201, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundBuilderSetLongitudeAt() {
        b.setLongitudeAt(501, 201, 3);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundBuilderSetLattitudeAt() {
        b.setLatitudeAt(501, 201, 3);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundBuilderSetElevationAt() {
        b.setElevationAt(501,201, 2);
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void checkSampleIndexOutOfBoundBuilderSetSlopeAt() {
        b.setSlopeAt(501, 201, 2);
    }
    
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderSetDistanceAt() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.setDistanceAt(100, 150, 4);
    }
    
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderSetLongitudeAt() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.setLongitudeAt(100, 150, 4);
    }
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderSetLattitudeAt() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.setLatitudeAt(100, 150, 4);
    }
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderSetElevationAt() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.setElevationAt(100, 150, 4);
    }
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderSetSlopeAt() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.setSlopeAt(100, 150, 4);
    }
    @Test(expected = IllegalStateException.class)
    public void checkIllegalStateBuilderBuild() {
        Builder b2 = new Builder(PARAMS);
        b2.build();
        b2.build();
    }
    
    @Test(expected = NullPointerException.class)
    public void checkPanoramaComputerConstructor() {
        new PanoramaComputer(null);
    }
}