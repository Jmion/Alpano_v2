package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.test.ObjectTest;

public class Interval1DTest {
    
    Interval1D i1 = new Interval1D(0, 25);
    Interval1D i2 = new Interval1D(10, 50);
    Interval1D i3 = new Interval1D(10, 10);
    Interval1D i4 = new Interval1D(25, 25);
    Interval1D i5 = new Interval1D(51, 60);

    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailIfIncludedFromBiggerThanIncludedTo(){
        new Interval1D(10, 5);
    }
    
    @Test
    public void constructorWorksOnNormalCase(){
        try{
            new Interval1D(5,7);
        }catch(Exception e){
            fail("Error with the creation of an intervall");
        }
        assertTrue(true);
    }
    
    @Test
    public void includedFromTest(){
        assertEquals(0, i1.includedFrom());
        assertEquals(10, i2.includedFrom());
        assertEquals(10, i3.includedFrom());
    }
    
    @Test
    public void includedToTest() {
        assertEquals(25, i1.includedTo());
        assertEquals(50, i2.includedTo());
        assertEquals(10, i3.includedTo());
    }
    
    @Test
    public void containsTest(){
        assertFalse(i1.contains(30));
        assertTrue(i1.contains(10));
        assertTrue(i1.contains(0));
        assertTrue(i1.contains(0));
        assertTrue(i1.contains(25));
        
        assertTrue(i3.contains(10));
        assertFalse(i3.contains(0));
        assertFalse(i3.contains(25));
    }
   
    @Test
    public void sizeTest() {
        assertEquals(26, i1.size());
        assertEquals(1, i3.size());
        assertEquals(41, i2.size());
    }
    
    @Test
    public void sizeOfIntersectionWithTest() {
        assertEquals(16, i1.sizeOfIntersectionWith(i2));
        assertEquals(16, i2.sizeOfIntersectionWith(i1));
        assertEquals(1, i1.sizeOfIntersectionWith(i3));
        assertEquals(1, i2.sizeOfIntersectionWith(i3));
        assertEquals(1, i3.sizeOfIntersectionWith(i3));
        assertEquals(1, i1.sizeOfIntersectionWith(i4));
        assertEquals(1, i4.sizeOfIntersectionWith(i1));
    }
    
    @Test
    public void boundingUnionTest() {
        assertEquals(new Interval1D(0, 50), i1.boundingUnion(i2));
        assertEquals(new Interval1D(0, 50), i2.boundingUnion(i1));
        assertEquals(i3, i3.boundingUnion(i3));
        assertEquals(i1, i1.boundingUnion(i1));
        assertEquals(i1, i1.boundingUnion(i4));
        assertEquals(i1, i4.boundingUnion(i1));
    }
    
    @Test
    public void equalsTest() {
        assertEquals(i1, new Interval1D(0, 25));
        assertEquals(i3, new Interval1D(10, 10));
    }
    
    @Test
    public void isUnionableWith() {
        assertTrue(i1.isUnionableWith(i4));
        assertTrue(i4.isUnionableWith(i1));
        assertTrue(i2.isUnionableWith(i3));
        assertFalse(i3.isUnionableWith(i4));
        assertFalse(i4.isUnionableWith(i3));
        assertTrue(i2.isUnionableWith(i5));
        assertTrue(i2.isUnionableWith(i2));
    }
    
    @Test
    public void unionTest() {
        Interval1D l1 = new Interval1D(10, 10), l2 = new Interval1D(11, 11);
        assertEquals(new Interval1D(10, 11), l1.union(l2));
        assertEquals(new Interval1D(10, 60), i2.union(i5));
    }
    
    @Test (expected = IllegalArgumentException.class )
    public void unionTestFail(){
        i1.union(i5);
    }
    
    @Test
    public void equalsCompatibleWithHashCode() {
        ObjectTest.hashCodeIsCompatibleWithEquals(new Interval1D(10, 10), new Interval1D(10, 10));
    }
    
    @Test
    public void toStringTest() {
        assertEquals("[0..25]", i1.toString());
        assertEquals("[10..10]", i3.toString());
    }

}
