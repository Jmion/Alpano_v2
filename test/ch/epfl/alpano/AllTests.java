/*
 *	Author:      Martin Cibils
 *	Date:        3 avr. 2017
 */


package ch.epfl.alpano;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AzimuthTest.class, CompositeDiscreteElevationModelTest.class,
        ContinuousElevationModelTest.class, DistanceTest.class,
        ElevationProfileTest.class, GeoPointTestBastienPhil.class,
        GeoPointTestProf.class, GeoPointTestSuperficial.class,
        HgtDiscreteElevationModelTest.class, Interval1DTest.class,
        Interval1DTestBastienPhil.class, Interval1DTestProf.class,
        Interval2DTest.class, Interval2DTestBastienPhil.class,
        Interval2DTestProf.class, Math2Test.class, PanoramaComputerTest.class,
        PanoramaParametersTest.class, PanoramaParameterTestAG.class,
        PanoramaTest.class, Part6ExceptionTest.class, PreconditionsTest.class })
public class AllTests {

}
