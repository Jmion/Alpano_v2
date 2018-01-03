package ch.epfl.alpano;
import static ch.epfl.alpano.Math2.PI2;
import static java.lang.Math.PI;
/**
 * This class contains methods to manipulate numbers representing 
 * azimuts expressed in radians.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */
public interface Azimuth {
    
    /**
     * Determines if azimuth is within the rage of [0;2*pi[.
     * 
     * @param azimuth The angle that needs to be checked.
     * @return True if azimuth is contained in [0; 2*pi[ otherwise false.
     */
    static boolean isCanonical(double azimuth){
        if( azimuth >= 0 && azimuth < PI2) {
            return true;
        }
        return false;
    }

    
    /**
     * Transforms any angle to an angle between [0, 2*pi[.
     * 
     * @param azimuth The angle to transform into a angle between [0, 2*pi[.
     * @return A double which is the azimuth expressed between [0 , 2*pi[.
     */
    static double canonicalize(double azimuth){
        return Math2.floorMod(azimuth, PI2);
    }
    
    
    /**
     * Converts a azimuth angle to a mathematical angle
     * <p>
     * Changes the angle from clockwise to anti-clockwise.
     * 
     * @param azimuth Angle to be converted into a mathematical angle.
     * @return The angle in radiant as a mathematical angle.
     * @throws IllegalArgumentException if azimuth is not canonical.
     */
    static double toMath(double azimuth){
            Preconditions.checkArgument(isCanonical(azimuth), "argument is not Canonical");
            if(azimuth == 0){
                return 0.;
            }
            return PI2-azimuth;
    }
    
    
    /**
     * Converts a mathematical angle into an azimuth angle.
     * 
     * @param angle The angle to be converted into a azimuth angle.
     * @return An angle in radiant as a mathematical angle.
     * @throws IllegalArgumentException If angle is not canonical.
     */
    static double fromMath(double angle){
            Preconditions.checkArgument(isCanonical(angle), "argument is not Canonical");
            if(angle == 0){
                return 0.;
            }
            return PI2-angle;
    }
    
    
    /**
     * Determines the octant that the angle is in.
     * <p>
     * The different strings can be grouped together to represent NE SE NW SW.
     * 
     * @param azimuth The angle used to determine what octant it is in.
     * @param n String to show if the angle is pointing north.
     * @param e String to show if the angle is pointing north.
     * @param s String to show if the angle is pointing north.
     * @param w String to show if the angle is pointing north.
     * @return The representation of which octant the azimuth is in.
     * @throws IllegalArgumentException If azimuth is not canonical.
     */
    static String toOctantString(double azimuth, String n, String e, String s, String w){
        Preconditions.checkArgument(isCanonical(azimuth), "arguement is not canonical");
        //Testing the cases that are not going to give N. The N is a bit special and will be the default return
        String[] tabCard = {n+e, e, s+e, s , s+w, w, n+w};
        for(int i = 0; i < 7; i++){
            if((azimuth >= ((PI/8)+(PI/4)*i)) && (azimuth < (((3*PI)/8)+i*PI/4))){
                return tabCard[i];
            }
        }
        return n;
    }
}
