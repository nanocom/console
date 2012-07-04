package org.nanocom.console.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.nanocom.console.Application;
import org.nanocom.console.fixtures.TestCommand;
import org.nanocom.console.helper.FormatterHelper;
import org.nanocom.console.helper.HelperInterface;
import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.input.StringInput;
import org.nanocom.console.output.NullOutput;
import org.nanocom.console.output.OutputInterface;
import org.nanocom.console.tester.CommandTester;

public class CommandTest {

    @Test
    public void testConstructor() throws Exception {
        Command command;
        try {
            command = new Command();
            fail("__construct() throws an Exception if the name is null");
        } catch (Exception e) {
            //assertInstanceOf("\LogicException", e, "__construct() throws a \LogicException if the name is null");
            assertEquals("__construct() throws an Exception if the name is null", "The command name cannot be empty.", e.getMessage());
        }
        command = new Command("foo:bar");
        assertEquals("__construct() takes the command name as its first argument", "foo:bar", command.getName());
    }

    @Test
    public void testSetApplication() throws Exception {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        assertEquals(".setApplication() sets the current application", application, command.getApplication());
    }

    @Test
    public void testSetGetDefinition() throws Exception {
        Command command = new TestCommand();
        InputDefinition definition = new InputDefinition();
        Command ret = command.setDefinition(definition);
        assertEquals(".setDefinition() implements a fluent interface", command, ret);
        assertEquals(".setDefinition() sets the current InputDefinition instance", definition, command.getDefinition());
        command.setDefinition(Arrays.asList((Object) new InputArgument("foo"), new InputOption("bar")));
        assertTrue(".setDefinition() also takes an array of InputArguments and InputOptions as an argument", command.getDefinition().hasArgument("foo"));
        assertTrue(".setDefinition() also takes an array of InputArguments and InputOptions as an argument", command.getDefinition().hasOption("bar"));
        command.setDefinition(new InputDefinition());
    }

    @Test
    public void testAddArgument() throws Exception {
        Command command = new TestCommand();
        command.addArgument("foo");
        assertTrue(".addArgument() adds an argument to the command", command.getDefinition().hasArgument("foo"));
    }

    @Test
    public void testAddOption() throws Exception {
        Command command = new TestCommand();
        command.addOption("foo");
        assertTrue(".addOption() adds an option to the command", command.getDefinition().hasOption("foo"));
    }

    @Test
    public void testGetNamespaceGetNameSetName() throws Exception {
        Command command = new TestCommand();
        assertEquals(".getName() returns the command name", "namespace:name", command.getName());
        command.setName("foo");
        assertEquals(".setName() sets the command name", "foo", command.getName());

        command.setName("foobar:bar");
        assertEquals(".setName() sets the command name", "foobar:bar", command.getName());

        try {
            command.setName("");
            fail(".setName() throws an Exception if the name is empty");
        } catch (Exception e) {
            // assertInstanceOf("\InvalidArgumentException", e, ".setName() throws an \InvalidArgumentException if the name is empty");
            assertEquals(".setName() throws an Exception if the name is empty", "Command name \"\" is invalid.", e.getMessage());
        }

        try {
            command.setName("foo:");
            fail(".setName() throws an Exception if the name is empty");
        } catch (Exception e) {
            // assertInstanceOf("\InvalidArgumentException", e, ".setName() throws an \InvalidArgumentException if the name is empty");
            assertEquals(".setName() throws an Exception if the name is empty", "Command name \"foo:\" is invalid.", e.getMessage());
        }
    }

    @Test
    public void testGetSetDescription() throws Exception {
        Command command = new TestCommand();
        assertEquals(".getDescription() returns the description", "description", command.getDescription());
        command.setDescription("description1");
        assertEquals(".setDescription() sets the description", "description1", command.getDescription());
    }

    @Test
    public void testGetSetHelp() throws Exception {
        Command command = new TestCommand();
        assertEquals(".getHelp() returns the help", "help", command.getHelp());
        command.setHelp("help1");
        assertEquals(".setHelp() sets the help", "help1", command.getHelp());
    }

    @Test
    public void testGetProcessedHelp() throws Exception {
        Command command = new TestCommand();
        command.setHelp("The %command.name% command does... Example: php %command.full_name%.");
        assertTrue(".getProcessedHelp() replaces %command.name% correctly", command.getProcessedHelp().contains("The namespace:name command does..."));
        assertFalse(".getProcessedHelp() replaces %command.full_name%", command.getProcessedHelp().matches("%command.full_name%"));
    }

    @Test
    public void testGetSetAliases() throws Exception {
        Command command = new TestCommand();
        assertEquals(".getAliases() returns the aliases", Arrays.asList("name"), command.getAliases());
        command.setAliases(Arrays.asList("name1"));
        assertEquals(".setAliases() sets the aliases", Arrays.asList("name1"), command.getAliases());
    }

    @Test
    public void testGetSynopsis() throws Exception {
        Command command = new TestCommand();
        command.addOption("foo");
        command.addArgument("foo");
        assertEquals(".getSynopsis() returns the synopsis", "namespace:name [--foo] [foo]", command.getSynopsis());
    }

