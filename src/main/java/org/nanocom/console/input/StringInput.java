/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;

/**
 * StringInput represents an input provided as a string.
 *
 * Usage:
 *
 *     StringInput input = new StringInput("foo --bar=\"foobar\"");
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class StringInput extends ArgvInput {

    // private final static String REGEX_STRING = "([^ ]+?)(?: |(?<!\\\\)\"|(?<!\\\\)\'|$)";
    // private final static String REGEX_QUOTED_STRING = "(?:\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"|\\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\')";

    /**
     * @param input An array of parameters from the CLI (in the argv format)
     * @param definition An InputDefinition instance
     * @throws Exception 
     */
    public StringInput(final String input, final InputDefinition definition) throws Exception {
        super(new String[0], definition);

        setTokens(tokenize(input));
    }
 
    public StringInput(final String input) throws Exception {
        this(input, null);
    }

    /**
     * Tokenizes a string.
     *
     * @param input The input to tokenize
     * @return The tokenized string
     *
     * @throws IllegalArgumentException When unable to parse input (should never happen)
     */
    private String[] tokenize(String input) throws IllegalArgumentException {
        input.replaceAll("(\r\n|\r|\n|\t)", " ");

        tokens = new ArrayList<String>();
        // int length = input.length();
        // int cursor = 0;
        // TODO Implement this part
        /* while (cursor < length) {
            if (preg_match('/\s+/A', $input, $match, null, $cursor)) {
            } else if (preg_match('/([^="\' ]+?)(=?)('.self::REGEX_QUOTED_STRING.'+)/A', $input, $match, null, $cursor)) {
                tokens[] = $match[1].$match[2].stripcslashes(str_replace(array('"\'', '\'"', '\'\'', '""'), '', substr($match[3], 1, strlen($match[3]) - 2)));
            } else if (preg_match('/'.self::REGEX_QUOTED_STRING.'/A', $input, $match, null, $cursor)) {
                tokens[] = stripcslashes(substr($match[0], 1, strlen($match[0]) - 2));
            } else if (preg_match('/'.self::REGEX_STRING.'/A', $input, $match, null, $cursor)) {
                tokens[] = stripcslashes($match[1]);
            } else {
                // should never happen
                // @codeCoverageIgnoreStart
                throw new IllegalArgumentException(String.format("Unable to parse input near \"... %s ...\"", input.substring(cursor, 10)));
                // @codeCoverageIgnoreEnd
            }

            cursor += match.get(0).length();
        }*/

        return (String[]) tokens.toArray();
    }

}
