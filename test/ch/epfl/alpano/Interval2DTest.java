package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.test.ObjectTest;

public class Interval2DTest {
    Interval2D i1 = new Interval2D(new Interval1D(1,5), new Interval1D(5,7));
    Interval2D i2 = new Interval2D(new Interval1D(0,0), new Interval1D(1,3));
    Interval2D i3 = new Interval2D(new Interval1D(3,7), new Interval1D(1,1));
    Interval2D i4 = new Interval2D(new Interval1D(1,1), new Interval1D(1,1));
    
    Interval2D i5 = new Interval2D(new Interval1D(2,7), new Interval1D(6,7));
    Interval2D i6 = new Interval2D(new Interval1D(3,3), new Interval1D(3,3));
    
    Interval2D i7 = new Interval2D(new Interval1D(0,3), new Interval1D(1,3));
    Interval2D i8 = new Interval2D(new Interval1D(0,3), new Interval1D(1,4));
    
    Interval2D i9 = new Interval2D(new Interval1D(0,4), new Interval1D(1,3));
    Interval2D i10 = new Interval2D(new Interval1D(0,3), new Interval1D(1,4));
    
    Interval2D[] i2dArray = {
            //case1
            new Interval2D(new Interval1D(4,4), new Interval1D(4,4)),
            new Interval2D(new Interval1D(4,4), new Interval1D(4,4)),
            //case2
            new Interval2D(new Interval1D(0,0), new Interval1D(0,0)),
            new Interval2D(new Interval1D(4,4), new Interval1D(4,4)),
            //case3
            new Interval2D(new Interval1D(0,10), new Interval1D(0,6)),
            new Interval2D(new Interval1D(4,8), new Interval1D(4,8)),
            //case4
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(2,6), new Interval1D(-3,1)),
            //case5
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(0,2), new Interval1D(0,2)),
            //case6
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(4,8), new Interval1D(4,8)),
            //case7
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(0,4), new Interval1D(2,8)),
            //case8
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(4,10), new Interval1D(0,4)),
            //case9
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(4,8), new Interval1D(0,4)),
            //case10
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(3,7), new Interval1D(-1,4)),
            //case11
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(4,8), new Interval1D(1,3)),
            //case12
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(7,10), new Interval1D(0,4)),
            //case12.2 same as 12 but in the y axis
            new Interval2D(new Interval1D(0,4), new Interval1D(0,4)),
            new Interval2D(new Interval1D(0,4), new Interval1D(7,10))
    };
    
    boolean[] isUnionableAwnsers ={
            true,false,false,false,true,false,true,true,true,false,false,false,false
    };
   

    @Test(expected = NullPointerException.class)
    public void constructorStopsWorkingWhenWithNullPointerExcpetion(){
        new Interval2D(null, new Interval1D(5, 10));
        new Interval2D(new Interval1D(1, 3), null);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithIllegalArgument(){
        new Interval1D(5, 4);
    }
    
    
    @Test
    public void iXReturnsTheRightValue(){
        assertEquals(new Interval1D(1, 5), i1.iX());
        assertEquals(new Interval1D(0, 0), i2.iX());
        assertEquals(new Interval1D(3, 7), i3.iX());
    }
    
    
    @Test 
    public void iYReturnsTheRightValue(){
        assertEquals(new Interval1D(5, 7), i1.iY());
        assertEquals(new Interval1D(1, 3), i2.iY());
        assertEquals(new Interval1D(1, 1), i3.iY());
        assertEquals(new Interval1D(1, 1), i4.iY());
    }
    
    
    @Test
    public void containsTest(){
        assertTrue(i1.contains(3, 6));
        assertFalse(i2.contains(1, 2));
        assertTrue(i2.contains(0, 2));
        assertFalse(i2.contains(0, 4));
        assertTrue(i4.contains(1, 1));
    }
    
    @Test
    public void sizeTest() {
        assertEquals(3, i2.size());
        assertEquals(15, i1.size());
        assertEquals(1 , i4.size());
    }
    
    @Test
    public void sizeOfIntersectionWithTest(){
        assertEquals(0, i1.sizeOfIntersectionWith(i4));
        assertEquals(0, i1.sizeOfIntersectionWith(i2));
        assertEquals(8, i1.sizeOfIntersectionWith(i5));
        assertEquals(i1.sizeOfIntersectionWith(i4), i4.sizeOfIntersectionWith(i1));
        assertEquals(8, i5.sizeOfIntersectionWith(i1));
        
    }
    
    @Test
    public void boundingUnionTest() {
        assertEquals(new Interval2D(new Interval1D(1,3), new Interval1D(1,3)), i4.boundingUnion(i6));
        assertEquals(new Interval2D(new Interval1D(1,7), new Interval1D(5,7)), i1.boundingUnion(i5));
        assertEquals(i4, i4.boundingUnion(i4));
    }
    
    @Test
    public void isUnionableWithTestBaclé() {
        Interval2D i11 = new Interval2D(new Interval1D(0,4), new Interval1D(0,4));
        Interval2D i12 = new Interval2D(new Interval1D(0,3), new Interval1D(0,3));
        
        assertFalse(i4.isUnionableWith(i6));
        assertTrue(i7.isUnionableWith(i8));
        assertFalse(i9.isUnionableWith(i10));
        assertTrue(i11.isUnionableWith(i12));
    }
    
    
    @Test
    public void isUnionableWithTestComplete(){
        for(int i = 0; i<i2dArray.length; i+=2){
            assertEquals(isUnionableAwnsers[i/2], i2dArray[i].isUnionableWith(i2dArray[i+1]));
            assertEquals(isUnionableAwnsers[i/2], i2dArray[i+1].isUnionableWith(i2dArray[i]));
        }
    }
    
    
    @Test
    public void hashCodeIsCompatibleWithEquals() {
        ObjectTest.hashCodeIsCompatibleWithEquals(i2dArray[0], i2dArray[1]);
    }
    
    @Test
    public void unionTestCase1() {
        assertEquals(i2dArray[0].union(i2dArray[1]), i2dArray[0]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase2() {
        i2dArray[2].union(i2dArray[3]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase3() {
        i2dArray[4].union(i2dArray[5]);
       
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase4() {
        i2dArray[6].union(i2dArray[7]);
    }
    
    @Test
    public void unionTestCase5() {
        assertEquals(i2dArray[8], i2dArray[8].union(i2dArray[9]));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase6() {
        i2dArray[10].union(i2dArray[11]);
    }
    
    @Test
    public void unionTestCase7() {
        assertEquals(new Interval2D(new Interval1D(0,4), new Interval1D(0,8)), i2dArray[12].union(i2dArray[13]));
    }
    
    @Test
    public void unionTestCase8() {
        assertEquals(new Interval2D(new Interval1D(0,10), new Interval1D(0,4)), i2dArray[14].union(i2dArray[15]));
        assertEquals(new Interval2D(new Interval1D(0,10), new Interval1D(0,4)), i2dArray[15].union(i2dArray[14]));
    }
    
    @Test
    public void unionTestCase9() {
        assertEquals(new Interval2D(new Interval1D(0,8), new Interval1D(0,4)), i2dArray[16].union(i2dArray[17]));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase10() {
        i2dArray[18].union(i2dArray[19]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase11() {
        i2dArray[20].union(i2dArray[21]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionTestCase12() {
        i2dArray[22].union(i2dArray[23]);
    }
    
    
    
    
    
    
    
    
}
