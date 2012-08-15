/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * NullOutput suppresses all output.
 *
 *     NullOutput output = new NullOutput();
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public final class NullOutput extends Output {

    public NullOutput(final Integer verbosity, final boolean decorated, final OutputFormatterInterface formatter) {
        super(verbosity, decorated, formatter);
    }

    public NullOutput(final Integer verbosity, final boolean decorated) {
        this(verbosity, decorated, null);
    }

    public NullOutput(final Integer verbosity) {
        this(verbosity, false);
    }

    public NullOutput() {
        super(OutputInterface.VERBOSITY_NORMAL);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    @Override
    public void doWrite(final String message, final boolean newline) {
    }
}
