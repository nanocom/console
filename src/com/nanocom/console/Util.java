package com.nanocom.console;

import java.util.List;

/**
 * Utility class imitating some PHP functions.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Util {

    public static String implode(final String glue, final String[] pieces) {
        String output = "";

        if (pieces.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(pieces[0]);

            for (int i = 1; i < pieces.length; i++) {
                sb.append(glue);
                sb.append(pieces[i]);
            }

            output = sb.toString();
        }

        return output;
    }

    public static String implode(final String glue, final List<String> pieces) {
        String output = "";

        if (pieces.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(pieces.get(0));

            for (int i = 1; i < pieces.size(); i++) {
                sb.append(glue);
                sb.append(pieces.get(i));
            }

            output = sb.toString();
        }

        return output;
    }

    public static Object array_pop(Object[] array) {
        Object toReturn = array[0];
        Object[] newArray = new Object[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            newArray[i - 1] = array[i];
        }
        array = newArray;

        return toReturn;
    }

    public static Object array_pop(List<Object> array) {
        return array.remove(0);
    }

}
