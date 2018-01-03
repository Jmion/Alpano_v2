/*
 *	Author:      Martin Cibils
 *	Date:        3 avr. 2017
 */


package ch.epfl.alpano.summit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GazetteerParserTest.class, GazetteerParserTest__Global.class,
        SummitTest.class, SummitTest__Global.class,
        TestingGazetteerParser.class })
public class AllTests {

}
