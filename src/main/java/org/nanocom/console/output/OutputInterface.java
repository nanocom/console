/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import java.util.List;

import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * OutputInterface is the interface implemented by all Output classes.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface OutputInterface {

    static int VERBOSITY_QUIET   = 0;
    static int VERBOSITY_NORMAL  = 1;
    static int VERBOSITY_VERBOSE = 2;

    static int OUTPUT_NORMAL = 0;
    static int OUTPUT_RAW    = 1;
    static int OUTPUT_PLAIN  = 2;

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines of a single string
     * @param newline  Whether to add a newline or not
     * @param type     The type of output
     *
     * @throws Exception When unknown output type is given
     */
    void write(List<String> messages, boolean newline, int type);

    void write(String message, boolean newline, int type);

    void write(List<String> messages, boolean newline);

    void write(String message, boolean newline);

    void write(List<String> messages);

    void write(String message);

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param messages The message as an array of lines of a single string
     * @param type     The type of output
     */
    void writeln(List<String> messages, int type);

    void writeln(String message, int type);

    void writeln(List<String> messages);

    void writeln(String message);

    /**
     * Sets the verbosity of the output.
     *
     * @param level The level of verbosity
     */
    void setVerbosity(int level);

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
    void setDecorated(boolean decorated);

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
    void setFormatter(OutputFormatterInterface formatter);

    /**
     * Returns current output formatter instance.
     *
     * @return
     */
    OutputFormatterInterface getFormatter();
}
