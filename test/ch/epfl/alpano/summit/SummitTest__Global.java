package ch.epfl.alpano.summit;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.summit.Summit;


public class SummitTest__Global {
	GeoPoint eigerPosition = new GeoPoint(Math.toRadians(8.0053),Math.toRadians(46.5775));
	Summit eiger = new Summit("EIGER", eigerPosition, 3970);
	Summit defaultSummit = new Summit("test", new GeoPoint(0, 0), 0);
	
	@Test
	public void testConstructor() {
		new Summit("test", new GeoPoint(0, 0), 0);
	}
	
	@Test
	public void testNameAccessor() {
		assertEquals("EIGER", eiger.name());
	}
	
	@Test
	public void testElevationAccessor() {
		assertEquals(3970, eiger.elevation());
	}
	
	@Test
	public void testPositionAccessor() {
		assertEquals(eigerPosition, eiger.position());
	}
	
	@Test
	public void testToString() {
		assertEquals("EIGER (8.0053,46.5775) 3970", eiger.toString());
	}
}