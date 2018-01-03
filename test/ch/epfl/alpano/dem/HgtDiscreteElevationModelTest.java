package ch.epfl.alpano.dem;

import static org.junit.Assert.*;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;
import static java.lang.Math.toRadians;

import java.io.File;

import org.junit.Test;

public class HgtDiscreteElevationModelTest {

    @Test
    public void testCornerSW() {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        //System.out.println(7.+" "+46.);
        h.elevationSample(7*3600, 3600*46);     
    }
    
    @Test
    public void testCornerNW() {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        //System.out.println(7.+" "+47.);
        h.elevationSample((int)sampleIndex(toRadians(7)), (int)sampleIndex(toRadians(47)));
    }
    
    @Test
    public void testCornerSE() {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        //System.out.println(8.+" "+46.);
        h.elevationSample(8*3600,46*3600);
    }

    @Test
    public void testCornerNE() {
        HgtDiscreteElevationModel h = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
      //  System.out.println(8.+" "+47.);
        h.elevationSample(3600*8, 47*3600); 
    }
    
}
