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

/**
 * Formatter class for console output.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public final class OutputFormatter implements OutputFormatterInterface {

    /**
     * The pattern to phrase the format.
     */
    // private static final String FORMAT_PATTERN = "#<([a-z][a-z0-9_=;-]+)>(.*?)</\\1?>#is";

    private Boolean decorated;
    private Map<String, OutputFormatterStyleInterface> styles = new HashMap<String, OutputFormatterStyleInterface>();

    /**
     * Initializes console output formatter.
     *
     * @param decorated  Whether this formatter should actually decorate strings
     * @param styles     Array of "name => FormatterStyle" instances
     */
    public OutputFormatter(final boolean decorated, Map<String, OutputFormatterStyleInterface> styles)  {
        init(decorated, styles);
    }

    public OutputFormatter(final boolean decorated)  {
        init(decorated, this.styles);
    }

    public OutputFormatter()  {
        init(false, this.styles);
    }

    private void init(final boolean decorated, Map<String, OutputFormatterStyleInterface> styles) {
        this.decorated = decorated;

        setStyle("error",    new OutputFormatterStyle("white", "red"));
        setStyle("info",     new OutputFormatterStyle("green"));
        setStyle("comment",  new OutputFormatterStyle("yellow"));
        setStyle("question", new OutputFormatterStyle("black", "cyan"));

        for (Entry<String, OutputFormatterStyleInterface> style : styles.entrySet()) {
            setStyle(style.getKey(), style.getValue());
        }
    }

    /**
     * Sets the decorated flag.
     *
     * @param decorated Whether to decorate the messages or not
     */
    @Override
    public void setDecorated(final boolean decorated) {
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
    public void setStyle(final String name, final OutputFormatterStyleInterface style) {
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
    public boolean hasStyle(final String name) {
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
        // return preg_replace_callback(FORMAT_PATTERN, array(this, 'replaceStyle'), message);
        // TODO
        return message;
    }

    /**
     * Replaces style of the output.
     *
     * @param match
     *
     * @return The replaced style
     */
    /*private String replaceStyle(final String match) {
        if (!isDecorated()) {
            return String.valueOf(match.charAt(2));
        }

        OutputFormatterStyleInterface locStyle;

        if (styles.containsKey(String.valueOf(match.charAt(1)).toLowerCase())) {
            locStyle = styles.get(String.valueOf(match.charAt(1)).toLowerCase());
        } else {
            locStyle = createStyleFromString(String.valueOf(match.charAt(1)));

            if (null == locStyle) {
                return String.valueOf(match.charAt(0));
            }
        }

        return locStyle.apply(format(String.valueOf(match.charAt(2))));
    }*/

    /**
     * Tries to create new style instance from string.
     *
     * @param string
     *
     * @return Null if string is not format string
     */
    /*private OutputFormatterStyle createStyleFromString(final String string) {
        // TODO
        return null;
        if (!preg_match_all('/([^=]+)=([^;]+)(;|)/', strtolower(string), matches, PREG_SET_ORDER)) {
            return false; // return null;
        }

        style = new OutputFormatterStyle();
        for (matches as match) {
            array_shift(match);

            if ('fg' == match[0]) {
                style.setForeground(match[1]);
            } elseif ('bg' == match[0]) {
                style.setBackground(match[1]);
            } else {
                style.setOption(match[1]);
            }
        }

        return style;
    }*/
}
