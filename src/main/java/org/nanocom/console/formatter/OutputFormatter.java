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
import org.apache.commons.lang3.StringUtils;

/**
 * Formatter class for console output.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class OutputFormatter implements OutputFormatterInterface {

    /**
     * The pattern to phrase the format.
     */
    private static String FORMAT_PATTERN = "<(/?)([a-z][a-z0-9_=;-]+)?>([^<]*)";

    private Boolean decorated;
    private Map<String, OutputFormatterStyleInterface> styles = new HashMap<String, OutputFormatterStyleInterface>();
    private OutputFormatterStyleStack styleStack;

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
        String match0 = StringUtils.defaultString(matchResult.group(0));
        String match1 = StringUtils.defaultString(matchResult.group(1));
        String match2 = StringUtils.defaultString(matchResult.group(2));
        String match3 = StringUtils.defaultString(matchResult.group(3));

        if ("".equals(match2)) {
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
        Pattern pattern = Pattern.compile("([^=]+)=([^;]+)(;|$)");
        Matcher matcher = pattern.matcher(string.toLowerCase());

        OutputFormatterStyle locStyle = new OutputFormatterStyle();
        MatchResult result;
        boolean foundMatch = false;

        while (matcher.find()) {
            foundMatch = true;
            result = matcher.toMatchResult();
            String match1 = result.group(1); // fg
            String match2 = result.group(2); // blue

            if ("fg".equals(result.group(1))) {
                locStyle.setForeground(result.group(2));
            } else if ("bg".equals(result.group(1))) {
                locStyle.setBackground(result.group(2));
            } else {
                locStyle.setOption(result.group(2));
            }
        }

        return foundMatch ? locStyle : null;
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
        return isDecorated() && StringUtils.isNotEmpty(text) ? style.apply(text) : text;
    }

    interface Callback {
        String foundMatch(MatchResult matchResult);
    }

    class CallbackMatcher {

        private final Pattern pattern;

        public CallbackMatcher(String regex) {
            pattern = Pattern.compile(regex);
        }

        public String replaceMatches(String stringToMatch, Callback callback) {
            final Matcher matcher = pattern.matcher(stringToMatch);
            while (matcher.find()) {
                MatchResult matchResult = matcher.toMatchResult();
                String replacement = callback.foundMatch(matchResult);
                String fullReplacement = stringToMatch.substring(0, matchResult.start()) +
                         replacement + stringToMatch.substring(matchResult.end());

                if (!stringToMatch.equals(fullReplacement)) {
                    stringToMatch = fullReplacement;
                    matcher.reset(stringToMatch);
                }
            }

            return stringToMatch;
        }
    }
}
