package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import static java.lang.Math.*;
import static ch.epfl.alpano.Math2.*;

final class WavyDEM implements DiscreteElevationModel {
  private final static double PERIOD = 100, HEIGHT = 1000;
  private final Interval2D extent;

  public WavyDEM(Interval2D extent) {
    this.extent = extent;
  }

  @Override
  public void close() throws Exception { }

  @Override
  public Interval2D extent() { return extent; }

 

    @Override
    public double elevationSample(int x, int y) {
        double x1 = PI * 2d * x / PERIOD;
        double y1 = PI * 2d * y / PERIOD;
          return (1 + sin(x1) * cos(y1)) / 2d * HEIGHT;
//        return (sin(10*(sq(x1-3)+sq(y1-3)))/10) / 2d * HEIGHT;
//        return (1/(15*(sq(x1-3)+sq(y1-3)))) / 2d * HEIGHT;
//        return (sin(5*x1)*cos(5*y1)/5) / 2d * HEIGHT;
//        return (sqrt(1/sq(x1-2) + sq(y1-4))) / 2d * HEIGHT;
//        return (sqrt(1/sq(x1-2) + sq(y1-4))) / 2d * HEIGHT; 
  }
}