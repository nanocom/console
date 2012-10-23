/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.tester;

import java.util.Map;
import org.nanocom.console.Application;
import org.nanocom.console.input.ArrayInput;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.InMemoryOutput;
import org.nanocom.console.output.OutputInterface.VerbosityLevel;

/**
 * Eases the testing of console applications.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ApplicationTester {

    private Application application;
    private InputInterface input;
    private InMemoryOutput output;

    /**
     * Constructor.
     *
     * @param application An Application instance to test
     */
    public ApplicationTester(Application application) {
        this.application = application;
    }

    /**
     * Executes the application.
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
    public int run(Map<String, String> input, Map<String, Object> options) {
        this.input = new ArrayInput(input);
        if (options.containsKey("interactive")) {
            this.input.setInteractive((Boolean) options.get("interactive"));
        }

        output = new InMemoryOutput();
        if (options.containsKey("decorated")) {
            output.setDecorated((Boolean) options.get("decorated"));
        }
        if (options.containsKey("verbosity")) {
            output.setVerbosity((VerbosityLevel) options.get("verbosity"));
        }

        return application.run(this.input, output);
    }

    /**
     * Gets the display returned by the last execution of the application.
     *
     * @return The display
     */
    public String getDisplay() {
        return output.getBuffer().toString();
    }

    /**
     * Gets the input instance used by the last execution of the application.
     *
     * @return The current input instance
     */
    public InputInterface getInput() {
        return input;
    }

    /**
     * Gets the output instance used by the last execution of the application.
     *
     * @return The current output instance
     */
    public InMemoryOutput getOutput() {
        return output;
    }
}