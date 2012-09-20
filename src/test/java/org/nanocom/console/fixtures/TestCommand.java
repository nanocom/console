package org.nanocom.console.fixtures;

import java.util.Arrays;
import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;

public class TestCommand extends Command {

    @Override
    protected void configure() {
        setName("namespace:name");
        setAliases(Arrays.asList("name"));
        setDescription("description");
        setHelp("help");
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        output.writeln("execute called");
        return 0;
    }

    @Override
    protected void interact(InputInterface input, OutputInterface output) {
        output.writeln("interact called");
    }
}
