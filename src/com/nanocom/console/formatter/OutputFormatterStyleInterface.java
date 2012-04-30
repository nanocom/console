package com.nanocom.console.formatter;

import java.util.List;

/**
 * Formatter style interface for defining styles.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface OutputFormatterStyleInterface {

    /**
     * Sets style foreground color.
     *
     * @param color Color name
     */
    void setForeground(final String color) throws Exception;

    /**
     * Sets style background color.
     *
     * @param color Color name
     */
    void setBackground(final String color) throws Exception;

    /**
     * Sets some specific style option.
     *
     * @param option Option name
     */
    void setOption(final String option) throws Exception;

    /**
     * Unsets some specific style option.
     *
     * @param option Option name
     */
    void unsetOption(final String option) throws Exception;

    /**
     * Sets multiple style options at once.
     *
     * @param options
     */
    void setOptions(final List<String> options) throws Exception;

    /**
     * Applies the style to a given text.
     *
     * @param text The text to style
     * @return
     */
    String apply(final String text);

}
