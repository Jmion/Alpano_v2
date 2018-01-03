

package ch.epfl.alpano.summit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;
/**
 * Allows to read a list of summit from a specific file. 
 * <p>
 * It will read the position, the elevation, and the name of each summit specified in the file.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class GazetteerParser {
    
    private GazetteerParser(){};
    
    /**
     * Gives a unmodifiable list of summits gotten from a file.
     * @param file The file we take the summits from.
     * @return The unmodifiable list of summits.
     * @throws IOException If there is a problem with a line.
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException{
        ArrayList<Summit> listOfSummits = new ArrayList<>();
        String nom; 
        int elevation;
        GeoPoint position;
        
        try(BufferedReader flot = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))){
           String ligne = flot.readLine(); 
           while(ligne != null){
               position = new GeoPoint(givesRadians(ligne.substring(0, 9).trim()), givesRadians(ligne.substring(10, 18).trim()));
               elevation = Integer.parseInt(ligne.substring(20, 25).trim());
               nom = ligne.substring(36);
               listOfSummits.add(new Summit(nom, position, elevation));
               ligne = flot.readLine();
           }
         }
        catch(NumberFormatException e){
            throw new IOException("Problem in the line.");
        }
        catch(StringIndexOutOfBoundsException e){
            throw new IOException("Problem in the line.");
        }
        return Collections.unmodifiableList(listOfSummits);
    }
    
    
    /**
     * This private method is used in order to have the radiant from a String represented with degrees, minutes and seconds.
     * @param e The String we want to know the value in radian of.
     * @return The value in radian.
     */
    private static double givesRadians(String e){
        String[] hms = e.split(":");
        int posNegaf = (int) Math.signum(Integer.parseInt(hms[0]));
        double value = Math.toRadians(Math.abs(Integer.parseInt(hms[0]))+Integer.parseInt(hms[1])/60.+Integer.parseInt(hms[2])/3600.);
        return posNegaf == -1 ?  value*(-1) :  value;
    }
}
