
package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;;

/**
 * This class creates the picture given a painter and the parameters of the panorama
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public interface PanoramaRenderer {
    /**
     * Creates an image from a panorama and the imagePainter that will be used to determine the
     *  colors of the pixels.
     * 
     * @param pano Parameters of the panorama. The information about the image to create.
     * @param imgPainter Painter of the image.
     * @return Image of the panorama
     */
    static Image renderPanorama(Panorama pano, ImagePainter imgPainter){
        int width = pano.parameters().width();
        int height = pano.parameters().height();
        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                pw.setColor(x, y, imgPainter.colorAt(y, x));
            }
        }
        return img;
    }
}
