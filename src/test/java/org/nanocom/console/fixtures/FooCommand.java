package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

public class FooCommand extends Command {

    public InputInterface input;
    public OutputInterface output;

    @Override
    protected void configure() {
        setName("foo:bar");
        setDescription("The foo:bar command");
        setAliases(Arrays.asList("afoobar"));
    }

    @Override
    protected void interact(InputInterface input, OutputInterface output) {
        output.writeln("interact called");
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        this.input = input;
        this.output = output;

        output.writeln("called");

        return 1;
    }
}
