package ch.epfl.alpano.gui;
/**
 * This interface represents six predefined panoramas. <p>
 * Each of them has a width of 2500 pixels, a height of 800 pixels. The max distance is 300 km and the oversampling value is set to 0.
 *  
 *  
 *  
 *@author Martin Cibils (261746)
 *@author Jeremy Mion (261178)
*/
public interface PredefinedPanoramas {
    PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300, 600, 180, 110, 300, 2500, 800, 0);
    PanoramaUserParameters ALP_JURA = new PanoramaUserParameters(68_087, 470_085, 1380, 162, 27, 300, 2500, 800, 0);
    PanoramaUserParameters MONT_RACINE = new PanoramaUserParameters(68_200, 470_200, 1500, 135, 45, 300, 2500, 800, 0);
    PanoramaUserParameters FINSTERRARHORN = new PanoramaUserParameters(81_260, 465_374, 4300, 205, 20, 300, 2500, 800, 0);
    PanoramaUserParameters TOUR_DE_SAUBABELIN = new PanoramaUserParameters(66_385, 465_353, 700, 135, 100, 300, 2500, 800, 0);
    PanoramaUserParameters PLAGE_DU_PELICAN = new PanoramaUserParameters(65_728, 465_132, 380, 135, 60, 300, 2500, 800, 0);
}