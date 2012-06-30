package org.nanocom.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class mapping useful PHP functions.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Util {

    /**
     * @param array
	 * 
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
        Map<String, String> toReturn = new HashMap<String, String>();

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

    public static List<String> array_slice(List<String> array, int offset, int length) {
        if (array.isEmpty()) {
            return array;
        }

        if (length >= array.size()) {
            return array;
        }

        return array.subList(offset, length);
    }

}
