package org.nanocom.console;

/**
 * Utility class mapping useful PHP functions.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Util {

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
