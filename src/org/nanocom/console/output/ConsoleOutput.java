/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ConsoleOutput extends StreamOutput implements ConsoleOutputInterface {

    private OutputInterface stderr;

    /**
     * @param verbosity The verbosity level
     * @param decorated Whether to decorate messages or not (null for auto-guessing)
     * @param formatter Output formatter instance
     */
    public ConsoleOutput(final int verbosity, final Boolean decorated, final OutputFormatterInterface formatter) throws Exception {
        super(System.out, verbosity, decorated, formatter);

        stderr = new StreamOutput(System.err, verbosity, decorated, formatter);
    }
 
    public ConsoleOutput(final int verbosity, final Boolean decorated) throws Exception {
        this(verbosity, decorated, null);
    }

    public ConsoleOutput(final int verbosity) throws Exception {
        this(verbosity, null);
    }
 
    public ConsoleOutput() throws Exception {
        this(OutputInterface.VERBOSITY_NORMAL);
    }

    @Override
    public void setDecorated(final boolean decorated) {
        super.setDecorated(decorated);
        stderr.setDecorated(decorated);
    }

    @Override
    public void setFormatter(OutputFormatterInterface formatter) {
        super.setFormatter(formatter);
        stderr.setFormatter(formatter);
    }

    @Override
    public void setVerbosity(final int level) {
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
