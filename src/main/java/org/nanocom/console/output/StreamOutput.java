/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import java.io.PrintStream;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import org.nanocom.console.formatter.OutputFormatterInterface;

/**
 * StreamOutput writes the output to a given stream.
 *
 * Usage:
 *
 * OutputInterface output = new StreamOutput(fopen('php://stdout', 'w'));
 *
 * As `StreamOutput` can use any stream, you can also use a file:
 *
 * OutputInterface output = new StreamOutput(fopen('/path/to/output.log', 'a', false));
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at jump-informatique dot com>
 */
public class StreamOutput extends Output {

    private PrintStream stream;

   /**
    * Constructor.
    *
    * @param stream    A stream resource
    * @param verbosity The verbosity level (QUIET, NORMAL, VERBOSE)
    * @param decorated Whether to decorate messages or not (null for auto-guessing)
    * @param formatter Output formatter instance
    *
    * @throws IllegalArgumentException When first argument is not a real stream
    */
    public StreamOutput(PrintStream stream, VerbosityLevel verbosity, Boolean decorated, OutputFormatterInterface formatter) {
        if (null == stream) {
            throw new IllegalArgumentException("The stream cannot be null.");
        }

        this.stream = stream;

        if (null == decorated) {
            decorated = hasColorSupport();
        }

        init(verbosity, decorated, formatter);
    }

   /**
    * Constructor.
    *
    * @param stream    A stream resource
    * @param verbosity The verbosity level (QUIET, NORMAL, VERBOSE)
    * @param decorated Whether to decorate messages or not (null for auto-guessing)
    *
    * @throws IllegalArgumentException When first argument is not a real stream
    */
    public StreamOutput(PrintStream stream, VerbosityLevel verbosity, Boolean decorated) {
        this(stream, verbosity, decorated, null);
    }

   /**
    * Constructor.
    *
    * @param stream    A stream resource
    * @param verbosity The verbosity level (QUIET, NORMAL, VERBOSE)
    *
    * @throws IllegalArgumentException When first argument is not a real stream
    */
    public StreamOutput(PrintStream stream, VerbosityLevel verbosity) {
        this(stream, verbosity, null);
    }

    /**
    * Constructor.
    *
    * @param stream A stream resource
    *
    * @throws IllegalArgumentException When first argument is not a real stream
    */
    public StreamOutput(PrintStream stream) {
        this(stream, VerbosityLevel.NORMAL);
    }

    /**
    * Constructor.
    */
    public StreamOutput() {
        super();
    }

    /**
     * Gets the stream attached to this StreamOutput instance.
     *
     * @return A stream resource
     */
    public PrintStream getStream() {
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doWrite(String message, boolean newline) {
        if (newline) {
            stream.println(message);
        } else {
            stream.print(message);
        }

        stream.flush();
    }

    /**
     * Returns true if the stream supports colorization.
     *
     * Colorization is disabled if not supported by the stream:
     *
     *  -  windows without ansicon
     *  -  non tty consoles
     *
     * @return true if the stream supports colorization, false otherwise
     */
    protected final boolean hasColorSupport() {
        if (IS_OS_WINDOWS) {
            return null != System.getenv("ANSICON");
        }

        return null != System.console(); // FIXME
    }
}
