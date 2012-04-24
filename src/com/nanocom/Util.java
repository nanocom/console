package com.nanocom;

import java.util.List;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Util {

    public static String implode(final String glue, final List<String> pieces) {
        StringBuilder sb = new StringBuilder();

        if (pieces != null) {
            for (int i = 0; i < pieces.size(); i++){
                if (i > 0) {
                    sb.append(glue);
                }

                sb.append(pieces.get(i));
            }
        }

		return sb.toString();
    }

}
