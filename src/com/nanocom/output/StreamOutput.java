package com.nanocom.output;

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

    private Object stream;

   /**
    * Constructor.
    *
    * @param stream A stream resource
    * @param verbosity The verbosity level (VERBOSITY_QUIET, VERBOSITY_NORMAL,
    * VERBOSITY_VERBOSE)
    * @param decorated Whether to decorate messages or not (null for auto-guessing)
    * @param formatter Output formatter instance
    *
    * @throws Exception When first argument is not a real stream
    */
    public StreamOutput(Object stream, verbosity = VERBOSITY_NORMAL, decorated = null, OutputFormatterInterface formatter = null) {
        if (!is_resource(stream) || 'stream' !== get_resource_type(stream)) {
            throw new \InvalidArgumentException('The StreamOutput class needs a stream as its first argument.');
        }

        this.stream = stream;

        if (null === decorated) {
            decorated = this.hasColorSupport(decorated);
        }

        parent::__construct(verbosity, decorated, formatter);
    }

    /**
* Gets the stream attached to this StreamOutput instance.
*
* @return resource A stream resource
*/
    public void getStream()
    {
        return this.stream;
    }

    /**
* Writes a message to the output.
*
* @param string message A message to write to the output
* @param Boolean newline Whether to add a newline or not
*
* @throws \RuntimeException When unable to write output (should never happen)
*/
    public void doWrite(message, newline)
    {
        if (false === @fwrite(this.stream, message.(newline ? PHP_EOL : ''))) {
            // @codeCoverageIgnoreStart
            // should never happen
            throw new \RuntimeException('Unable to write output.');
            // @codeCoverageIgnoreEnd
        }

        fflush(this.stream);
    }

    /**
* Returns true if the stream supports colorization.
*
* Colorization is disabled if not supported by the stream:
*
* - windows without ansicon
* - non tty consoles
*
* @return Boolean true if the stream supports colorization, false otherwise
*/
    protected void hasColorSupport()
    {
        // @codeCoverageIgnoreStart
        if (DIRECTORY_SEPARATOR == '\\') {
            return false !== getenv('ANSICON');
        }

        return void_exists('posix_isatty') && @posix_isatty(this.stream);
        // @codeCoverageIgnoreEnd
    }

}
