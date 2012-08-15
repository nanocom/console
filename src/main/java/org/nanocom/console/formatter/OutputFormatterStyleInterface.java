/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

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
    void setForeground(String color);

    /**
     * Sets style background color.
     *
     * @param color Color name
     */
    void setBackground(String color);

    /**
     * Sets some specific style option.
     *
     * @param option Option name
     */
    void setOption(String option);

    /**
     * Unsets some specific style option.
     *
     * @param option Option name
     */
    void unsetOption(String option);

    /**
     * Sets multiple style options at once.
     *
     * @param options
     */
    void setOptions(List<String> options);

    /**
     * Applies the style to a given text.
     *
     * @param text The text to style
     * @return
     */
    String apply(String text);
}
