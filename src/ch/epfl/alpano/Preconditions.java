
package ch.epfl.alpano;
/**
 * Preconditions are used to check if the boolean argument is true.
 * <p>
 * If the boolean passed is false then the precondition is not met and a new IllegalArgumentException is thrown.
 * The use of this interface allows for a more esthetic way to check and create IllegalArguemtnException when needed.
 * 
 * @author Martin Cibils (261746)
 * @author Jeremy Mion (261178)
 */


public interface Preconditions {
    
   
    /**
     * Checks if the boolean condition is met. If the condition is not true the and error will be created.
     * 
     * @param b Boolean condition to check.
     * @throws IllegalArgumentException if b doesn't evaluate to true.
     */
    public static void checkArgument(boolean b)
    {
        if(!b)
            throw new IllegalArgumentException();
    }
    
    
    /**
     * Checks if the boolean condition is met. If the condition is not met it will create an error and join the message to it.
     * 
     * @param b The condition that must be true to not create an error.
     * @param message To be added to the error if b is false.
     * @throws IllegalArgumentException with message if b evaluates to false.
     */
    public static void checkArgument(boolean b, String message)
    {
        if(!b)
            throw new IllegalArgumentException(message);
    }
    
}

