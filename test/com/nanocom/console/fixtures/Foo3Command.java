package com.nanocom.console.fixtures;

import com.nanocom.console.command.Command;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.output.OutputInterface;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Foo3Command extends Command {

    public Foo3Command() throws Exception {
        super("foo3:bar");

        setDescription("The foo3:bar command");
    }

    @Override
    protected void configure() {
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) throws Exception {
        try {
            throw new Exception("First exception");
        } catch (Exception e) {
            throw new Exception("Second exception", e);
        }
    }
}
