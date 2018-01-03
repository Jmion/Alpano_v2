package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChannelPainterTest {

    @Test
    public void testAdd() {
       ChannelPainter channel = (x,y)->1;
       
       ChannelPainter addition = channel.add(1000);
       
       for(int x=0;x<100;++x)
           for(int y=0;y<100;++y)
               assertEquals(1001, addition.valueAt(x, y),0);
    }

    @Test
    public void testSub() {
       ChannelPainter channel = (x,y)->40;
       
       ChannelPainter substraction = channel.sub(10);
       
       for(int x=0;x<100;++x)
           for(int y=0;y<100;++y)
               assertEquals(30, substraction.valueAt(x, y),0);
    }
    
    @Test
    public void testMul() {
       ChannelPainter channel = (x,y)->40;
       
       ChannelPainter multiplication = channel.mul(2);
       
       for(int x=0;x<100;++x)
           for(int y=0;y<100;++y)
               assertEquals(80, multiplication.valueAt(x, y),0);
    }
    
    @Test
    public void testDiv() {
       ChannelPainter channel = (x,y)->40;
       
       ChannelPainter division = channel.div(2);
       
       for(int x=0;x<100;++x)
           for(int y=0;y<100;++y)
               assertEquals(20, division.valueAt(x, y),0);
    }
    
    @Test
    public void testClamp() {
        ChannelPainter channel = (x,y)->0.1f;
        ChannelPainter clamped = channel.clamped();
        
        for(int x=0;x<100;++x)
            for(int y=0;y<100;++y)
                assertEquals(0.1f, clamped.valueAt(x, y),0);
    }
    
}