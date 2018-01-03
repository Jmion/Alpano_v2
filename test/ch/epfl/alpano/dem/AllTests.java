/*
 *	Author:      Martin Cibils
 *	Date:        3 avr. 2017
 */


package ch.epfl.alpano.dem;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ElevationProfileTest.class, ExceptionPart4Test.class,
        HgtDiscreteElevationModelTest.class, PanoramaParametersTestMJ.class })
public class AllTests {

}
