/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * InMemoryOutput keeps written messages in memory.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class InMemoryOutput extends Output {

    private StringBuilder buffer;

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     * @param formatter Output formatter instance
     */
    public InMemoryOutput(VerbosityLevel verbosity, OutputFormatterInterface formatter) {
        init(verbosity, false, formatter);
        buffer = new StringBuilder();
    }

    /**
     * Constructor.
     *
     * @param verbosity The verbosity level
     */
    public InMemoryOutput(VerbosityLevel verbosity) {
        this(verbosity, null);
    }

    /**
     * Constructor.
     */
    public InMemoryOutput() {
        this(VerbosityLevel.NORMAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doWrite(String message, boolean newline) {
        buffer.append(message);

         if (newline) {
            buffer.append(LINE_SEPARATOR);
         }
    }

    public StringBuilder getBuffer() {
        return buffer;
    }
}
