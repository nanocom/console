package org.nanocom.console.command;

import java.util.Arrays;

import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.output.OutputInterface;

/**
 * ListCommand displays the list of all available commands for the application.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ListCommand extends Command {

    public ListCommand(String name) {
        super(name);
    }

    public ListCommand() {
        super("list");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        try {
            setDefinition(createDefinition())
            .setName("list");
            setDescription("Lists commands")
            .setHelp("The <info>list</info> command lists all commands:"
                + "<info>php app/console list</info>"
                + "You can also display the commands for a specific namespace:"
                + "<info>php app/console list test</info>"
                + "You can also output the information as XML by using the <comment>--xml</comment> option:"
                + "<info>php app/console list --xml</info>");
        } catch (Exception ex) {
            // Does not happen
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputDefinition getNativeDefinition() {
        return createDefinition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int execute(InputInterface input, OutputInterface output) {
        if (null != input.getOption("xml")) {
            // TODO
            // output.writeln(this.getApplication().asXml(input.getArgument("namespace")), OutputInterface.OUTPUT_RAW);
        } else {
            // TODO
            // output.writeln(this.getApplication().asText(input.getArgument("namespace")));
        }
        
        return 1;
    }

    private InputDefinition createDefinition() {
        return new InputDefinition(Arrays.asList(
            new InputArgument("namespace", InputArgument.OPTIONAL, "The namespace name"),
            new InputOption("xml", null, InputOption.VALUE_NONE, "To output help as XML")
        ));
    }

}
