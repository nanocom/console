/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.tester.CommandTester;

public class ListCommandTest {

    @Test
    public void testExecute() {
        Application application = new Application();

        Command command = application.get("list");
        CommandTester commandTester = new CommandTester(command);
        Map<String, String> input = new LinkedHashMap<String, String>();
        input.put("command", command.getName());
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("decorated", false);
        commandTester.execute(input, options);
        assertTrue("execute() returns a list of available commands", commandTester.getDisplay().contains("help   Displays help for a command"));

        input.put("--raw", null);
        commandTester.execute(input, options);
        String output = String.format("help   Displays help for a command%slist   Lists commands%s", LINE_SEPARATOR, LINE_SEPARATOR);
        assertEquals("boo", output, commandTester.getDisplay());
    }
}
