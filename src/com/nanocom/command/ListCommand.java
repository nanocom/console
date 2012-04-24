package com.nanocom.command;

import com.nanocom.input.InputArgument;
import com.nanocom.input.InputDefinition;
import com.nanocom.input.InputInterface;
import com.nanocom.input.InputOption;
import com.nanocom.output.OutputInterface;
import java.util.Arrays;

/**
 * ListCommand displays the list of all available commands for the application.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ListCommand extends Command {

    /**
     * {@inheritdoc}
     */
    protected void configure() {
        this.setDefinition(this.createDefinition())
            .setName("list")
            .setDescription("Lists commands")
            .setHelp("The <info>list</info> command lists all commands:"
                + "<info>php app/console list</info>"
                + "You can also display the commands for a specific namespace:"
                + "<info>php app/console list test</info>"
                + "You can also output the information as XML by using the <comment>--xml</comment> option:"
                + "<info>php app/console list --xml</info>");
    }

    /**
     * {@inheritdoc}
     */
    @Override
    protected InputDefinition getNativeDefinition() throws Exception {
        return createDefinition();
    }

    /**
     * {@inheritdoc}
     */
    @Override
    protected int execute(InputInterface input, OutputInterface output) throws Exception {
        if (null != input.getOption("xml")) {
            output.writeln(this.getApplication().asXml(input.getArgument("namespace")), OutputInterface.OUTPUT_RAW);
        } else {
            output.writeln(this.getApplication().asText(input.getArgument("namespace")));
        }
        
        return 1;
    }

    private InputDefinition createDefinition() throws Exception {
        return new InputDefinition(Arrays.asList(
            new InputArgument("namespace", InputArgument.OPTIONAL, "The namespace name"),
            new InputOption("xml", null, InputOption.VALUE_NONE, "To output help as XML")
        ));
    }

}
