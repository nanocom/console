/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import java.io.File;
import java.io.PrintStream;

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
    * @param stream    A stream resource
    * @param verbosity The verbosity level (VERBOSITY_QUIET, VERBOSITY_NORMAL, VERBOSITY_VERBOSE)
    * @param decorated Whether to decorate messages or not (null for auto-guessing)
    * @param formatter Output formatter instance
    *
    * @throws Exception When first argument is not a real stream
    */
    public StreamOutput(final PrintStream stream, final int verbosity, Boolean decorated, final OutputFormatterInterface formatter) throws Exception {
        if (null == stream) {
            throw new Exception("The stream cannot be null.");
        }

        this.stream = stream;

        if (null == decorated) {
            decorated = hasColorSupport();
        }

        init(verbosity, decorated, formatter);
    }

    public StreamOutput(final PrintStream stream) throws Exception {
        this(stream, OutputInterface.VERBOSITY_NORMAL, null, null);
    }

    public StreamOutput() throws Exception {
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
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     *
     * @throws Exception When unable to write output (should never happen)
     */
    @Override
    public void doWrite(final String message, final boolean newline) throws Exception {
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
     * - windows without ansicon
     * - non tty consoles
     *
     * @return True if the stream supports colorization, false otherwise
     */
    protected final boolean hasColorSupport() {
        if ("\\".equals(File.separator)) {
            // Install AnsiCon on Windows to get colors in the command line
            return !"false".equals(System.getenv("ANSICON"));
        }

        // return function_exists('posix_isatty') && @posix_isatty(this.stream);
        // TODO check that this is equivalent to
        return System.console() != null;
    }

}
