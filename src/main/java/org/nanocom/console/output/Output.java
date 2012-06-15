/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.nanocom.console.formatter.OutputFormatter;
import org.nanocom.console.formatter.OutputFormatterInterface;
import java.util.Arrays;
import java.util.List;

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
    public Output(final Integer verbosity, final boolean decorated, final OutputFormatterInterface formatter) throws Exception {
        init(verbosity, decorated, formatter);
    }

    public Output(final Integer verbosity, final boolean decorated) throws Exception {
        this(verbosity, decorated, null);
    }

    public Output(final Integer verbosity) throws Exception {
        this(verbosity, false);
    }

    public Output() throws Exception {
        this(OutputInterface.VERBOSITY_NORMAL);
    }

    protected final void init(final Integer verbosity, final boolean decorated, final OutputFormatterInterface formatter) throws Exception {
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
    public void setDecorated(final boolean decorated) {
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
    public void writeln(List<String> messages, int type) throws Exception {
        write(messages, true, type);
    }

    @Override
    public void writeln(String message, int type) throws Exception {
        write(message, true, type);
    }

    @Override
    public void writeln(List<String> messages) throws Exception {
        write(messages, true, 0);
    }

    @Override
    public void writeln(String message) throws Exception {
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
    public void write(final List<String> messages, final boolean newline, final int type) throws Exception {
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
                    throw new Exception("Unknown output type given (" + type + ")");
            }

            doWrite(message, newline);
        }
    }

    @Override
    public void write(final String message, final boolean newline, final int type) throws Exception {
        write(Arrays.asList(message), newline, type);
    }

    @Override
    public void write(final List<String> messages, final boolean newline) throws Exception {
        write(messages, newline, 0);
    }

    @Override
    public void write(final String message, final boolean newline) throws Exception {
        write(message, newline, 0);
    }

    @Override
    public void write(final List<String> messages) throws Exception {
        write(messages, false, 0);
    }

    @Override
    public void write(final String message) throws Exception {
        write(message, false, 0);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    abstract public void doWrite(final String message, final boolean newline) throws Exception;

}
