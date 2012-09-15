/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.tester;

import java.util.HashMap;
import java.util.Map;
import org.nanocom.console.command.Command;
import org.nanocom.console.input.ArrayInput;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;
import org.nanocom.console.output.OutputInterface.VerbosityLevel;
import org.nanocom.console.output.StreamOutput;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class CommandTester {

    private Command command;
    private InputInterface input;
    private StreamOutput output;

    /**
     * @param command A Command instance to test.
     */
    public CommandTester(Command command) {
        this.command = command;
    }

    /**
     * Executes the command.
     *
     * Available options:
     *
     *  * interactive: Sets the input interactive flag
     *  * decorated:   Sets the output decorated flag
     *  * verbosity:   Sets the output verbosity flag
     *
     * @param input   An array of arguments and options
     * @param options An array of options
     *
     * @return The command exit code
     */
    public int execute(Map<String, String> input, Map<String, Object> options) {
        this.input = new ArrayInput(input);
        if (options.containsKey("interactive")) {
            this.input.setInteractive((Boolean) options.get("interactive"));
        }

        output = new StreamOutput(System.out);
        // StreamOutput output = new StreamOutput(fopen("php://memory", "w", false));
        if (options.containsKey("decorated")) {
            output.setDecorated((Boolean) options.get("decorated"));
        }

        if (options.containsKey("verbosity")) {
            output.setVerbosity(VerbosityLevel.createFromInt((Integer) options.get("verbosity")));
        }

        return command.run(this.input, (OutputInterface) output);
    }

    public int execute(Map<String, String> input) {
        return execute(input, new HashMap<String, Object>());
    }

    /**
     * Gets the display returned by the last execution of the command.
     *
     * @return The display
     */
    /*public String getDisplay() {
        // rewind(output.getStream());
        // TODO return stream_get_contents(output.getStream());
        return "";
    }*/

    /**
     * Gets the input instance used by the last execution of the command.
     *
     * @return The current input instance
     */
    public InputInterface getInput() {
        return input;
    }

    /**
     * Gets the output instance used by the last execution of the command.
     *
     * @return The current output instance
     */
    public OutputInterface getOutput() {
        return output;
    }
}
