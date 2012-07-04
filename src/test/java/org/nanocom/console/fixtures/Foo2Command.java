package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

public class Foo2Command extends Command {

    @Override
    protected void configure() {
        setName("foo1:bar");
        setDescription("The foo1:bar command");
        setAliases(Arrays.asList("afoobar2"));
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        return 1;
    }
}
