/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import java.util.Arrays;
import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputParameterInterface;
import org.nanocom.console.output.OutputInterface;

/**
 * HelpCommand displays the help for a given command.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class HelpCommand extends Command {

    private Command command;

    /**
     * {@inheritdoc}
     */
    @Override
    protected void configure() {
        ignoreValidationErrors();

        setName("help");
        setDefinition(new InputParameterInterface[] {
            new InputArgument("command_name", InputArgument.OPTIONAL, "The command name", "help")
        });
        setDescription("Displays help for a command");
        setHelp("The <info>%command.name%</info> command displays help for a given command:" + LINE_SEPARATOR + LINE_SEPARATOR
                + "  <info>java -jar %command.full_name% list</info>"
        );
    }

    /**
     * Sets the command.
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
    protected int execute(InputInterface input, OutputInterface output) {
        if (null == command) {
            command = getApplication().get((String) input.getArgument("command_name"));
        }

        output.writeln(command.asText());

        command = null;

        return 0;
    }
}
