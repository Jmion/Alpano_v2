package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;
/**
 * Represents a two dimensional interval of integers.
 * <p>
 * A 2 dimensional interval allows for a description an area. The area is described as an interval along the 
 * x-axis, and another area along the y-axis.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class Interval2D {
    
    private final Interval1D iX;
    private final Interval1D iY;
    
    
    /**
     * Creates a 2D interval.
     * 
     * @param iX interval along the x-axis
     * @param iY interval along the y-axi.
     * @throws NullPointerException If iX or iY is null.
     */
    public Interval2D(Interval1D iX, Interval1D iY){
        if(iX == null || iY == null)
            throw new NullPointerException();
        this.iX = iX;
        this.iY = iY;
    }
    
    
    /**
     * Returns the Interval1D along the x-axis.
     * 
     * @return x-axis
     */
    public Interval1D iX(){
        return iX;
    }
    
    
    /**
     * Returns the Interval1D along the y-axis
     * 
     * @return y-axis
     */
    public Interval1D iY(){
        return iY;
    }
    
    
    /**
     * Checks that the (x,y) coordinates are part of this.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return True if (x,y) is contained in this.
     */
    public boolean contains(int x, int y){
        return (this.iX().contains(x) && this.iY().contains(y));
    }
    
    
    /**
     * Determines the number of elements in this.
     * 
     * @return The total number of elements.
     */
    public int size(){
        return this.iX().size() * this.iY().size();
    }
    
    
    /**
     * Calculates the size of the intersection between this and that.
     * 
     * @param that The interval you do the intersection with.
     * @return size of the intersection.
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return this.iX().sizeOfIntersectionWith(that.iX()) * this.iY().sizeOfIntersectionWith(that.iY());
    }
    
    
    /**
     * Gives the bounding union of two interval.
     * 
     * @param that The interval you do the bounding union with.
     * @return A new 2D interval.
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion(that.iY()));
    }
    
    
    /**
     * Tells you if you can union (not a bounding one!) the two interval.
     * 
     * @param that The interval you check if you can do the union with.
     * @return True if you can, false if you can't.
     */
    public boolean isUnionableWith(Interval2D that){
        return this.size()+ that.size() - this.sizeOfIntersectionWith(that) == this.boundingUnion(that).size();
    }
    
    
    /**
     * Gives you the union (not bounding!) of two 2D interval.
     * 
     * @param that The interval you want to make the union with.
     * @return The 2D interval.
     * @throws IllegalArgumentException If the union cannot be made.
     */
    public Interval2D union(Interval2D that){
        Preconditions.checkArgument(this.isUnionableWith(that), "This cannot be unioned with that.Interval2D");
        return new Interval2D(this.iX().union(that.iX()), this.iY().union(that.iY()));
    }
    
    
    @Override
    public int hashCode() {
      return Objects.hash(this.iX(), this.iY());
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Interval2D))
            return false;
        Interval2D other = (Interval2D) obj;
        if (iX() == null) {
            if (other.iX() != null)
                return false;
        } else if (!iX().equals(other.iX()))
            return false;
        if (iY() == null) {
            if (other.iY() != null)
                return false;
        } else if (!iY().equals(other.iY()))
            return false;
        return true;
    }
    
    
    @Override
    public String toString(){
        Locale l = null;
        return String.format(l, "[%d..%d]x[%d..%d]",iX.includedFrom(),iX().includedTo(),iY().includedFrom(),iY().includedTo()); 
    }
    
    
}
