package com.nanocom.console.output;

import com.nanocom.console.formatter.OutputFormatterInterface;
import java.util.List;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ConsoleOutput implements ConsoleOutputInterface {

    @Override
    public OutputInterface getErrorOutput() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setErrorOutput(OutputInterface error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(final List<String> messages, final boolean newline, final int type) throws Exception {
        for (String message : messages) {
            write(message, newline, type);
        }
    }

    @Override
    public void write(final String message, final boolean newline, final int type) throws Exception {
        System.out.print(message);
    }

    @Override
    public void write(final List<String> messages, final boolean newline) throws Exception {
        write(messages, newline, 0);
    }

    @Override
    public void write(final String message, final boolean newline) throws Exception {
        write(message, newline, 0);
    }

    @Override
    public void write(final List<String> messages) throws Exception {
        write(messages, false);
    }

    @Override
    public void write(final String message) throws Exception {
        write(message, false);
    }

    @Override
    public void writeln(final List<String> messages, int type) {
        for (String message : messages) {
            writeln(message, type);
        }
    }

    @Override
    public void writeln(final String message, int type) {
        System.out.println(message);
    }

    @Override
    public void writeln(final List<String> messages) {
        writeln(messages, 0);
    }

    @Override
    public void writeln(final String message) {
        writeln(message, 0);
    }

    @Override
    public void setVerbosity(int level) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getVerbosity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDecorated(boolean decorated) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDecorated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFormatter(OutputFormatterInterface formatter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputFormatterInterface getFormatter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
