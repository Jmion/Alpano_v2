
package ch.epfl.alpano.gui;
import javafx.scene.paint.Color;

/**
 * This interface represents an image painter. It allows to obtain the color of a given pixel.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
@FunctionalInterface
public interface ImagePainter {
    /**
     * Gives the color of a specific pixel.
     * @param x The x-axis coordinate.
     * @param y The y-axis coordinate.
     * @return The color on that point.
     */
    Color colorAt(int x, int y);
    /**
     * Gives a new ImagePainter with different colors made of 4 ChannelPainter.
     * @param hue The color of the image.
     * @param saturation The saturation of it.
     * @param brightness The brightness of it.
     * @param opacity The opacity of it.
     * @return A new ImagePainter made of these ChannelPainter.
     */
    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness, ChannelPainter opacity){
        return (x,y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), brightness.valueAt(x, y), opacity.valueAt(x, y));
    }
    
    /**
     * Creates an image painter that will return the color of each pixel. The representation of the pixel will be passed
     * to this method as a biBimensional <i>int</i> array representing the picture with the following criteria.<p>
     * <ul>
     * <li>Bits 31-24 represent the <b>alpha</b> value</li>
     * <li>Bits 23-16 represent the <b>Red</b> component of the pixel</li>
     * <li>Bits 15-8 represent the <b>Green</b> component of the pixel</li>
     * <li>Bits 7-0 represent the <b>Blue</b> component of the pixel</li>
     * 
     * @param picutre
     * @return
     */
    static ImagePainter argb(int[][] picutre){
        return (x,y) -> {
            int pixelValue = picutre[x][y];
//            return Color.rgb( pixelValue>>>16 & 0xff, pixelValue>>>8 & 0xff, pixelValue & 0xff, ((pixelValue>>>24)&0xff)/255d);
            return Color.rgb( pixelValue>>>8 & 0xff ,pixelValue>>>16 & 0xff ,pixelValue & 0xff, ((pixelValue>>>24)&0xff)/255d);

        };
    }
    
    /**
     * Gives a new ImagePainter made of gray with 2 different ChannelPainter.
     * @param greyHue The color of the image.
     * @param opacity The opacity of it.
     * @return A new ImagePainter.
     */
    static ImagePainter gray(ChannelPainter greyHue, ChannelPainter opacity){
        return (x,y) -> Color.gray(greyHue.valueAt(x, y), opacity.valueAt(x, y));
    }
}
