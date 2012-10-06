/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * ConsoleOutput is the default class for all console output. It uses STDOUT.
 *
 * This class is a convenient wrapper around `StreamOutput`.
 *
 *     OutputInterface output = new ConsoleOutput();
 *
 * This is equivalent to:
 *
 *     OutputInterface output = new StreamOutput(System.out);
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ConsoleOutput extends StreamOutput implements ConsoleOutputInterface {

    private OutputInterface stderr;

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     * @param formatter Output formatter instance
     */
    public ConsoleOutput(VerbosityLevel verbosity, Boolean decorated, OutputFormatterInterface formatter) {
        super(System.out, verbosity, decorated, formatter);
        stderr = new StreamOutput(System.err, verbosity, decorated, formatter);
    }

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     */
    public ConsoleOutput(VerbosityLevel verbosity, Boolean decorated) {
        this(verbosity, decorated, null);
    }

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     */
    public ConsoleOutput(VerbosityLevel verbosity) {
        this(verbosity, null);
    }

    /**
     * Constructor.
     */
    public ConsoleOutput() {
        this(VerbosityLevel.NORMAL);
    }

    @Override
    public void setDecorated(boolean decorated) {
        super.setDecorated(decorated);
        stderr.setDecorated(decorated);
    }

    @Override
    public void setFormatter(OutputFormatterInterface formatter) {
        super.setFormatter(formatter);
        stderr.setFormatter(formatter);
    }

    @Override
    public void setVerbosity(VerbosityLevel level) {
        super.setVerbosity(level);
        stderr.setVerbosity(level);
    }

    @Override
    public OutputInterface getErrorOutput() {
        return stderr;
    }

    @Override
    public void setErrorOutput(OutputInterface error) {
        stderr = error;
    }
}
