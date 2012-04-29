package com.nanocom.console.output;

/**
 * ConsoleOutputInterface is the interface implemented by ConsoleOutput class.
 * This adds information about stderr output stream.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface ConsoleOutputInterface extends OutputInterface {

    /**
     * @return OutputInterface
     */
    public OutputInterface getErrorOutput();

    /**
     * @param error 
     */
    public void setErrorOutput(final OutputInterface error);

}
