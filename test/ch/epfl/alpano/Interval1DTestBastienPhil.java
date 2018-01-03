package ch.epfl.alpano;

import static ch.epfl.test.ObjectTest.hashCodeIsCompatibleWithEquals;
import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTestBastienPhil {

    @Test (expected = IllegalArgumentException.class)
    public void testConstructeur() {
        new Interval1D(6,0);
    }
    
    @Test
    public void testConstructeurOk(){
        new Interval1D (0,6);
    }
    
    @Test 
    public void testIncludedFrom(){
        Interval1D interval = new Interval1D(0,6);
        assertEquals(0, interval.includedFrom(), 0);
    }
    
    @Test
    public void testIncludedTo() {
        Interval1D interval = new Interval1D(0,6);
        assertEquals(6, interval.includedTo(), 0);
    }
    
    @Test
    public void testContains(){
        Interval1D interval = new Interval1D(0,6);
        assertTrue(interval.contains(5));
    }
    
    @Test
    public void testContainsUp(){
        Interval1D interval = new Interval1D(0,6);
        assertTrue(interval.contains(6));
    }
    
    @Test
    public void testContainsDown(){
        Interval1D interval = new Interval1D(0,6);
        assertTrue(interval.contains(0));
    }
    
    @Test
    public void testContainsWrong(){
        Interval1D interval = new Interval1D(0,6);
        assertFalse(interval.contains(8));
    }
    
    @Test
    public void testSize(){
        Interval1D interval = new Interval1D(0,3);
        assertEquals(4, interval.size(), 0);
    }
    
    @Test
    public void testSizeZero(){
        Interval1D interval = new Interval1D(0,0);
        assertEquals(1, interval.size(), 0);
    }
    
    @Test
    public void testSizeOfIntersectionWith(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(1,6);
        assertEquals(3, interval.sizeOfIntersectionWith(interval2),0);
    }
    
    @Test
    public void testSizeOfIntersectionWith2(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(3,6);
        assertEquals(1, interval.sizeOfIntersectionWith(interval2),0);
    }
    
    @Test
    public void testSizeOfIntersectionWith3(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(0,3);
        assertEquals(4, interval.sizeOfIntersectionWith(interval2),0);
    }
    
    @Test
    public void testSizeOfIntersectionWith4(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(4,6);
        assertEquals(0, interval.sizeOfIntersectionWith(interval2),0);
    }
    
    @Test
    public void testBoundingUnion(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(6,7);
        Interval1D interval3 = new Interval1D(0,7);
        assertEquals(interval3, interval.boundingUnion(interval2));
    }
    
    @Test
    public void testBoundingUnion2(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(6,7);
        Interval1D interval3 = new Interval1D(0,7);
        assertEquals(interval3, interval2.boundingUnion(interval));
    }
    
    @Test
    public void testBoundingUnion3(){
        Interval1D interval = new Interval1D(0,12);
        Interval1D interval2 = new Interval1D(6,7);
        Interval1D interval3 = new Interval1D(0,12);
        assertEquals(interval3, interval.boundingUnion(interval2));
    }
    
    @Test
    public void testBoundingUnion4(){
        Interval1D interval = new Interval1D(0,10);
        Interval1D interval2 = new Interval1D(6,7);
        Interval1D interval3 = new Interval1D(0,10);
        assertEquals(interval3, interval2.boundingUnion(interval));
    }
    
    @Test
    public void testIsUnionableWith(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(2,7);
        assertTrue(interval.isUnionableWith(interval2));
    }
    
    @Test
    public void testIsUnionableWith2(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(0,3);
        assertTrue(interval.isUnionableWith(interval2));
    }
    
    @Test
    public void testIsUnionableWith3(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(3,7);
        assertTrue(interval.isUnionableWith(interval2));
    }
    
    @Test
    public void testIsUnionableWith5(){
        Interval1D interval = new Interval1D(0,0);
        Interval1D interval2 = new Interval1D(0,7);
        assertTrue(interval.isUnionableWith(interval2));
    }
    
    @Test
    public void testIsUnionableWithWrong(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(5,7);
        assertFalse(interval.isUnionableWith(interval2));
    }
    
    @Test
    public void testUnion(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(2,5);
        Interval1D interval3 = new Interval1D(0,5);
        assertEquals(interval3, interval.union(interval2));
    }
    
    @Test
    public void testUnion2(){
        Interval1D interval = new Interval1D(0,0);
        Interval1D interval2 = new Interval1D(0,5);
        assertEquals(interval2, interval.union(interval2));
    }
    
    @Test
    public void testUnion3(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(3,5);
        Interval1D interval3 = new Interval1D(0,5);
        assertEquals(interval3, interval.union(interval2));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testUnionException(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(6,7);
        interval.union(interval2);
    }
    
    @Test
    public void testEquals(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(0,3);
        assertTrue(interval.equals(interval2));
    }
    
    @Test
    public void testEqualsWrong(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(5,8);
        assertFalse(interval.equals(interval2));
    }
    
    @Test
    public void testHashCode(){
        Interval1D interval = new Interval1D(0,3);
        Interval1D interval2 = new Interval1D(5,8);
        assertTrue(hashCodeIsCompatibleWithEquals(interval, interval2));
    }
    
    @Test
    public void testToString(){
        Interval1D interval = new Interval1D(0,3);
        System.out.println(interval.toString());
        assertTrue(interval.toString().equals("[0..3]"));
    }
}
