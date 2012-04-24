package com.nanocom.output;

/**
 * NullOutput suppresses all output.
 *
 *     OutputInterface output = new NullOutput();
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public final class NullOutput extends Output {

    public NullOutput(final Integer verbosity, final boolean decorated, final com.nanocom.formatter.OutputFormatterInterface formatter) {
        super(verbosity, decorated, formatter);
    }

    /**
     * Writes a message to the output.
     *
     * @param message A message to write to the output
     * @param newline Whether to add a newline or not
     */
    @Override
    public void doWrite(final String message, final boolean newline)
    {
    }

}
