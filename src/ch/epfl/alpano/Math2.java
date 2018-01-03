package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

/**
 * This interface is an extension of {@link java.lang.Math} providing extra mathematical
 * functions.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */


public interface Math2 {
    /**
     * The value of PI*2
     */
    double PI2 = 2*Math.PI;
    
    
    /**
     * Squares the argument x that is passed as a parameter.
     * 
     * @param x Value to square.
     * @return  value of x^2
     */
    static double sq(double x)
    {
        return x*x;
    }
    
    
    /**
     * Calculates the floored mod of the parameter x using the following formula.
     * <p>
     * x mod_floored y = x-y*(x dif_floored y)
     * 
     * @param x argument that will be used as the number that we want to take the floor mod of
     * @param y that will be used to divide by
     * @return the floored mod. x mod_floor y
     */
    static double floorMod(double x, double y){
        return x-y*Math.floor(x/y);
    }
    
    
    /**
     * Calculates the haversine of a value. 
     * 
     * @param x Value to input into the haversine function. haversine(x).
     * @return Value haversine of x. haversine(x).
     */
     static double haversin(double x){
        return sq(Math.sin(x/2));
    }
     

     /**
      * Calculates difference between 2 angles.
      * 
      * @param a1 First angle.
      * @param a2 Second angle.
      * @return the difference between the 2 angles.
      */
    static double angularDistance(double a1, double a2){
        return (floorMod((a2-a1+Math.PI), PI2)-Math.PI);
    }
    
    
    /**
     * Calculates the value of x using linear interpolation based on 2 known points.
     * 
     * @param y0 Value of the function in x = 0.
     * @param y1 Value of the function in x = 1.
     * @param x The point that needs to be interpolated.
     * @return The value of the linear approximation of the function in x.
     */
    static double lerp(double y0, double y1, double x){
        return y0+(y1-y0)*x;
    }
    
    
    /**
     * Calculates the value of a bilinear interpolation. 
     * <p>
     * This allows for interpolation in 3D knowing only 4 points.
     * This method will calculate the value of z given 4 points in 3D.
     * To specify where the z value that is being looked is the x and y variables represent it's location.
     * 
     * @param z00 Is the value of z when x = 0 and y = 0
     * @param z10 Is the value of z when x = 1 and y = 0
     * @param z01 Is the value of z when x = 0 and y = 1
     * @param z11 Is the value of z when x = 1 and y = 1
     * @param x The x coordinate of the z value that is being interpolated.
     * @param y The y coordinate of the z value that is being interpolated.  
     * @return The value in the z axis of the interpolation of the point located at the location (x,y).
     */
    static double bilerp(double z00, double z10, double z01, double z11, double x, double y){
        double z0 = lerp(z00,z10,x);
        double z1 = lerp(z01,z11,x);
        return lerp(z0,z1,y);
    }
    
    
    /**
     * Finds the root of of the function f. 
     * <p> 
     * The search starts at bornMin and will progress until reaching bornMax.
     * If no root is found within the interval Double.POSITIVE_INFINITY will be returned.
     * <p>
     * This method will look for a change of sign to detect a root and will return the lower bound of the interval dx where the change of sign was detected.
     * 
     * @param f The function that we are looking for the roots of.
     * @param minX The lower bound of where the search will begin to look for roots.
     * @param maxX The upper bound of where the search will stop looking for roots.
     * @param dX The size of the interval that the search algorithm will use when looking for the roots
     * @return The lower of the interval of size dx that contains the root. If no root found Double.POSITIVE_INFINITY will re returned
     */
    static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
        double bornMin = minX;
        double bornMax = minX+dX;
        while(bornMax <= maxX && (f.applyAsDouble(bornMin)*f.applyAsDouble(bornMax) > 0)){
            bornMin = bornMax;
            bornMax += dX;
        }
        if(bornMax > maxX){
            return Double.POSITIVE_INFINITY;
        }
        return bornMin;
    }
    
      
    
    /**
     * Calculates the zeros (roots) of a function f by using the dicotomie method.
     * <p>
     * This method will split the interval between x1 and x2 into 2 until the distance
     * between the 2 is smaller or equal to epsilon. The method will also stop it search if the exact value of the root is found.
     * 
     * @param f The function that is being analyzed to find it's roots.
     * @param x1 The lower bound of where the search begins.
     * @param x2 The upper bound of where the search ends.
     * @param epsilon The distance that x1 and x2 can be apart so that the algorithm concludes it has successfully found the answer.
     * @return The lower bound of the interval smaller than or equal to epsilon.
     * @throws IllegalArgumentException if no root is contained between x1 and x2.
     */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        Preconditions.checkArgument((f.applyAsDouble(x1)*f.applyAsDouble(x2) <= 0));
            
        double bornMin = x1;
        double bornMax = x2;
        if(f.applyAsDouble(x2) == 0){
            return x2;
        }
        boolean found = false;
        while(!found){
            double xm = f.applyAsDouble((bornMax+bornMin)/2);
            double fy = f.applyAsDouble(bornMin);
            if(xm == 0)
                return xm;
            if(fy == 0)
                return fy;
            if((fy*xm) < 0){
                bornMax = (bornMax+bornMin)/2;
            }
            else{
                bornMin = (bornMax+bornMin)/2;
            }
            if(bornMax-bornMin <= epsilon){
                found = true;
            }
        }
        return bornMin;
    }
    
}        
