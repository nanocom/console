package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Foo1Command extends Command {

    public InputInterface input;
    public OutputInterface output;

    public Foo1Command() throws Exception {
        super("foo:bar1");

        setDescription("The foo:bar1 command")
        .setAliases(Arrays.asList("afoobar1"))
        ;
    }

    @Override
    protected void configure() {

    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        this.input = input;
        this.output = output;

        return 1;
    }

}
