package ch.epfl.alpano;

import static ch.epfl.test.ObjectTest.hashCodeIsCompatibleWithEquals;
import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTestBastienPhil {

    @Test
    public void testConstructeur() {
        Interval1D i1 = new Interval1D (0,3);
        Interval1D i2 = new Interval1D (2,4);
        new Interval2D (i1, i2);
    }
    
    @Test (expected = NullPointerException.class)
    public void testConstructeurException() {
        Interval1D i1 = null;
        Interval1D i2 = new Interval1D (0,4);
        new Interval2D (i1, i2);
    }
    
    @Test
    public void testConstructeur2() {
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (2,4);
        new Interval2D (i1, i2);
    }
    
    @Test
    public void testConstructeur3() {
        Interval1D i1 = new Interval1D (0,3);
        Interval1D i2 = new Interval1D (2,4);
        new Interval2D (i2, i1);
    }
    
    @Test
    public void testConstructeur4() {
        Interval1D i1 = new Interval1D (0,3);
        new Interval2D (i1, i1);
    }
    
    @Test
    public void testIX(){
        Interval1D i1 = new Interval1D (0,3);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        Interval1D i4 = new Interval1D (5,10);
        Interval1D i5 = new Interval1D (8,14);

        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i4,i3);
        Interval2D I3 = new Interval2D (i1,i5);
        
        assertEquals(i1, I1.iX());
        assertEquals(i4, I2.iX());
        assertEquals(i1, I3.iX());
    }
    
    @Test
    public void testIY(){
        Interval1D i1 = new Interval1D (0,3);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
       
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        
        assertEquals(i2, I1.iY());
        assertEquals(i2, I2.iY());
        assertEquals(i1, I3.iY());
    }
    
    @Test
    public void testContains(){
        Interval1D i1 = new Interval1D (0,3);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        
        assertTrue(I1.contains(0, 7));
        assertFalse(I1.contains(4, 5));
        assertTrue(I2.contains(10, 5));
        assertFalse(I2.contains(1, 10));
        assertTrue(I3.contains(10, 2));
        assertFalse(I3.contains(1, 2));
        
    }
    
    @Test
    public void testSize(){
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        
        assertEquals(4, I1.size());
        assertEquals(44, I2.size());
        assertEquals(11, I3.size());
    }
    
    @Test
    public void testSizeOfIntersectionWith(){
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        Interval1D i4 = new Interval1D (5, 8);
        
        Interval1D i5 = new Interval1D (0,5);
        Interval1D i6 = new Interval1D (2,4);
        Interval1D i7 = new Interval1D (0,5);
        Interval1D i8 = new Interval1D (2,5);
        
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        Interval2D I4 = new Interval2D (i3,i4);
        
        Interval2D I5 = new Interval2D (i5,i6);
        Interval2D I6 = new Interval2D (i7,i8);
        
        assertEquals(18, I5.sizeOfIntersectionWith(I6));
        
        assertEquals(0, I1.sizeOfIntersectionWith(I2));
        assertEquals(0, I2.sizeOfIntersectionWith(I3));
        assertEquals(33, I2.sizeOfIntersectionWith(I4));
    }
    
    @Test
    public void testBoundingUnion(){
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        Interval1D i4 = new Interval1D (5, 8);
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        new Interval2D (i3,i1);
        Interval2D I4 = new Interval2D (i3,i4);
        
        assertEquals(new Interval2D(new Interval1D(2,12), new Interval1D(4,8)), I2.boundingUnion(I4));
        assertEquals(new Interval2D(new Interval1D(0,12), new Interval1D(4,7)), I1.boundingUnion(I2));
    }
    
    @Test
    public void testIsUnionableWith(){
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        Interval1D i4 = new Interval1D (5, 8);
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        Interval2D I4 = new Interval2D (i3,i4);
        
        assertFalse(I1.isUnionableWith(I2));
        assertFalse(I2.isUnionableWith(I3));
        assertTrue(I2.isUnionableWith(I4));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testUnionException(){
        Interval1D i1 = new Interval1D (0,0);
        Interval1D i2 = new Interval1D (4,7);
        Interval1D i3 = new Interval1D (2,12);
        new Interval1D (5, 8);
        
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i3,i2);
        Interval2D I3 = new Interval2D (i3,i1);
        
        I1.union(I2);
        I2.union(I3);
    }
    
    @Test
    public void testUnion(){
        Interval1D i1 = new Interval1D (0,5);
        Interval1D i2 = new Interval1D (2,4);
        Interval2D I1 = new Interval2D (i1,i2);
        
        Interval1D i3 = new Interval1D (0,5);
        Interval1D i4 = new Interval1D (2,5);
        Interval2D I2 = new Interval2D (i3,i4);
        
        assertEquals(new Interval2D(new Interval1D(0,5), new Interval1D(2,5)), I1.union(I2));
    }
    
    @Test
    public void testUnion2(){
        Interval1D i1 = new Interval1D (2,5);
        Interval1D i2 = new Interval1D (2,4);
        Interval2D I1 = new Interval2D (i1,i2);
        
        Interval1D i3 = new Interval1D (5,10);
        Interval1D i4 = new Interval1D (2,4);
        Interval2D I2 = new Interval2D (i3,i4);
        
        assertEquals(new Interval2D(new Interval1D(2,10), new Interval1D(2,4)), I1.union(I2));
    }
    
    @Test
    public void testUnion3(){
        Interval1D i1 = new Interval1D (5,10);
        Interval1D i2 = new Interval1D (2,4);
        Interval2D I1 = new Interval2D (i1,i2);
        
        Interval1D i3 = new Interval1D (5,10);
        Interval1D i4 = new Interval1D (2,4);
        Interval2D I2 = new Interval2D (i3,i4);
        
        assertEquals(new Interval2D(new Interval1D(5,10), new Interval1D(2,4)), I1.union(I2));
    }
    
    
    
    @Test
    public void testEqualsOK(){
        Interval1D i1 = new Interval1D (0,5);
        Interval1D i2 = new Interval1D (2,4);
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i1, i2);
        assertTrue(I1.equals(I2));
    }
    
    @Test
    public void testEqualsWrong(){
        Interval1D i1 = new Interval1D (0,5);
        Interval1D i2 = new Interval1D (2,4);
        Interval2D I1 = new Interval2D (i1,i2);
        Interval2D I2 = new Interval2D (i1, new Interval1D(3,5));
        assertFalse(I1.equals(I2));
    }
    
    @Test
    public void testHashCode(){
        Interval1D i = new Interval1D(0,3);
        Interval1D i2 = new Interval1D(5,8);
        Interval2D I1 = new Interval2D (i, i2);
        Interval2D I2 = new Interval2D (i, new Interval1D(4,7));
        assertTrue(hashCodeIsCompatibleWithEquals(I1, I2));
    }
    
    @Test
    public void testToString(){
        Interval1D i1 = new Interval1D(0,3);
        Interval1D i2 = new Interval1D (5,8);
        Interval2D I1 = new Interval2D(i1, i2);
        System.out.println(I1.toString());
        assertTrue(I1.toString().equals("[0..3]x[5..8]"));
    }
}