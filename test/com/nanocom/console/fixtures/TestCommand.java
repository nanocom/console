package com.nanocom.console.fixtures;

import com.nanocom.console.command.Command;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class TestCommand extends Command {

    public TestCommand() throws Exception {
        super("namespace:name");

        setAliases(Arrays.asList("name"))
        .setDescription("description")
        .setHelp("help")
        ;
    }

    @Override
    protected void configure() throws Exception {
        
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) throws Exception {
        output.writeln("execute called");

        return 1;
    }

    @Override
    protected void interact(InputInterface input, OutputInterface output) throws Exception {
        output.writeln("interact called");
    }

}
