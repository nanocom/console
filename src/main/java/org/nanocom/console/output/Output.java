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
import org.nanocom.console.output.OutputInterface.OutputType;

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

    private VerbosityLevel verbosity;
    private OutputFormatterInterface formatter;

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     * @param formatter Output formatter instance
     */
    public Output(VerbosityLevel verbosity, boolean decorated, OutputFormatterInterface formatter) {
        init(verbosity, decorated, formatter);
    }

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     */
    public Output(VerbosityLevel verbosity, boolean decorated) {
        this(verbosity, decorated, null);
    }

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     */
    public Output(VerbosityLevel verbosity) {
        this(verbosity, false);
    }

    /**
     * Constructor.
     */
    public Output() {
        this(VerbosityLevel.NORMAL);
    }

    protected final void init(VerbosityLevel verbosity, boolean decorated, OutputFormatterInterface formatter) {
        this.verbosity = null == verbosity ? VerbosityLevel.NORMAL : verbosity;
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
    public void setVerbosity(VerbosityLevel level) {
        verbosity = level;
    }

    /**
     * Gets the current verbosity of the output.
     *
     * @return The current level of verbosity
     */
    @Override
    public VerbosityLevel getVerbosity() {
        return verbosity;
    }

    @Override
    public void writeln(List<String> messages, OutputType type) {
        write(messages, true, type);
    }

    @Override
    public void writeln(String message, OutputType type) {
        write(message, true, type);
    }

    @Override
    public void writeln(List<String> messages) {
        write(messages, true, OutputType.NORMAL);
    }

    @Override
    public void writeln(String message) {
        write(message, true, OutputType.NORMAL);
    }

    /**
     * Writes a message to the output.
     *
     * @param messages The message as an array of lines of a single string
     * @param newline  Whether to add a newline or not
     * @param type     The type of output
     *
     * @throws IllegalArgumentException When unknown output type is given
     */
    @Override
    public void write(List<String> messages, boolean newline, OutputType type) {
        if (VerbosityLevel.QUIET.equals(verbosity)) {
            return;
        }

        for (String message : messages) {
            switch (type) {
                case NORMAL:
                    message = formatter.format(message);
                    break;
                case RAW:
                    break;
                case PLAIN:
                    message = /*strip_tags(*/formatter.format(message)/*)*/; // TODO
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown output type given (%s)", type));
            }

            doWrite(message, newline);
        }
    }

    @Override
    public void write(String message, boolean newline, OutputType type) {
        write(Arrays.asList(message), newline, type);
    }

    @Override
    public void write(List<String> messages, boolean newline) {
        write(messages, newline, OutputType.NORMAL);
    }

    @Override
    public void write(String message, boolean newline) {
        write(message, newline, OutputType.NORMAL);
    }

    @Override
    public void write(List<String> messages) {
        write(messages, false, OutputType.NORMAL);
    }

    @Override
    public void write(String message) {
        write(message, false, OutputType.NORMAL);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    abstract protected void doWrite(String message, boolean newline);
}
