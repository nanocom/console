package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

public class Foo1Command extends Command {

    public InputInterface input;
    public OutputInterface output;

    @Override
    protected void configure() {
        setName("foo:bar1");
        setDescription("The foo:bar1 command");
        setAliases(Arrays.asList("afoobar1"));
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        this.input = input;
        this.output = output;

        return 1;
    }
}
