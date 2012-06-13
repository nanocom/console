package org.nanocom.console.fixtures;

import org.nanocom.console.command.Command;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class FooCommand extends Command {

    public InputInterface input;
    public OutputInterface output;

    public FooCommand() throws Exception {
        super("foo:bar");

        setDescription("The foo:bar command")
        .setAliases(Arrays.asList("afoobar"))
        ;
    }

    @Override
    protected void configure() {
    }

    @Override
    protected void interact(InputInterface input, OutputInterface output) throws Exception {
        output.writeln("interact called");
    }

    @Override
    protected int execute(InputInterface input, OutputInterface output) throws Exception {
        this.input = input;
        this.output = output;

        output.writeln("called");

        return 1;
    }
}
