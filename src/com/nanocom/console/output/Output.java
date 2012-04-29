package com.nanocom.console.output;

import com.nanocom.console.formatter.OutputFormatterInterface;
import java.util.List;

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
public abstract class Output /*implements OutputInterface*/ {

    private Integer verbosity;
    private OutputFormatterInterface formatter;

//    /**
//     * @param verbosity The verbosity level (VERBOSITY_QUIET, VERBOSITY_NORMAL, VERBOSITY_VERBOSE)
//     * @param decorated Whether to decorate messages or not (null for auto-guessing)
//     * @param formatter Output formatter instance
//     */
//    public Output(final Integer verbosity, final boolean decorated, final OutputFormatterInterface formatter) throws Exception {
//        if (null == formatter) {
//            this.formatter = (OutputFormatterInterface)new OutputFormatter();
//        }
//
//        this.verbosity = null == verbosity ? VERBOSITY_NORMAL : verbosity;
//        this.formatter = formatter;
//        this.formatter.setDecorated(decorated);
//    }
//
//    /**
//     * Sets output formatter.
//     *
//     * @param formatter
//     */
//    @Override
//    public void setFormatter(OutputFormatterInterface formatter) {
//        this.formatter = formatter;
//    }
//
//    /**
//     * Returns current output formatter instance.
//     *
//     * @return
//     */
//    @Override
//    public OutputFormatterInterface getFormatter() {
//        return formatter;
//    }

    /**
     * Sets the decorated flag.
     *
     * @param decorated Whether to decorate the messages or not
     */
    // @Override
    public void setDecorated(final boolean decorated) {
        formatter.setDecorated(decorated);
    }

    /**
     * Gets the decorated flag.
     *
     * @return True if the output will decorate messages, false otherwise
     */
    // @Override
    public boolean isDecorated() {
        return formatter.isDecorated();
    }

    /**
     * Sets the verbosity of the output.
     *
     * @param level The level of verbosity
     */
    // @Override
    public void setVerbosity(int level) {
        verbosity = level;
    }

//    /**
//     * Gets the current verbosity of the output.
//     *
//     * @return The current level of verbosity
//     */
//    @Override
//    public int getVerbosity() {
//        return verbosity;
//    }
//
//    /**
//     * Writes a message to the output and adds a newline at the end.
//     *
//     * @param messages The message as an array of lines of a single string
//     * @param type     The type of output
//     */
//    @Override
//    public void writeln(final List<String> messages, int type) throws Exception {
//        write(messages, true, type);
//    }
//
//    /**
//     * Writes a message to the output.
//     *
//     * @param string|array messages The message as an array of lines of a single string
//     * @param newline  Whether to add a newline or not
//     * @param type     The type of output
//     *
//     * @throws Exception When unknown output type is given
//     */
//    @Override
//    public void write(final List<String> messages, final boolean newline, final int type) throws Exception {
//        if (VERBOSITY_QUIET == verbosity) {
//            return;
//        }
//
//        for (String message : messages) {
//            switch (type) {
//                case OutputInterface.OUTPUT_NORMAL:
//                    message = formatter.format(message);
//                    break;
//                case OutputInterface.OUTPUT_RAW:
//                    break;
//                case OutputInterface.OUTPUT_PLAIN:
//                    message = /*strip_tags(*/formatter.format(message)/*)*/;
//                    break;
//                default:
//                    throw new Exception("Unknown output type given (" + type + ")");
//            }
//
//            doWrite(message, newline);
//        }
//    }
//
//    @Override
//    public void write(final String message, final boolean newline, final int type) throws Exception {
//        
//    }
//
//    @Override
//    public void write(final List<String> messages, final boolean newline) throws Exception {
//        
//    }
//
//    @Override
//    public void write(final String message, final boolean newline) throws Exception {
//        
//    }
//
//    @Override
//    public void write(final List<String> messages) throws Exception {
//        
//    }
//
//    @Override
//    public void write(final String message) throws Exception {
//        
//    }
//
//    /**
//     * Writes a message to the output.
//     *
//     * @param message A message to write to the output
//     * @param newline Whether to add a newline or not
//     */
//    abstract public void doWrite(final String message, final boolean newline) throws Exception;

}
