package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;
/**
 * Represents a one dimensional interval of integers.
 * <p>
 * A 1 dimensional interval defines a range of integers that are contained between a lower and upper value.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */

public final class Interval1D {
    private final int lower, upper;
    /**
     * Constructor to create a 1 dimensional interval with starting at the value of lower and includes all the integers up to and including upper. 
     * 
     * @param includedFrom The lower bound.
     * @param includedTo The upper bound.
     * @throws IllegalArgumentException If the includedFrom is bigger than includedTo.
     */
    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedTo >= includedFrom);
        lower = includedFrom;
        upper = includedTo;

    }
    
    
    /**
     * Returns the lower bound.
     * 
     * @return Lower which is the lower bound.
     */
    public int includedFrom(){
        return lower;
    }
    
    
    /**
     * Returns the upper bound.
     * 
     * @return Upper which is the upper bound.
     */
    public int includedTo() {
        return upper;
    }
    
    
    /**
     * Determines if v is contained within this.
     * 
     * @param v The number you are looking for.
     * @return True/false if it contains it or not.
     */
    public boolean contains(int v){
        return (v >= includedFrom() && v <= includedTo());
    }
    
    
    /**
     * Returns the size of the interval.
     * 
     * @return The size of this.
     */
    public int size(){
        return includedTo()-includedFrom()+1;
    }
    
    
    /**
     * Gives you the size of the intersection of this with that.
     * 
     * @param that The interval that you want to know the size of the intersection with.
     * @return The size of the intersection as an integer.
     */
    public int sizeOfIntersectionWith(Interval1D that){
        int sizeOftheIntersection = 0;
            for(int i = Math.min(that.includedFrom(), this.includedFrom()); i <= Math.min(that.includedTo(), this.includedTo()); ++ i){
                if(i == Math.max(this.includedFrom(), that.includedFrom()) + sizeOftheIntersection){
                    sizeOftheIntersection ++;
                }
            }
        return sizeOftheIntersection;
    }
    
    
    /**
     * Creates a new interval made of the smallest and biggest bound of the two interval 
     * (this is called a bounding union).
     * 
     * @param that The interval you want to make the bounding union with.
     * @return The bounded union of this with that.
     */
    public Interval1D boundingUnion(Interval1D that){
        return new Interval1D(Math.min(this.includedFrom(), that.includedFrom()), Math.max(this.includedTo(), that.includedTo()));
    }
    
    
    /**
     * Tells you if you can create an union (not a bounding one!) out of the two interval.
     * 
     * @param that The interval you want to know if you can make an interval with.
     * @return True/False if you can make the interval or not.
     */
    public boolean isUnionableWith(Interval1D that){
        if(this.size()+that.size()-sizeOfIntersectionWith(that) == boundingUnion(that).size())
            return true;
        return false;
    }
    
    
    /**
     * Gives you the union (not a bounding) of two interval.
     * 
     * @param that The interval you want to create an union with.
     * @return A new interval that is the union of this and that.
     * @throws IllegalArgumentException If you can not make an interval out of the two interval.
     */
    public Interval1D union(Interval1D that){
        Preconditions.checkArgument(isUnionableWith(that));
        return boundingUnion(that);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Interval1D))
            return false;
        Interval1D other = (Interval1D) obj;
        if (includedFrom() != other.includedFrom())
            return false;
        if (includedTo() != other.includedTo())
            return false;
        return true;
    }
    
    
    @Override
    public int hashCode() {
      return Objects.hash(includedFrom(), includedTo());
    }
    
    
    @Override
    public String toString(){
        Locale l = null;
        return String.format(l, "[%d..%d]", includedFrom(), includedTo()); 
    }
}
