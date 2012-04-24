package com.nanocom.output;

import com.nanocom.formatter.OutputFormatterStyle;
import com.nanocom.formatter.OutputFormatterStyleInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Formatter class for console output.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class OutputFormatter implements OutputFormatterInterface {

    /**
     * The pattern to phrase the format.
     */
    private static final String FORMAT_PATTERN = "#<([a-z][a-z0-9_=;-]+)>(.*?)</\\1?>#is";

    private Boolean decorated;
    private Map<String, OutputFormatterStyleInterface> styles = new HashMap<String, OutputFormatterStyleInterface>();

    /**
     * Initializes console output formatter.
     *
     * @param decorated  Whether this formatter should actually decorate strings
     * @param styles     Array of "name => OutputFormatterStyle" instances
     */
    public OutputFormatter(final boolean decorated, Map<String, OutputFormatterStyleInterface> styles) throws Exception {
        init(decorated, styles);
    }

    public OutputFormatter(final boolean decorated) throws Exception {
        init(decorated, new HashMap<String, OutputFormatterStyleInterface>());
    }

    public OutputFormatter() throws Exception {
        init(false, new HashMap<String, OutputFormatterStyleInterface>());
    }

    private void init(final boolean decorated, Map<String, OutputFormatterStyleInterface> styles) throws Exception {
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
        return this.decorated;
    }

    /**
     * Sets a new style.
     *
     * @param name  The style name
     * @param style The style instance
     */
    @Override
    public void setStyle(final String name, OutputFormatterStyleInterface style) {
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
     * @throws Exception When style isn't defined
     */
    @Override
    public OutputFormatterStyleInterface getStyle(final String name) throws Exception {
        if (!hasStyle(name)) {
            throw new Exception("Undefined style: " + name);
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
    public String format(final String message) {
        // TODO
        // return preg_replace_callback(FORMAT_PATTERN, array(this, 'replaceStyle'), message);
        return "";
    }

    /**
     * Replaces style of the output.
     *
     * @param match
     *
     * @return The replaced style
     */
    private String replaceStyle(final List<String> match) {
        // TODO
        return "";
        /*if (!isDecorated()) {
            return match.get(2);
        }

        OutputFormatterStyleInterface style;
        if (styles.containsKey(match.get(1))) {
            style = styles.get(match.get(1));
        } else {
            style = createStyleFromString(match.get(1));

            if (null == style) {
                return match.get(0);
            }
        }

        return style->apply(this.format(match[2]));*/
    }

    /**
     * Tries to create new style instance from string.
     *
     * @param   string  string
     *
     * @return  Symfony\Component\Console\Format\FormatterStyle|Boolean false if string is not format string
     */
    private OutputFormatterStyleInterface createStyleFromString(final String string) throws Exception {
        return new OutputFormatterStyle();
        // TODO
        /*if (!preg_match_all('/([^=]+)=([^;]+)(;|)/', strtolower(string), matches, PREG_SET_ORDER)) {
            return false;
        }

        style = new OutputFormatterStyle();
        foreach (matches as match) {
            array_shift(match);

            if ('fg' == match[0]) {
                style->setForeground(match[1]);
            } elseif ('bg' == match[0]) {
                style->setBackground(match[1]);
            } else {
                style->setOption(match[1]);
            }
        }

        return style;*/
    }

}
