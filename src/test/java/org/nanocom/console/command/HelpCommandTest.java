package org.nanocom.console.command;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.tester.CommandTester;

public class HelpCommandTest {

	@Test
    public void testExecute() throws Exception {
        HelpCommand command = new HelpCommand();

        CommandTester commandTester = new CommandTester(command);
        command.setCommand(new ListCommand());
        commandTester.execute(new HashMap<String, String>());
        // assertRegExp(".execute() returns a text help for the given command", "list [--xml] [--raw] [namespace]", commandTester.getDisplay());

        command.setCommand(new ListCommand());
        Map<String, String> input = new HashMap<String, String>();
        input.put("--xml", "true");
        commandTester.execute(input);
        // assertRegExp("<command/", commandTester.getDisplay(), ".execute() returns an XML help text if --xml is passed");

        Application application = new Application();
        commandTester = new CommandTester(application.get("help"));
        input.clear();
        input.put("command_name", "list");
        commandTester.execute(input);
        // assertRegExp(".execute() returns a text help for the given command", "list [--xml] [--raw] [namespace]", commandTester.getDisplay());

        input.put("--xml", "true");
        commandTester.execute(input);
        // assertRegExp(".execute() returns an XML help text if --xml is passed", "<command", commandTester.getDisplay());
    }
}