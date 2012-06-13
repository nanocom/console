package com.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Foo2Command extends Command {

    public Foo2Command() throws Exception {
        super("foo1:bar");

        setDescription("The foo1:bar command")
        .setAliases(Arrays.asList("afoobar2"))
        ;
    }

    @Override
    protected void configure() {
        
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        return 1;
    }

}
