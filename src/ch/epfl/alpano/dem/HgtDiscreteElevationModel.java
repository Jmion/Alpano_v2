package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * Creates an DEM discret from the HGT file. It also allows to get a summit of a specific point in hgt file.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */


public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    
    private ShortBuffer canal;
    private final String fileName;
    private final Interval2D myInterval;
    private static final int sizeOfFileNeeded = 25934402;
    private static final int height = 3601;
    
    /**
     * Construct a MNT discrete from the .hgt file. It checks if the name is correct or not.  
     * @param file The file we take the information from.
     * @throws IllegalArgumentException If the name of the file is not correct.
     */
    public HgtDiscreteElevationModel(File file){
        boolean isValid = true;
        this.fileName = file.getName();
        Preconditions.checkArgument(fileName.length() == 11,"the length of the file name is not correct");
        
        if(!(fileName.charAt(0) =='N' || fileName.charAt(0) == 'S'))
            isValid = false;
        try{
            Integer.parseInt(String.valueOf(fileName.charAt(1)));
            Integer.parseInt(String.valueOf(fileName.charAt(2)));
            Integer.parseInt(String.valueOf(fileName.charAt(4)));
            Integer.parseInt(String.valueOf(fileName.charAt(5)));
            Integer.parseInt(String.valueOf(fileName.charAt(6)));
        }
        catch(NumberFormatException e){
            isValid = false;
        }    
        if(fileName.charAt(3) != 'W' && fileName.charAt(3) != 'E')
            isValid = false;
        if(!(fileName.substring(7, 11).equals(".hgt")))
            isValid = false;
        Preconditions.checkArgument(isValid, "Error with the file's name.");
        Preconditions.checkArgument(file.length() == sizeOfFileNeeded, "Size of the file is not correct.");
        try(FileInputStream s = new FileInputStream(file)){
            canal = s.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        myInterval = buildExtent();
}
    
    /**
     * Creates an Interval2D that represents the areas the HGT file covers. It uses the name of the file to create it.
     * @return The Interval2D.
     */
    private Interval2D buildExtent() {
        int indexBasGaucheAbscisse;
        int indexBasGaucheOrdonne;
        int indexBasGaucheAsbcissePlus;
        int indexBasGaucheOrdonnePlus;
        int longitude = Integer.parseInt(fileName.substring(4, 7));
        int latitude = Integer.parseInt(fileName.substring(1, 3));
        
        if(fileName.charAt(0) == 'N'){
            indexBasGaucheOrdonne = latitude*SAMPLES_PER_DEGREE;
            indexBasGaucheOrdonnePlus = (latitude+1)*SAMPLES_PER_DEGREE;
        }
        else{   
            indexBasGaucheOrdonne = -latitude*SAMPLES_PER_DEGREE;
            indexBasGaucheOrdonnePlus = (-latitude+1)*SAMPLES_PER_DEGREE;
        }
        if(fileName.charAt(3) == 'E'){
            indexBasGaucheAbscisse = longitude*SAMPLES_PER_DEGREE; 
            indexBasGaucheAsbcissePlus = (longitude+1)*SAMPLES_PER_DEGREE;
        }
        else{
            indexBasGaucheAbscisse = (-longitude)*SAMPLES_PER_DEGREE;
            indexBasGaucheAsbcissePlus = (-longitude+1)*SAMPLES_PER_DEGREE;
        }
        return new Interval2D(new Interval1D(indexBasGaucheAbscisse, indexBasGaucheAsbcissePlus), new Interval1D(indexBasGaucheOrdonne,indexBasGaucheOrdonnePlus));
    }

    @Override
    public double elevationSample(int x, int y)
            throws IllegalArgumentException {
        Preconditions.checkArgument(this.extent().contains(x, y));
        int x1 = x - this.extent().iX().includedFrom();
        int y1 = this.extent().iY().includedTo() - y;
        double hauteur = canal.get(y1*height+x1);
        return hauteur;
    }


    @Override
    public Interval2D extent() {
        return myInterval;
    }

}
