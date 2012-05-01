/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console;

import com.nanocom.console.output.ConsoleOutput;
import com.nanocom.console.output.OutputInterface;

/**
 * A Shell wraps an Application to add shell capabilities to it.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class Shell {

    private Application application;
    private String history;
    private OutputInterface output;
    private Boolean hasReadline;
    private String prompt;
    private Boolean processIsolation;

    /**
     * If there is no readline support for the current PHP executable
     * an exception is thrown.
     *
     * @param application An application instance
     */
    public void Shell(Application application) throws Exception {
        hasReadline = false; // function_exists("readline");
        this.application = application;
        history = System.getenv("HOME") + "/.history_" + application.getName();
        output = new ConsoleOutput();
        prompt = application.getName() + " > ";
        processIsolation = false;
    }

    /**
     * Runs the shell.
     */
    public void run() {
        application.setAutoExit(false);
        application.setCatchExceptions(true);

        if (hasReadline) {
            // TODO
            // readline_read_history(history);
            // readline_completion_function(array(this, "autocompleter"));
        }

        /*output.writeln(this.getHeader());
        php = null;
        if (this.processIsolation) {
            finder = new PhpExecutableFinder();
            php = finder.find();
            this.output.writeln(<<<EOF
<info>Running with process isolation, you should consider this:</info>
  * each command is executed as separate process,
  * commands don"t support interactivity, all params must be passed explicitly,
  * commands output is not colorized.

EOF
            );
        }

        while (true) {
            command = this.readline();

            if (false === command) {
                this.output.writeln(\"\n\");

                break;
            }

            if (this.hasReadline) {
                readline_add_history(command);
                readline_write_history(this.history);
            }

            if (this.processIsolation) {
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
            } else {
                ret = this.application.run(new StringInput(command), this.output);
            }

            if (0 !== ret) {
                this.output.writeln(sprintf("<error>The command terminated with an error status (%s)</error>", ret));
            }
        }*/
    }

    /**
     * Returns the shell header.
     *
     * @return string The header string
     */
    protected void getHeader() {
        /* return <<<EOF

Welcome to the <info>{this.application.getName()}</info> shell (<comment>{this.application.getVersion()}</comment>).

At the prompt, type <comment>help</comment> for some help,
or <comment>list</comment> to get a list of available commands.

To exit the shell, type <comment>^D</comment>.

EOF;*/
    }

    /**
     * Tries to return autocompletion for the current entered text.
     *
     * @param text The last segment of the entered text
     * @return Boolean|array A list of guessed strings or true
     */
    private void autocompleter(final String text) {
        /*info = readline_info();
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

        return list; */
    }

    /**
     * Reads a single line from standard input.
     *
     * @return string The single line from standard input
     */
    private void readline() {
        /*if (hasReadline) {
            line = readline(this.prompt);
        } else {
            this.output.write(this.prompt);
            line = fgets(STDIN, 1024);
            line = (!line && strlen(line) == 0) ? false : rtrim(line);
        }

        return line;*/
    }

    public boolean getProcessIsolation() {
        return processIsolation;
    }

    public void setProcessIsolation(final boolean processIsolation) {
        this.processIsolation = processIsolation;
    }

}
