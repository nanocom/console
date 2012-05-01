/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.tester;

import com.nanocom.console.Application;
import com.nanocom.console.input.ArrayInput;
import com.nanocom.console.input.InputInterface;
import com.nanocom.console.output.OutputInterface;
import com.nanocom.console.output.StreamOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ApplicationTester {

    private Application application;
    private InputInterface input;
    private OutputInterface output;

    /**
     * @param application An Application instance to test.
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
    public int run(final Map<String, String> input, final Map<String, Object> options) throws Exception {
        this.input = new ArrayInput(input);
        if (options.containsKey("interactive")) {
            this.input.setInteractive((Boolean) options.get("interactive"));
        }

        // TODO Make a memory stream
        // this.output = new StreamOutput(fopen("php://memory", "w", false));
        output = new StreamOutput(new PrintStream(new OutputStream() {

            @Override
            public void write(int i) throws IOException {
                
            }
        }));
        if (options.containsKey("decorated")) {
            output.setDecorated((Boolean) options.get("decorated"));
        }
        if (options.containsKey("verbosity")) {
            output.setVerbosity((Integer) options.get("verbosity"));
        }

        return application.run(this.input, this.output);
    }

    /**
     * Gets the display returned by the last execution of the application.
     *
     * @return The display
     */
    public String getDisplay() {
        // TODO rewind(this.output.getStream());

        // TODO return stream_get_contents(this.output.getStream());
        return "";
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
    public OutputInterface getOutput() {
        return output;
    }

}