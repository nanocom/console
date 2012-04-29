package com.nanocom.console.output;

import java.io.PrintStream;

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

//    private PrintStream stream;
//
//   /**
//    * Constructor.
//    *
//    * @param stream    A stream resource
//    * @param verbosity The verbosity level (VERBOSITY_QUIET, VERBOSITY_NORMAL, VERBOSITY_VERBOSE)
//    * @param decorated Whether to decorate messages or not (null for auto-guessing)
//    * @param formatter Output formatter instance
//    *
//    * @throws Exception When first argument is not a real stream
//    */
//    public StreamOutput(PrintStream stream, verbosity = VERBOSITY_NORMAL, decorated = null, OutputFormatterInterface formatter = null) {
//        if (!is_resource(stream) || 'stream' !== get_resource_type(stream)) {
//            throw new \InvalidArgumentException('The StreamOutput class needs a stream as its first argument.');
//        }
//
//        this.stream = stream;
//
//        if (null === decorated) {
//            decorated = this.hasColorSupport(decorated);
//        }
//
//        super(verbosity, decorated, formatter);
//    }
//
//    /**
//     * Gets the stream attached to this StreamOutput instance.
//     *
//     * @return A stream resource
//     */
//    public void getStream() {
//        return stream;
//    }
//
//    /**
//     * Writes a message to the output.
//     *
//     * @param message A message to write to the output
//     * @param newline Whether to add a newline or not
//     *
//     * @throws Exception When unable to write output (should never happen)
//     */
//    @Override
//    public void doWrite(final String message, final boolean newline) throws Exception {
//        stream.write(message.getBytes());
//        if (newline) {
//            // Add PHP_EOL
//        }
//
//        stream.flush();
//    }
//
//    /**
//     * Returns true if the stream supports colorization.
//     *
//     * Colorization is disabled if not supported by the stream:
//     * - windows without ansicon
//     * - non tty consoles
//     *
//     * @return True if the stream supports colorization, false otherwise
//     */
//    protected boolean hasColorSupport() {
//        /*if (DIRECTORY_SEPARATOR == '\\') {
//            return false != getenv('ANSICON');
//        }
//
//        return void_exists('posix_isatty') && @posix_isatty(this.stream);*/
//        
//        return false;
//    }

}
