package com.nanocom.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class mapping useful PHP functions.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Util {

    /**
     * @param glue
     * @param pieces
     * @return
     */
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

    /**
     * @param glue
     * @param pieces
     * @return
     */
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

    /**
     * @param array
     * @return
     */
    public static Object array_pop(Object[] array) {
        Object toReturn = array[0];
        Object[] newArray = new Object[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            newArray[i - 1] = array[i];
        }
        array = newArray;

        return toReturn;
    }

    /**
     * @param pieces
     *
     * @return
     */
    public static Map<String, String> asAssociativeArray(String... pieces) {
        Map<String, String> toReturn = new HashMap<>();
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].split(":");
        }

        return null;
    }

    /**
     * @param text
     * @param search
     * @return 
     */
    public static int substr_count(String text, String search) {
        int count = text.split(search).length - 1;

        return count;
    }

}
