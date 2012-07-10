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
    void setForeground(String color) throws Exception;

    /**
     * Sets style background color.
     *
     * @param color Color name
     */
    void setBackground(String color) throws Exception;

    /**
     * Sets some specific style option.
     *
     * @param option Option name
     */
    void setOption(String option) throws Exception;

    /**
     * Unsets some specific style option.
     *
     * @param option Option name
     */
    void unsetOption(String option) throws Exception;

    /**
     * Sets multiple style options at once.
     *
     * @param options
     */
    void setOptions(List<String> options) throws Exception;

    /**
     * Applies the style to a given text.
     *
     * @param text The text to style
     * @return
     */
    String apply(String text);

}
