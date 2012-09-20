/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.tester.CommandTester;

public class ListCommandTest {

	// @Test
	public void testExecute() {
		Application application = new Application();

		Command command = application.get("list");
		CommandTester commandTester = new CommandTester(command = application.get("list"));
		Map<String, String> input = new HashMap<String, String>();
		input.put("command", command.getName());
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("decorated", false);
        commandTester.execute(input, options);
        // assertRegExp("/help   Displays help for a command/", commandTester.getDisplay(), "execute() returns a list of available commands");

        input.put("--raw", "true");
        commandTester.execute(input);
        String output = "help   Displays help for a command\nlist   Lists commands\n\n";
        // assertEquals(output.replace("\n", LINE_SEPARATOR), commandTester.getDisplay(), "boo");
    }
}
