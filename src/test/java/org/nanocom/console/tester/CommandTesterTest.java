/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.tester;

import java.util.HashMap;
import java.util.Map;
import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nanocom.console.command.Command;
import org.nanocom.console.command.Executable;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.Output;
import org.nanocom.console.output.OutputInterface;

public class CommandTesterTest {

    protected Command command;
    protected CommandTester tester;

    @Before
    public void setUp() {
        command = new Command("foo");
        command.addArgument("command");
        command.addArgument("foo");
        command.setCode(new Executable() {

            @Override
            protected int execute(InputInterface input, OutputInterface output) {
                output.writeln("foo");
                return 0;
            }
        });

        tester = new CommandTester(this.command);
        Map<String, String> input = new HashMap<String, String>();
        input.put("foo", "bar");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("interactive", false);
        options.put("decorated", false);
        options.put("verbosity", Output.VerbosityLevel.VERBOSE);
        tester.execute(input, options);
    }

    @After
    public void tearDown() {
        command = null;
        tester = null;
    }

    @Test
    public void testExecute() {
        assertFalse("execute() takes an interactive option", tester.getInput().isInteractive());
        assertFalse("execute() takes a decorated option", tester.getOutput().isDecorated());
        assertEquals("execute() takes a verbosity option", Output.VerbosityLevel.VERBOSE, tester.getOutput().getVerbosity());
    }

    @Test
    public void testGetInput() {
        assertEquals("getInput() returns the current input instance", "bar", tester.getInput().getArgument("foo"));
    }

    @Test
    public void testGetOutput() {
        assertEquals("getOutput() returns the current output instance", "foo" + LINE_SEPARATOR, tester.getOutput().getBuffer().toString());
    }

    @Test
    public void testGetDisplay() {
        assertEquals("getDisplay() returns the display of the last execution", "foo" + LINE_SEPARATOR, tester.getDisplay().toString());
    }
}
