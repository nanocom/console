package com.nanocom.console.input;

import java.util.ArrayList;

/**
 * StringInput represents an input provided as a string.
 *
 * Usage:
 *
 *     InputInterface input = new StringInput("foo --bar=\"foobar\"");
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class StringInput /*extends ArgvInput*/ {

//    private static final String REGEX_STRING = "([^ ]+?)(?: |(?<!\\\\)\"|(?<!\\\\)'|)";
//    private static final String REGEX_QUOTED_STRING = "(?:\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"|'([^'\\\\]*(?:\\\\.[^'\\\\]*)*)')";
//
//    /**
//     * @param input      An array of parameters from the CLI (in the argv format)
//     * @param definition A InputDefinition instance
//     */
//    public StringInput(String input, InputDefinition definition) throws Exception {
//        super(new String[0], definition);
//
//        setTokens(tokenize(input));
//    }
//
//    /**
//     * Tokenizes a string.
//     *
//     * @param input The input to tokenize
//     *
//     * @throws Exception When unable to parse input (should never happen)
//     */
//    private String[] tokenize(String input) {
//        input.replaceAll("/(\r\n|\r|\n|\t)/", " ");
//
//        ArrayList<String> locTokens = new ArrayList<String>();
//        int length = input.length();
//        int cursor = 0;
//        while (cursor < length) {
//            // TODO
//            /*if (preg_match('/\s+/A', input, match, null, cursor)) {
//
//            input.
//            } else if (preg_match('/([^="\' ]+?)(=?)('.self::REGEX_QUOTED_STRING.'+)/A', input, match, null, cursor)) {
//                locTokens[] = match[1].match[2].stripcslashes(str_replace(array('"\'', '\'"', '\'\'', '""'), '', substr(match[3], 1, strlen(match[3]) - 2)));
//            } else if (preg_match('/'.self::REGEX_QUOTED_STRING.'/A', input, match, null, cursor)) {
//                locTokens[] = stripcslashes(substr(match[0], 1, strlen(match[0]) - 2));
//            } else if (preg_match('/'.self::REGEX_STRING.'/A', input, match, null, cursor)) {
//                locTokens[] = stripcslashes(match[1]);
//            } else {
//                // should never happen
//                throw new Exception("Unable to parse input near \"... " + input.substring(cursor, 10) + " ...\"");
//            }
//
//            cursor += strlen(match[0]);*/
//        }
//
//        return (String[])locTokens.toArray();
//    }

}
