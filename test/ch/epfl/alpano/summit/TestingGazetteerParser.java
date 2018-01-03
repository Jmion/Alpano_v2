/*
 *	Author:      Martin Cibils
 *	Date:        17 mars 2017
 */


package ch.epfl.alpano.summit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public class TestingGazetteerParser {
    @Test
    public void rightSummitWithFirstValue() throws IOException{
        List<Summit> tab = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        assertEquals("MONTE CURT", tab.get(0).name());
        assertEquals(tab.get(0).elevation(), 1325);
        assertEquals(Math.toRadians(7.42),tab.get(0).position().longitude(),0.05);
        assertEquals(Math.toRadians(48.14027778),tab.get(0).position().latitude(), 0.07);
    }
    @Test
    public void rightSummitWithSecondValue() throws IOException{
       List<Summit> tab = GazetteerParser.readSummitsFrom(new File("alps.txt"));
       assertEquals("TRUC DEL FARO", tab.get(1).name());
       assertEquals(1206, tab.get(1).elevation());
       assertEquals(Math.toRadians(7.41), tab.get(1).position().longitude(),0.05);
       assertEquals(Math.toRadians(45.156),tab.get(1).position().latitude(), 0.07);
       
    }
    @Test(expected = UnsupportedOperationException.class)
    public void cantChangeTheList() throws IOException{
        List<Summit> tab = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        tab.remove(5);
    }



}
