/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.output;

import com.nanocom.console.formatter.OutputFormatterInterface;
import java.util.List;

/**
 * OutputInterface is the interface implemented by all Output classes.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface OutputInterface {

    final static int VERBOSITY_QUIET   = 0;
    final static int VERBOSITY_NORMAL  = 1;
    final static int VERBOSITY_VERBOSE = 2;

    final static int OUTPUT_NORMAL = 0;
    final static int OUTPUT_RAW    = 1;
    final static int OUTPUT_PLAIN  = 2;

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines of a single string
     * @param newline  Whether to add a newline or not
     * @param type     The type of output
     *
     * @throws Exception When unknown output type is given
     */
    void write(final List<String> messages, final boolean newline, final int type) throws Exception;

    void write(final String message, final boolean newline, final int type) throws Exception;

    void write(final List<String> messages, final boolean newline) throws Exception;

    void write(final String message, final boolean newline) throws Exception;

    void write(final List<String> messages) throws Exception;

    void write(final String message) throws Exception;

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param messages The message as an array of lines of a single string
     * @param type     The type of output
     */
    void writeln(final List<String> messages, int type) throws Exception;

    void writeln(final String message, int type) throws Exception;

    void writeln(final List<String> messages) throws Exception;

    void writeln(final String message) throws Exception;

    /**
     * Sets the verbosity of the output.
     *
     * @param level The level of verbosity
     */
    void setVerbosity(final int level);

    /**
     * Gets the current verbosity of the output.
     *
     * @return The current level of verbosity
     */
    int getVerbosity();

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
     * Sets output formatter.
     *
     * @param formatter
     */
    void setFormatter(final OutputFormatterInterface formatter);

    /**
     * Returns current output formatter instance.
     *
     * @return
     */
    OutputFormatterInterface getFormatter();

}
