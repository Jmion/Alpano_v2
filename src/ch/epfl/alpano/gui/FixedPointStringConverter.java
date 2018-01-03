package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.util.StringConverter;
/**
* FixesPointStringConverter class converts chain of string and integer.
* <ul>
* <li>From a string to an integer, it moves the dot of the string to a certain number to the left. It also round up.</li>
* <li>From an integer to a string, it moves the dot of the integer to a certain number to the right.</li>
* </ul>
* 
* @author Martin Cibils (261746)
* @author Jeremy Mion (261178)
**/
public class FixedPointStringConverter extends StringConverter<Integer> {
    private int pointPosition;
    /**
     * Creates a fixed point string converter. Requires that the offset of the converter is known.
     * 
     * @param pointPosition The number that will represents how much we move the dot.
     */
    public FixedPointStringConverter(int pointPosition){
        this.pointPosition = pointPosition;
    }

    @Override
    public Integer fromString(String string){
        BigDecimal b = new BigDecimal(string);
        b = b.movePointRight(pointPosition);
        b = b.setScale(0, RoundingMode.HALF_UP);
        return b.intValueExact();
    }
    
    @Override
    public String toString(Integer number){
        BigDecimal b = new BigDecimal(number);
        b = b.movePointLeft(pointPosition);
        return b.toPlainString();
    }
}
