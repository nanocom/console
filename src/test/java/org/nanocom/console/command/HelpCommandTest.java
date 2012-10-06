/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.tester.CommandTester;

public class HelpCommandTest {

    public HelpCommandTest() {
    }

    @Test
    public void testExecute() {
        HelpCommand command = new HelpCommand();

        CommandTester commandTester = new CommandTester(command);
        command.setCommand(new ListCommand());
        commandTester.execute(new HashMap<String, String>());
        assertTrue("execute() returns a text help for the given command", commandTester.getDisplay().contains("list [--raw] [namespace]"));

        Application application = new Application();
        commandTester = new CommandTester(application.get("help"));
        Map<String, String> input = new HashMap<String, String>();
        input.put("command_name", "list");
        commandTester.execute(input);
        assertTrue("execute() returns a text help for the given command", commandTester.getDisplay().contains("list [--raw] [namespace]"));
    }
}