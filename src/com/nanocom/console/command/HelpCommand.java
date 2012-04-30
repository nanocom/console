package com.nanocom.console.command;

import com.nanocom.console.input.InputArgument;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.input.InputOption;
import com.nanocom.console.output.OutputInterface;
import java.util.Arrays;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
/**
 * HelpCommand displays the help for a given command.
 *
 * @author Fabien Potencier <fabien@symfony.com>
 */
public class HelpCommand extends Command {

    public HelpCommand() throws Exception {
        super("help");
    }

//    private Command command;
//
//    public HelpCommand(final String name) throws Exception {
//        super(name);
//    }
//
//    public HelpCommand() throws Exception {
//        super(null);
//    }
//
//    /**
//     * {@inheritdoc}
//     */
//    @Override
//    protected void configure() {
//        ignoreValidationErrors();
//
//        try {
//            setDefinition(Arrays.asList(
//                    new InputArgument("command_name", InputArgument.OPTIONAL, "The command name", "help"),
//                    new InputOption("xml", null, InputOption.VALUE_NONE, "To output help as XML")
//            ))
//            .setName("help")
//            .setDescription("Displays help for a command")
//            .setHelp("The <info>help</info> command displays help for a given command:"
//                + "<info>php app/console help list</info>"
//                + "You can also output the help as XML by using the <comment>--xml</comment> option:"
//                + "<info>help --xml list</info>");
//        } catch (Exception e) {
//            
//        }
//    }
//
//    /**
//     * Sets the command
//     *
//     * @param command The command to set
//     */
//    public void setCommand(Command command) {
//        this.command = command;
//    }
//
//    /**
//     * {@inheritdoc}
//     */
//    @Override
//    protected int execute(InputInterface input, OutputInterface output) throws Exception {
//        if (null == command) {
//            command = getApplication().get((String)input.getArgument("command_name"));
//        }
//
//        if (null != input.getOption("xml")) {
//            output.writeln(Arrays.asList(command.asXml(false)), OutputInterface.OUTPUT_RAW);
//        } else {
//            output.writeln(Arrays.asList(command.asText()), OutputInterface.OUTPUT_NORMAL);
//        }
//
//        command = null;
//        
//        return 1;
//    }

}
