package org.nanocom.console.command;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.tester.CommandTester;

public class ListCommandTest {

	@Test
	public void testExecute() {
		Application application = new Application();

		Command command = application.get("list");
		CommandTester commandTester = new CommandTester(command = application.get("list"));
		Map<String, String> input = new HashMap<String, String>();
		input.put("command", command.getName());
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("decorated", false);
        commandTester.execute(input, options);
        // assertRegExp("/help   Displays help for a command/", commandTester.getDisplay(), ".execute() returns a list of available commands");

        input.put("--xml", "true");
        commandTester.execute(input);
        // assertRegExp("/<command id="list" name="list">/", commandTester.getDisplay(), ".execute() returns a list of available commands in XML if --xml is passed");

        input.remove("--xml");
        /*input.put("--raw", "true");
        commandTester.execute(input);*/
        /*String output = "help   Displays help for a command\n" +
        		 "list   Lists commands\n\n";*/

        // assertEquals(str_replace("\n", System.getProperty("line.separator"), output), commandTester.getDisplay(), "boo");
    }

}
