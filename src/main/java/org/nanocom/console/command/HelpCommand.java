/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * HelpCommand displays the help for a given command.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class HelpCommand extends Command {

    private Command command;

    public HelpCommand(final String name) throws Exception {
        super(name);
    }

    public HelpCommand() throws Exception {
        super("help");
    }

    /**
     * {@inheritdoc}
     */
    @Override
    protected void configure() {
        ignoreValidationErrors();

        try {
            setDefinition(Arrays.asList(
                    new InputArgument("command_name", InputArgument.OPTIONAL, "The command name", "help"),
                    new InputOption("xml", null, InputOption.VALUE_NONE, "To output help as XML")
            ))
            .setDescription("Displays help for a command")
            .setHelp("The <info>help</info> command displays help for a given command:\n"
                + "<info>php app/console help list</info>\n"
                + "You can also output the help as XML by using the <comment>--xml</comment> option:\n"
                + "<info>help --xml list</info>");
        } catch (Exception e) {
            
        }
    }

    /**
     * Sets the command
     *
     * @param command The command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    protected int execute(InputInterface input, OutputInterface output) throws Exception {
        if (null == command) {
            command = getApplication().get((String) input.getArgument("command_name"));
        }

        if (null != input.getOption("xml")) {
            output.writeln(Arrays.asList(command.asXml(false)), OutputInterface.OUTPUT_RAW);
        } else {
            output.writeln(Arrays.asList(command.asText()), OutputInterface.OUTPUT_NORMAL);
        }

        command = null;
        
        return 1;
    }

}