    @Test
    public void testGetHelper() throws Exception {
        Application application = new Application();
        TestCommand command = new TestCommand();
        command.setApplication(application);
        FormatterHelper formatterHelper = new FormatterHelper();
        assertEquals(".getHelper() returns the correct helper", formatterHelper.getName(), ((HelperInterface) command.getHelper("formatter")).getName());
    }

    @Test
    public void testGet() throws Exception {
        Application application = new Application();
        Command command = new TestCommand();
        command.setApplication(application);
        FormatterHelper formatterHelper = new FormatterHelper();
        assertEquals(".__get() returns the correct helper", formatterHelper.getName(), ((HelperInterface) command.getHelper("formatter")).getName());
    }

    @Test
    public void testMergeApplicationDefinition() throws Exception {
        Application application1 = new Application();
        application1.getDefinition().addArguments(Arrays.asList(new InputArgument("foo")));
        application1.getDefinition().addOptions(Arrays.asList(new InputOption("bar")));
        Command command = new TestCommand();
        command.setApplication(application1);
        InputDefinition definition = new InputDefinition(Arrays.asList((Object) new InputArgument("bar"), new InputOption("foo")));
        command.setDefinition(definition);

        Class<Command> cls = Command.class;
        Method m = cls.getDeclaredMethod("mergeApplicationDefinition");
        m.setAccessible(true);
        m.invoke(command);

        assertTrue(".mergeApplicationDefinition() merges the application arguments and the command arguments", command.getDefinition().hasArgument("foo"));
        assertTrue(".mergeApplicationDefinition() merges the application arguments and the command arguments", command.getDefinition().hasArgument("bar"));
        assertTrue(".mergeApplicationDefinition() merges the application options and the command options", command.getDefinition().hasOption("foo"));
        assertTrue(".mergeApplicationDefinition() merges the application options and the command options", command.getDefinition().hasOption("bar"));

        m.invoke(command);
        assertEquals(".mergeApplicationDefinition() does not try to merge twice the application arguments and options", 3, command.getDefinition().getArgumentCount());
    }

    @Test
    public void testRun() throws Exception {
        Command command = new TestCommand();
        CommandTester tester = new CommandTester(command);
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("--bar", "true");

        try {
            tester.execute(foobar);
            fail(".run() throws a Exception when the input does not validate the current InputDefinition");
        } catch (Exception e) {
            // assertInstanceOf("\InvalidArgumentException", e, ".run() throws a \InvalidArgumentException when the input does not validate the current InputDefinition");
            assertEquals(".run() throws an Exception when the input does not validate the current InputDefinition", "The \"--bar\" option does not exist.", e.getMessage());
        }

        Map<String, Object> foobar2 = new HashMap<String, Object>();
        foobar2.put("interactive", true);
        tester.execute(new HashMap<String, String>(), foobar2);
        // TODO assertEquals(".run() calls the interact() method if the input is interactive", "interact called" + System.getProperty("line.separator") + "execute called" + System.getProperty("line.separator"), tester.getDisplay());

        foobar2.clear();
        foobar2.put("interactive", false);
        tester.execute(new HashMap<String, String>(), foobar2);
        // TODO assertEquals(".run() does not call the interact() method if the input is not interactive", "execute called" + System.getProperty("line.separator"), tester.getDisplay());

        command = new Command("foo");
        try {
            command.run(new StringInput(""), new NullOutput());
            fail(".run() throws an Exception if the execute() method has not been overridden and no code has been provided");
        } catch (Exception e) {
            // assertInstanceOf("\LogicException", e, ".run() throws a \LogicException if the execute() method has not been overridden and no code has been provided");
            // TODO assertEquals(".run() throws an Exception if the execute() method has not been overridden and no code has been provided", "You must override the execute() method in the concrete command class.", e.getMessage());
        }
    }

    @Test
    public void testSetCode() throws Exception {
        Command command = new TestCommand();
        Command ret = command.setCode(new Executable() {
			@Override
			protected int execute(InputInterface input, OutputInterface output) {
				output.writeln("from the code...");

				return 0;
			}
		});
        assertEquals(".setCode() implements a fluent interface", command, ret);
        CommandTester tester = new CommandTester(command);
        tester.execute(new HashMap<String, String>());
        // assertEquals("interact called" + System.getProperty("line.separator") + "from the code..." + System.getProperty("line.separator"), tester.getDisplay());
    }

    /*@Test
    public void testAsText() {
        Command command = new TestCommand();
        command.setApplication(new Application());
        CommandTester tester = new CommandTester(command);
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("command", command.getName());
        tester.execute(foobar);
        // assertStringEqualsFile(self.fixturesPath."/command_astext.txt", command.asText(), ".asText() returns a text representation of the command");
    }*/

    /*@Test
    public void testAsXml() {
        Command command = new TestCommand();
        command.setApplication(new Application());
        tester = new CommandTester(command);
        tester.execute(array("command" => command.getName()));
        assertXmlStringEqualsXmlFile(fixturesPath + "/command_asxml.txt", command.asXml(), ".asXml() returns an XML representation of the command");
    }*/
}
