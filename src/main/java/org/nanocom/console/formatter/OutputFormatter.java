/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Formatter class for console output.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class OutputFormatter implements OutputFormatterInterface {

    /**
     * The pattern to phrase the format.
     */
    private static Pattern FORMAT_PATTERN = Pattern.compile("<(/?)([a-z][a-z0-9_=;-]+)?>([^<]*)", Pattern.CASE_INSENSITIVE);

    /**
     * The pattern for style.
     */
    private static Pattern STYLE_PATTERN = Pattern.compile("([^=]+)=([^;]+)(;|$)", Pattern.CASE_INSENSITIVE);

    private Boolean decorated;
    private Map<String, OutputFormatterStyleInterface> styles = new HashMap<String, OutputFormatterStyleInterface>();
    private OutputFormatterStyleStack styleStack;

    /**
     * Escapes "<" special char in given text.
     *
     * @param text Text to escape
     *
     * @return Escaped text
     */
    public static String escape(String text) {
        return text.replaceAll("([^\\\\\\\\]?)<", "$1\\\\<");
    }

    /**
     * Initializes console output formatter.
     *
     * @param decorated  Whether this formatter should actually decorate strings
     * @param styles     Array of "name => FormatterStyle" instances
     */
    public OutputFormatter(boolean decorated, Map<String, OutputFormatterStyleInterface> styles)  {
        init(decorated, styles);
    }

    public OutputFormatter(boolean decorated)  {
        init(decorated, new HashMap<String, OutputFormatterStyleInterface>());
    }

    public OutputFormatter()  {
        init(false, new HashMap<String, OutputFormatterStyleInterface>());
    }

    private void init(boolean decorated, Map<String, OutputFormatterStyleInterface> styles) {
        this.decorated = decorated;

        setStyle("error",    new OutputFormatterStyle("white", "red"));
        setStyle("info",     new OutputFormatterStyle("green"));
        setStyle("comment",  new OutputFormatterStyle("yellow"));
        setStyle("question", new OutputFormatterStyle("black", "cyan"));

        for (Entry<String, OutputFormatterStyleInterface> style : styles.entrySet()) {
            setStyle(style.getKey(), style.getValue());
        }

        styleStack = new OutputFormatterStyleStack();
    }

    /**
     * Sets the decorated flag.
     *
     * @param decorated Whether to decorate the messages or not
     */
    @Override
    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }

    /**
     * Gets the decorated flag.
     *
     * @return True if the output will decorate messages, false otherwise
     */
    @Override
    public boolean isDecorated() {
        return decorated;
    }

    /**
     * Sets a new style.
     *
     * @param name  The style name
     * @param style The style instance
     */
    @Override
    public void setStyle(String name, OutputFormatterStyleInterface style) {
        styles.put(name, style);
    }

    /**
     * Checks if output formatter has style with specified name.
     *
     * @param name
     *
     * @return
     */
    @Override
    public boolean hasStyle(String name) {
        return styles.containsKey(name);
    }

    /**
     * Gets style options from style with specified name.
     *
     * @param name
     *
     * @return
     *
     * @throws IllegalArgumentException When style isn't defined
     */
    @Override
    public OutputFormatterStyleInterface getStyle(String name) {
        if (!this.hasStyle(name)) {
            throw new IllegalArgumentException("Undefined style: " + name);
        }

        return styles.get(name);
    }

    /**
     * Formats a message according to the given styles.
     *
     * @param message The message to style
     *
     * @return The styled message
     */
    @Override
    public String format(String message) {
        return new CallbackMatcher(FORMAT_PATTERN).replaceMatches(message, new Callback() {

            @Override
            public String foundMatch(MatchResult matchResult) {
                return replaceStyle(matchResult);
            }
        });
    }

    /**
     * Replaces style of the output.
     *
     * @param match
     *
     * @return The replaced style
     */
    private String replaceStyle(MatchResult matchResult) {
        String match0 = defaultString(matchResult.group(0));
        String match1 = defaultString(matchResult.group(1));
        String match2 = defaultString(matchResult.group(2));
        String match3 = defaultString(matchResult.group(3));

        if (EMPTY.equals(match2)) {
            if ("/".equals(match1)) {
                // Closing tag ("</>")
                styleStack.pop();

                return applyStyle(styleStack.getCurrent(), match3);
            }

            // Opening tag ("<>")
            return "<>" + match3;
        }

        OutputFormatterStyleInterface locStyle;

        if (null != styles.get(match2.toLowerCase())) {
            locStyle = styles.get(match2.toLowerCase());
        } else {
            locStyle = createStyleFromString(match2);

            if (null == locStyle) {
                return match0;
            }
        }

        if ("/".equals(match1)) {
            styleStack.pop(locStyle);
        } else {
            styleStack.push(locStyle);
        }

        return applyStyle(styleStack.getCurrent(), match3);
    }

    /**
     * Tries to create new style instance from string.
     *
     * @param string
     *
     * @return Null if string is not format string
     */
    private OutputFormatterStyle createStyleFromString(String string) {
        Matcher matcher = STYLE_PATTERN.matcher(string.toLowerCase());

        OutputFormatterStyle style = new OutputFormatterStyle();
        MatchResult result;

        if (!matcher.find()) {
            return null;
        }

        do {
            result = matcher.toMatchResult();
            String match1 = result.group(1); // fg
            String match2 = result.group(2); // blue

            if ("fg".equals(result.group(1))) {
                style.setForeground(result.group(2));
            } else if ("bg".equals(result.group(1))) {
                style.setBackground(result.group(2));
            } else {
                style.setOption(result.group(2));
            }
        } while (matcher.find());

        return style;
    }

    /**
     * Applies style to text if must be applied.
     *
     * @param style Style to apply
     * @param text  Input text
     *
     * @return Styled text
     */
    private String applyStyle(OutputFormatterStyleInterface style, String text) {
        return isDecorated() && isNotEmpty(text) ? style.apply(text) : text;
    }

    interface Callback {
        String foundMatch(MatchResult matchResult);
    }

    /**
     * TODO Delete this unuseful class
     */
    class CallbackMatcher {

        private Pattern pattern;

        public CallbackMatcher(Pattern pattern) {
            this.pattern = pattern;
        }

        public String replaceMatches(String stringToMatch, Callback callback) {
            Matcher matcher = pattern.matcher(stringToMatch);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                MatchResult matchResult = matcher.toMatchResult();
                matcher.appendReplacement(sb, Matcher.quoteReplacement(callback.foundMatch(matchResult)));
            }
            matcher.appendTail(sb);

            return sb.toString();
        }
    }
}
