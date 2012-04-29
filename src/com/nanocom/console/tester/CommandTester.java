package com.nanocom.console.tester;

import com.nanocom.console.command.Command;
import com.nanocom.console.input.ArrayInput;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.output.OutputInterface;
import com.nanocom.console.output.StreamOutput;
import java.util.HashMap;
import java.util.Map;

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
    public int execute(Map<String, String> input, Map<String, Object> options) throws Exception {
        this.input = new ArrayInput(input);
        if (options.containsKey("interactive")) {
            this.input.setInteractive((Boolean) options.get("interactive"));
        }

        output = new StreamOutput();
        // StreamOutput output = new StreamOutput(fopen("php://memory", "w", false));
        if (options.containsKey("decorated")) {
            output.setDecorated((Boolean) options.get("decorated"));
        }

        if (options.containsKey("verbosity")) {
            output.setVerbosity((Integer) options.get("verbosity"));
        }

        return command.run(this.input, (OutputInterface) output);
    }

    public int execute(Map<String, String> input) throws Exception {
        return execute(input, new HashMap<String, Object>());
    }

    /**
     * Gets the display returned by the last execution of the command.
     *
     * @return The display
     */
    public String getDisplay() {
        // rewind(output.getStream());

        //return stream_get_contents(output.getStream());
        return "";
    }

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
        return (OutputInterface) output;
    }

}
