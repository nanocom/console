package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;

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
