package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;

public class Foo3Command extends Command {

    public Foo3Command() {
        super("foo3:bar");
        setDescription("The foo3:bar command");
    }

    @Override
    protected void configure() {
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        try {
            throw new RuntimeException("First exception");
        } catch (RuntimeException e) {
            throw new RuntimeException("Second exception", e);
        }
    }
}
