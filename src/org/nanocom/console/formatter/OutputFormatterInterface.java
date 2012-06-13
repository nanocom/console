/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

/**
 * Formatter interface for console output.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface OutputFormatterInterface {

    /**
     * Sets the decorated flag.
     *
     * @param decorated Whether to decorate the messages or not
     */
    void setDecorated(final boolean decorated);

    /**
     * Gets the decorated flag.
     *
     * @return True if the output will decorate messages, false otherwise
     */
    boolean isDecorated();

    /**
     * Sets a new style.
     *
     * @param name  The style name
     * @param style The style instance
     */
    void setStyle(final String name, final OutputFormatterStyleInterface style);

    /**
     * Checks if output formatter has style with specified name.
     *
     * @param name
     * @return
     */
    boolean hasStyle(final String name);

    /**
     * Gets style options from style with specified name.
     *
     * @param  name
     * @return
     */
    OutputFormatterStyleInterface getStyle(final String name) throws Exception;

    /**
     * Formats a message according to the given styles.
     *
     * @param message The message to style
     *
     * @return The styled message
     */
    String format(final String message);

}
