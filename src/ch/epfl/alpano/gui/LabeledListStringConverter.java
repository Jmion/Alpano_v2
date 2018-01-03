package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
/**
* The LabeledListStringConverter converts  chain of string and integer.<p>
* The value of the string will correspond to the position of the string in the chain of string.
* It works in both sense.
* 
* @author Martin Cibils (261746)
* @author Jeremy Mion (261178)
**/
public class LabeledListStringConverter extends StringConverter<Integer> {
    private final Map<String, Integer> strToInt;
    private final Map<Integer, String> intToStr;
    /**
     * Takes each string and initialize each map with the string and the position of it.
     * @param strings The strings to initialize the maps.
     */
    public LabeledListStringConverter(String...strings) {
        strToInt = new HashMap<>();
        intToStr = new HashMap<>();
        for(int i = 0; i < strings.length; ++i){
            intToStr.put(i, strings[i]);
            strToInt.put(strings[i], i);
        }
    }
    
    @Override
    public Integer fromString(String string) {
        Integer numb = strToInt.get(string);
        checkArgument(numb != null, "Cannot convert from dtring to integer with the given value.");
        return numb;
    }

    @Override
    public String toString(Integer object) {
        if(object == null)
            return "";
        return intToStr.get(object);
    }
}
