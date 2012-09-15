/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console;

import org.nanocom.console.input.StringInput;
import org.nanocom.console.output.ConsoleOutput;
import org.nanocom.console.output.OutputInterface;

/**
 * A Shell wraps an Application to add shell capabilities to it.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Shell {

    private Application application;
    // private String history;
    private OutputInterface output;
    private String prompt;
    private Boolean processIsolation;

    /**
     * @param application An application instance
     */
    public Shell(Application application) {
        this.application = application;
        // history = System.getenv("HOME") + "/.history_" + application.getName();
        output = new ConsoleOutput();
        prompt = application.getName() + " > ";
        processIsolation = false;
    }

    /**
     * Runs the shell.
     *
     * @throws Exception
     */
    public void run() {
        application.setAutoExit(false);
        application.setCatchExceptions(true);

        // TODO
        // readline_read_history(history);
        // readline_completion_function(array(this, "autocompleter"));

        output.writeln(getHeader());
        /*php = null;
        if (this.processIsolation) {
            finder = new PhpExecutableFinder();
            php = finder.find();
            this.output.writeln("<info>Running with process isolation, you should consider this:</info>\n" +
            		"    * each command is executed as separate process,\n" +
            		"    * commands don't support interactivity, all params must be passed explicitly,\n" +
            		"    * commands output is not colorized.";
            );
        }*/

        while (true) {
            String command = this.readline();

            if ("" == command) {
                output.writeln("\n");

                break;
            }

            /*if (this.hasReadline) {
                readline_add_history(command);
                readline_write_history(this.history);
            }*/

            /* if (this.processIsolation) {
                pb = new ProcessBuilder();

                process = pb
                    .add(php)
                    .add(_SERVER["argv"][0])
                    .add(command)
                    .inheritEnvironmentVariables(true)
                    .getProcess()
                ;

                output = this.output;
                process.run(void(type, data) use (output) {
                    output.writeln(data);
                });

                ret = process.getExitCode();
            } else {*/
                int ret = this.application.run(new StringInput(command), this.output);
            //}

            if (0 != ret) {
                output.writeln(String.format("<error>The command terminated with an error status (%s)</error>", ret));
            }
        }
    }

    /**
     * Returns the shell header.
     *
     * @return The header string
     */
    protected String getHeader() {
        return "Welcome to the <info>{this.application.getName()}</info> shell (<comment>{this.application.getVersion()}</comment>).\n\n" +
        		"At the prompt, type <comment>help</comment> for some help, or <comment>list</comment> to get a list of available commands.\n\n" +
        		"To exit the shell, type <comment>^D</comment>.";
    }

    /**
     * Tries to return autocompletion for the current entered text.
     *
     * @param The last segment of the entered text
     * @return Boolean|array A list of guessed strings or true
     */
    /*private void autocompleter(String text) {
        info = readline_info();
        text = substr(info["line_buffer"], 0, info["end"]);

        if (info["point"] != info["end"]) {
            return true;
        }

        // task name?
        if (false == strpos(text, " ") || !text) {
            return array_keys(this.application.all());
        }

        // options and arguments?
        try {
            command = this.application.find(substr(text, 0, strpos(text, " ")));
        } catch (Exception e) {
            return true;
        }

        list = array("--help");
        foreach (command.getDefinition().getOptions() as option) {
            list[] = "--".option.getName();
        }

        return list;
    }*/

    /**
     * Reads a single line from standard input.
     *
     * @return The single line from standard input
     */
    private String readline() {
    	System.console().printf(prompt);

    	return System.console().readLine();
    }

    public boolean getProcessIsolation() {
        return processIsolation;
    }

    public void setProcessIsolation(boolean processIsolation) {
        this.processIsolation = processIsolation;
    }
}
