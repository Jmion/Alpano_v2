package ch.epfl.alpano.gui;

import static org.junit.Assert.*;
import java.util.*;
import java.util.EnumMap;
import java.util.HashMap;

import org.junit.Test;
public class PanoramaUserParametersTest {
    private static Map<UserParameter, Integer> myEnumMap = new EnumMap<>(UserParameter.class);
    private static PanoramaUserParameters myPanFirst;
    private static PanoramaUserParameters myPanSecond;
    @Test
    public void hasRightValuesWithFirstConstructor(){
        myEnumMap.put(UserParameter.OBSERVER_LONGITUDE, 500_000);
        myEnumMap.put(UserParameter.OBSERVER_LATITUDE, 430_000);
        myEnumMap.put(UserParameter.OBSERVER_ELEVATION, 10_001);
        myEnumMap.put(UserParameter.CENTER_AZIMUTH, 360);
        myEnumMap.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, 155);
        myEnumMap.put(UserParameter.MAX_DISTANCE, 895465);
        myEnumMap.put(UserParameter.WIDTH, 16_001);
        myEnumMap.put(UserParameter.HEIGHT, 9);
        myEnumMap.put(UserParameter.SUPER_SAMPLING_EXPONENT, 1);
        myPanFirst = new PanoramaUserParameters(myEnumMap);
        assertEquals(120_000, myPanFirst.observerLongitude(), 0);
        assertEquals(450_000, myPanFirst.observerLatitude(), 0);
        assertEquals(10_000, myPanFirst.observerElevation(), 0);
        assertEquals(359, myPanFirst.centerAzimuth(), 0);
        assertEquals(155, myPanFirst.horizontalFieldOfView(), 0);
        assertEquals(600, myPanFirst.maxDistance(), 0);
        assertEquals(16_000, myPanFirst.width(), 0);
        assertEquals(10, myPanFirst.height(), 0);
    }
    @Test
    public void hasRightValuesWithSecondCOnstructor(){
        myPanSecond = new PanoramaUserParameters(500_000, 430_000, 10_001, 360, 155, 895465, 16_001, 9 , 1);
        assertEquals(120_000, myPanSecond.observerLongitude(), 0);
        assertEquals(450_000, myPanSecond.observerLatitude(), 0);
        assertEquals(10_000, myPanSecond.observerElevation(), 0);
        assertEquals(359, myPanSecond.centerAzimuth(), 0);
        assertEquals(155, myPanSecond.horizontalFieldOfView(), 0);
        assertEquals(600, myPanSecond.maxDistance(), 0);
        assertEquals(16_000, myPanSecond.width(), 0);
        assertEquals(10, myPanSecond.height(), 0);
    }

}
