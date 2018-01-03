
package ch.epfl.alpano.gui;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * ChannelPainters allow for operations on panorama. It provides all the basic mathematical operations. This class is meant to
 * describe a component such as
 * <ul>
 * <li> brightness</li>
 * <li> hue</li>
 * <li> saturation</li>
 * <li> opacity</li>
 * <li> etc.</li>
 * </ul>
 * Various default methods have been written in order to modify the value with basic mathematical operations.
 * 
 * 
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

@FunctionalInterface
public interface ChannelPainter {
    /**
     * Gives the value of a canal on a point with coordinate x and y.
     * @param x The x-axis coordinate.
     * @param y The y-axis coordinate.
     * @return The float value of the canal of the point.
     */
    float valueAt(int x, int y);
    /**
     * Gives a new ChannelPainter which override valueAt. For each pixel, it will return the difference
     * of distance between the point itself and the neighbor that has the highest distance. If a point
     * is on the side of the panorama, the neighbors that are outside will have the distance equals to 
     * zero.
     * @param p The panorama we take the informations from.
     * @return A new ChannelPainter
     */
    static ChannelPainter maxDistanceToNeighbors(Panorama p){
        return (x,y) -> max(max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0)), max(p.distanceAt(x-1, y, 0), p.distanceAt(x, y-1, 0))) - p.distanceAt(x, y, 0);
    }
    /**
     * Adds a constant to the value of the method valueAt of the ChannelPainter.
     * @param constant The constant.
     * @return A new ChannelPainter.
     */
    default ChannelPainter add(float constant){
        return (x,y) -> this.valueAt(x, y)+constant;
    }
    /**
     * Subtract a constant to the value of the method valueAt of the ChannelPainter.
     * @param constant The constant.
     * @return A new ChannelPainter.
     */
    default ChannelPainter sub(float constant){
        return (x,y) -> this.valueAt(x,y)-constant;
    }
    /**
     * Multiply a constant to the value of the method valueAt of the ChannelPainter.
     * @param constant The constant.
     * @return A new ChannelPainter.
     */
    default ChannelPainter mul(float constant){
        return (x,y) -> this.valueAt(x, y)*constant;
    }
    /**
     * Divides a constant to the value of the method valueAt of the ChannelPainter.
     * @param constant The constant.
     * @return A new ChannelPainter.
     */
    default ChannelPainter div(float constant){
        return (x,y) -> {
            Preconditions.checkArgument(constant!=0, "You can not divide by zero!");
            return this.valueAt(x, y)/constant;
        };   
    }
    /**
     * Apply a unary operator to the value of the function valueAt.
     * @param po The unary operator.
     * @return The ChannelPainter that redefined valueAt.
     */
    default ChannelPainter map(DoubleUnaryOperator po){
        return (x,y) -> (float)po.applyAsDouble(this.valueAt(x, y));
    }
    /**
     * Apply a mathematical function (max(0,min(x,1))) to valueAt of each pixel.
     * @return A new ChannelPainter that redefines valueAt.
     */
    default ChannelPainter clamped(){
        return (x,y) -> max(0, min(this.valueAt(x, y), 1));
    }
    /**
     * Apply a mathematical function (xmod1) to valueAt of each pixel.
     * @return A new ChannelPainter that redefines valueAt.
     */
    default ChannelPainter cycling(){
        return (x,y) ->(float)Math2.floorMod(this.valueAt(x, y), 1);
    }
    /**
     * Apply a mathematical function (1−x) to valueAt of each pixel.
     * @return A new ChannelPainter that redefines valueAt.
     */
    default ChannelPainter inverted(){
        return (x,y) -> 1-this.valueAt(x, y);
    }
}
