package com.nanocom.output;

import com.nanocom.formatter.OutputFormatterInterface;
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
    public void write(List<String> messages, boolean newline, int type) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeln(List<String> mesages, int type) {
        throw new UnsupportedOperationException("Not supported yet.");
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
