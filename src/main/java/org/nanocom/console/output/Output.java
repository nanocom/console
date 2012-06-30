/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import java.util.Arrays;
import java.util.List;

import org.nanocom.console.formatter.OutputFormatter;
import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * Base class for output classes.
 *
 * There are three levels of verbosity:
 *
 *  * normal: no option passed (normal output - information)
 *  * verbose: -v (more output - debug)
 *  * quiet: -q (no output)
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public abstract class Output implements OutputInterface {

    private Integer verbosity;
    private OutputFormatterInterface formatter;

    /**
     * @param verbosity The verbosity level (self::VERBOSITY_QUIET, self::VERBOSITY_NORMAL, self::VERBOSITY_VERBOSE)
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     * @param formatter Output formatter instance
     */
    public Output(Integer verbosity, boolean decorated, OutputFormatterInterface formatter) {
        init(verbosity, decorated, formatter);
    }

    public Output(Integer verbosity, boolean decorated) {
        this(verbosity, decorated, null);
    }

    public Output(Integer verbosity) {
        this(verbosity, false);
    }

    public Output() {
        this(OutputInterface.VERBOSITY_NORMAL);
    }

    protected void init(Integer verbosity, boolean decorated, OutputFormatterInterface formatter) {
        this.verbosity = null == verbosity ? OutputInterface.VERBOSITY_NORMAL : verbosity;
        this.formatter = null == formatter ? new OutputFormatter() : formatter;
        this.formatter.setDecorated(decorated);
    }

    /**
     * Sets output formatter.
     *
     * @param formatter
     */
    @Override
    public void setFormatter(OutputFormatterInterface formatter) {
        this.formatter = formatter;
    }

    /**
     * Returns current output formatter instance.
     *
     * @return
     */
    @Override
    public OutputFormatterInterface getFormatter() {
        return formatter;
    }

    /**
     * Sets the decorated flag.
     *
     * @param decorated Whether to decorate the messages or not
     */
    @Override
    public void setDecorated(boolean decorated) {
        formatter.setDecorated(decorated);
    }

    /**
     * Gets the decorated flag.
     *
     * @return True if the output will decorate messages, false otherwise
     */
    @Override
    public boolean isDecorated() {
        return formatter.isDecorated();
    }

    /**
     * Sets the verbosity of the output.
     *
     * @param level The level of verbosity
     */
    @Override
    public void setVerbosity(int level) {
        verbosity = level;
    }

    /**
     * Gets the current verbosity of the output.
     *
     * @return The current level of verbosity
     */
    @Override
    public int getVerbosity() {
        return verbosity;
    }

    /**
     * Writes a message to the output and adds a newline at the end.
     *
     * @param messages The message as an array of lines of a single string
     * @param type     The type of output
     */
    @Override
    public void writeln(List<String> messages, int type) {
        write(messages, true, type);
    }

    @Override
    public void writeln(String message, int type) {
        write(message, true, type);
    }

    @Override
    public void writeln(List<String> messages) {
        write(messages, true, 0);
    }

    @Override
    public void writeln(String message) {
        write(message, true, 0);
    }

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines of a single string
     * @param newline  Whether to add a newline or not
     * @param type     The type of output
     *
     * @throws Exception When unknown output type is given
     */
    @Override
    public void write(List<String> messages, boolean newline, int type) {
        if (VERBOSITY_QUIET == verbosity) {
            return;
        }

        for (String message : messages) {
            switch (type) {
                case OutputInterface.OUTPUT_NORMAL:
                    message = formatter.format(message);
                    break;
                case OutputInterface.OUTPUT_RAW:
                    break;
                case OutputInterface.OUTPUT_PLAIN:
                    message = /*strip_tags(*/formatter.format(message)/*)*/; // TODO
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown output type given (%s)", type));
            }

            doWrite(message, newline);
        }
    }

    @Override
    public void write(String message, boolean newline, int type) {
        write(Arrays.asList(message), newline, type);
    }

    @Override
    public void write(List<String> messages, boolean newline) {
        write(messages, newline, 0);
    }

    @Override
    public void write(String message, boolean newline) {
        write(message, newline, 0);
    }

    @Override
    public void write(List<String> messages) {
        write(messages, false, 0);
    }

    @Override
    public void write(String message) {
        write(message, false, 0);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    abstract public void doWrite(String message, boolean newline);

}
