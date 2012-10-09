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

    public static enum VerbosityLevel {

        QUIET(0),
        NORMAL(1),
        VERBOSE(2);

        private int level;

        VerbosityLevel(int level) {
            this.level = level;
        }

        public static VerbosityLevel createFromInt(int number) {
            for (VerbosityLevel value : values()) {
                if (number == value.level) {
                    return value;
                }
            }

            return null;
        }
    }

    public static enum OutputType {

        NORMAL,
        RAW,
        PLAIN;
    }

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines
     * @param newline  Whether to add a newline or not
     * @param type     The type of output
     *
     * @throws IllegalArgumentException When unknown output type is given
     */
    void write(List<String> messages, boolean newline, OutputType type);

    /**
     * Writes a message to the output.
     *
     * @param message The message as a single string
     * @param newline Whether to add a newline or not
     * @param type    The type of output
     *
     * @throws IllegalArgumentException When unknown output type is given
     */
    void write(String message, boolean newline, OutputType type);

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines
     * @param newline  Whether to add a newline or not
     */
    void write(List<String> messages, boolean newline);

    /**
     * Writes a message to the output.
     *
     * @param message The message as a single string
     * @param newline Whether to add a newline or not
     */
    void write(String message, boolean newline);

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines
     */
    void write(List<String> messages);

    /**
     * Writes a message to the output.
     *
     * @param message The message as a single string
     */
    void write(String message);

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param messages The message as an array of lines
     * @param type     The type of output
     */
    void writeln(List<String> messages, OutputType type);

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param message The message as a single string
     * @param type    The type of output
     */
    void writeln(String message, OutputType type);

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param messages The message as an array of lines
     */
    void writeln(List<String> messages);

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param message The message as a single string
     */
    void writeln(String message);

    /**
     * Sets the verbosity of the output.
     *
     * @param level The level of verbosity
     */
    void setVerbosity(VerbosityLevel level);

    /**
     * Gets the current verbosity of the output.
     *
     * @return The current level of verbosity
     */
    VerbosityLevel getVerbosity();

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
