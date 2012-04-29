package com.nanocom.console;

/**
 * Utility class imitating PHP functions.
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

}
