/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.command.Command;
import org.nanocom.console.command.HelpCommand;
import org.nanocom.console.fixtures.Foo1Command;
import org.nanocom.console.fixtures.Foo2Command;
import org.nanocom.console.fixtures.Foo3Command;
import org.nanocom.console.fixtures.FooCommand;
import org.nanocom.console.input.ArgvInput;
import org.nanocom.console.output.ConsoleOutput;
import org.nanocom.console.tester.ApplicationTester;

public class ApplicationTest {

    public ApplicationTest() {
    }

    protected String normalizeLineBreaks(String text) {
        return text.replaceAll(System.getProperty("line.separator"), "\n");
    }

    /**
     * Replaces the dynamic placeholders of the command help text with a static version.
     * The placeholder %command.full_name% includes the script path that is not predictable
     * and can not be tested against.
     */
    protected void ensureStaticCommandHelp(Application application) {
        for (Command command : application.all().values()) {
            if (null != command.getHelp()) {
                command.setHelp(command.getHelp().replaceAll("%command.full_name%", "app/console %command.name%"));
            }
        }
    }

    @Test
    public void testConstructor() {
        Application application = new Application("foo", "bar");
        assertEquals("Application() takes the application name as its first argument", "foo", application.getName());
        assertEquals("Application() takes the application version as its first argument", "bar", application.getVersion());
        assertTrue("Application() registered the help and list commands by default", application.all().containsKey("help") && application.all().containsKey("list"));
    }

    @Test
    public void testSetGetName() {
        Application application = new Application();
        application.setName("foo");
        assertEquals("setName() sets the name of the application", "foo", application.getName());
    }

    @Test
    public void testSetGetVersion() {
        Application application = new Application();
        application.setVersion("bar");
        assertEquals("setVersion() sets the version of the application", "bar", application.getVersion());
    }

    @Test
    public void testGetLongVersion() {
        Application application = new Application("foo", "bar");
        assertEquals("getLongVersion() returns the long version of the application", "<info>foo</info> version <comment>bar</comment>", application.getLongVersion());
    }

    @Test
    public void testHelp() {
        Application application = new Application();
        assertEquals("setHelp() returns a help message", getResource("application_gethelp.txt"), normalizeLineBreaks(application.getHelp()));
    }

    @Test
    public void testAll() {
        Application application = new Application();
        Map<String, Command> commands = application.all();
        assertEquals("all() returns the registered commands", HelpCommand.class, commands.get("help").getClass());

        application.add(new FooCommand());
        commands = application.all("foo");
        assertEquals("all() takes a namespace as its first argument", 1, commands.size());
    }

    @Test
    public void testRegister() {
        Application application = new Application();
        Command command = application.register("foo");
        assertEquals("register() registers a new command", "foo", command.getName());
    }

    @Test
    public void testAdd() {
        Application application = new Application();
        Command foo = new FooCommand();
        application.add(foo);
        Map<String, Command> commands = application.all();
        assertEquals("add() registers a command", foo, commands.get("foo:bar"));

        application = new Application();
        foo = new FooCommand();
        Command foo1 = new Foo1Command();
        application.addCommands(Arrays.asList(foo, foo1));
        commands = application.all();
        assertEquals("addCommands() registers an array of commands", Arrays.asList(foo, foo1), Arrays.asList(commands.get("foo:bar"), commands.get("foo:bar1")));
    }

    @Test
    public void testHasGet() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Application application = new Application();
        assertTrue("has() returns true if a named command is registered", application.has("list"));
        assertFalse("has() returns false if a named command is not registered", application.has("afoobar"));

        FooCommand foo = new FooCommand();
        application.add(foo);
        assertTrue("has() returns true if an alias is registered", application.has("afoobar"));
        assertEquals("get() returns a command by name", foo, application.get("foo:bar"));
        assertEquals("get() returns a command by alias", foo, application.get("afoobar"));

        try {
            application.get("foofoo");
            fail("get() throws an IllegalArgumentException if the command does not exist");
        } catch (Exception e) {
            assertTrue("get() throws an IllegalArgumentException if the command does not exist", e instanceof IllegalArgumentException);
            assertEquals("get() throws an IllegalArgumentException if the command does not exist", "The command \"foofoo\" does not exist.", e.getMessage());
        }

        application = new Application();
        foo = new FooCommand();
        application.add(foo);
        // simulate --help
        Class<Application> cls = Application.class;
        Field f = cls.getDeclaredField("wantHelps");
        f.setAccessible(true);
        f.set(application, true);
        Command command = application.get("foo:bar");
        assertEquals("get() returns the help command if --help is provided as the input", HelpCommand.class, command.getClass());
    }

    @Test
    public void testGetNamespaces() {
        Application application = new Application();
        application.add(new FooCommand());
        application.add(new Foo1Command());
        Set<String> foo = new HashSet<String>(1);
        foo.add("foo");
        assertEquals("getNamespaces() returns an array of unique used namespaces", foo, application.getNamespaces());
    }

    @Test
    public void testFindNamespace() {
        Application application = new Application();
        application.add(new FooCommand());
        assertEquals("findNamespace() returns the given namespace if it exists", "foo", application.findNamespace("foo"));
        assertEquals("findNamespace() finds a namespace given an abbreviation", "foo", application.findNamespace("f"));
        application.add(new Foo2Command());
        assertEquals("findNamespace() returns the given namespace if it exists", "foo", application.findNamespace("foo"));

        try {
            application.findNamespace("f");
            fail("findNamespace() throws an IllegalArgumentException if the abbreviation is ambiguous");
        } catch (Exception e) {
            assertTrue("findNamespace() throws an IllegalArgumentException if the abbreviation is ambiguous", e instanceof IllegalArgumentException);
            assertEquals("findNamespace() throws an IllegalArgumentException if the abbreviation is ambiguous", "The namespace \"f\" is ambiguous (foo, foo1).", e.getMessage());
        }

        try {
            application.findNamespace("bar");
            fail("findNamespace() throws an IllegalArgumentException if no command is in the given namespace");
        } catch (Exception e) {
            assertTrue("findNamespace() throws an IllegalArgumentException if no command is in the given namespace", e instanceof IllegalArgumentException);
            assertEquals("findNamespace() throws an IllegalArgumentException if no command is in the given namespace", "There are no commands defined in the \"bar\" namespace.", e.getMessage());
        }
    }

    @Test
    public void testFind() {
        Application application = new Application();
        application.add(new FooCommand());
        assertEquals("find() returns a command if its name exists", FooCommand.class, application.find("foo:bar").getClass());
        assertEquals("find() returns a command if its name exists", HelpCommand.class, application.find("h").getClass());
        assertEquals("find() returns a command if the abbreviation for the namespace exists", FooCommand.class, application.find("f:bar").getClass());
        assertEquals("find() returns a command if the abbreviation for the namespace and the command name exist", FooCommand.class, application.find("f:b").getClass());
        assertEquals("find() returns a command if the abbreviation exists for an alias", FooCommand.class, application.find("a").getClass());

        application.add(new Foo1Command());
        application.add(new Foo2Command());

        try {
            application.find("f");
            fail("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a namespace");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a namespace", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a namespace", "Command \"f\" is not defined.", e.getMessage());
        }

        try {
            application.find("a");
            fail("find() throws an IllegalArgumentException if the abbreviation is ambiguous for an alias");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if the abbreviation is ambiguous for an alias", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if the abbreviation is ambiguous for an alias", "Command \"a\" is ambiguous (afoobar, afoobar1 and 1 more).", e.getMessage());
        }

        try {
            application.find("foo:b");
            fail("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a command");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a command", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a command", "Command \"foo:b\" is ambiguous (foo:bar, foo:bar1).", e.getMessage());
        }
    }

    @Test
    public void testFindAlternativeCommands() {
        Application application = new Application();

        application.add(new FooCommand());
        application.add(new Foo1Command());
        application.add(new Foo2Command());
        String commandName = null;

        try {
            commandName = "Unknow command";
            application.find(commandName);
            fail("find() throws an IllegalArgumentException if command does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if command does not exist, without alternatives", String.format("Command \"%s\" is not defined.", commandName), e.getMessage());
        }

        try {
            commandName = "foo";
            application.find(commandName);
            fail("find() throws an IllegalArgumentException if command does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if command does not exist, with alternatives", String.format("Command \"%s\" is not defined.", commandName), e.getMessage());
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative : \"foo:bar\"", e.getMessage().contains("foo:bar"));
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative : \"foo1:bar\"", e.getMessage().contains("foo1:bar"));
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative : \"foo:bar1\"", e.getMessage().contains("foo:bar1"));
        }

        // Test if "foo1" command throws an "IllegalArgumentException" and does not contain
        // "foo:bar" as alternative because "foo1" is too far from "foo:bar"
        try {
            commandName = "foo1";
            application.find(commandName);
            fail("find() throws an IllegalArgumentException if command does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if command does not exist, with alternatives", String.format("Command \"%s\" is not defined.", commandName), e.getMessage());
            assertFalse("find() throws an IllegalArgumentException if command does not exist, without \"foo:bar\" alternative", e.getMessage().indexOf("foo:bar") > -1);
        }
    }

    public void testFindAlternativeNamespace() {
        Application application = new Application();

        application.add(new FooCommand());
        application.add(new Foo1Command());
        application.add(new Foo2Command());
        application.add(new Foo3Command());

        try {
            application.find("Unknow-namespace:Unknow-command");
            fail("find() throws an IllegalArgumentException if namespace does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist", e instanceof IllegalArgumentException);
            assertEquals("find() throws an IllegalArgumentException if namespace does not exist, without alternatives", "There are no commands defined in the \"Unknow-namespace\" namespace.", e.getMessage());
        }

        try {
            application.find("foo2:command");
            fail("find() throws an IllegalArgumentException if namespace does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist, with alternative", e.getMessage().contains("There are no commands defined in the \"foo2\" namespace."));
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist, with alternative : \"foo\"", e.getMessage().contains("foo"));
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist, with alternative : \"foo1\"", e.getMessage().contains("foo1"));
            assertTrue("find() throws an IllegalArgumentException if namespace does not exist, with alternative : \"foo3\"", e.getMessage().contains("foo3"));
        }
    }

    /*public void testSetCatchExceptions() {
        Application application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(120));
        tester = new ApplicationTester(application);

        application.setCatchExceptions(true);
        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".setCatchExceptions() sets the catch exception flag");

        application.setCatchExceptions(false);
        try {
            tester.run(array("command" => "foo"), array("decorated" => false));
            fail(".setCatchExceptions() sets the catch exception flag");
        } catch (Exception e) {
            assertInstanceOf("\Exception", e, ".setCatchExceptions() sets the catch exception flag");
            assertEquals("Command \"foo\" is not defined.", e.getMessage(), ".setCatchExceptions() sets the catch exception flag");
        }
    }*/

    public String getResource(String file) {
        StringBuilder contents = new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            getClass().getClassLoader().getResourceAsStream(file)));
            try {
                String line;
                while (null != (line = input.readLine())) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }

            contents.deleteCharAt(contents.lastIndexOf(System.getProperty("line.separator")));
        } catch (IOException ex){
            ex.printStackTrace();
            fail();
        }

        return contents.toString();
    }

    @Test
    public void testAsText() {
        Application application = new Application();
        application.add(new FooCommand());
        ensureStaticCommandHelp(application);
        assertEquals("asText() returns a text representation of the application", getResource("application_astext1.txt"), normalizeLineBreaks(application.asText()));
        assertEquals("asText() returns a text representation of the application", getResource("application_astext2.txt"), normalizeLineBreaks(application.asText("foo")));
    }

    /*public void testAsXml() {
        Application application = new Application();
        application.add(new FooCommand());
        this.ensureStaticCommandHelp(application);
        assertXmlStringEqualsXmlFile(self.fixturesPath."/application_asxml1.txt", application.asXml(), ".asXml() returns an XML representation of the application");
        assertXmlStringEqualsXmlFile(self.fixturesPath."/application_asxml2.txt", application.asXml("foo"), ".asXml() returns an XML representation of the application");
    }*/

    /*public void testRenderException() {
        Application application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(120));
        tester = new ApplicationTester(application);

        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders a pretty exception");

        tester.run(array("command" => "foo"), array("decorated" => false, "verbosity" => Output.VERBOSITY_VERBOSE));
        assertContains("Exception trace", tester.getDisplay(), ".renderException() renders a pretty exception with a stack trace when verbosity is verbose");

        tester.run(array("command" => "list", "--foo" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders the command synopsis when an exception occurs in the context of a command");

        application.add(new \Foo3Command);
        tester = new ApplicationTester(application);
        tester.run(array("command" => "foo3:bar"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() renders a pretty exceptions with previous exceptions");

        application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(32));
        tester = new ApplicationTester(application);

        application = this.getMock("Symfony\Component\Console\Application", array("getTerminalWidth"));
        application.setAutoExit(false);
        application.expects(this.any())
            .method("getTerminalWidth")
            .will(this.returnValue(32));
        tester = new ApplicationTester(application);

        tester.run(array("command" => "foo"), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_renderexception4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".renderException() wraps messages when they are bigger than the terminal");
    }*/

    @Test
    public void testRun() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        Foo1Command command = new Foo1Command();
        application.add(command);
        application.run(new ArgvInput(new String[]{"foo:bar1"}));

        assertEquals("run() creates a ConsoleOutput by default if none is given", ConsoleOutput.class, command.output.getClass());

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        ensureStaticCommandHelp(application);
        /*ApplicationTester tester = new ApplicationTester(application);

        tester.run(new ArgvInput(), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run1.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the list command if no argument is passed");

        tester.run(array("--help" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the help command if --help is passed");

        tester.run(array("-h" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run2.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() runs the help command if -h is passed");

        tester.run(array("command" => "list", "--help" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the help if --help is passed");

        tester.run(array("command" => "list", "-h" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run3.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the help if -h is passed");

        tester.run(array("--ansi" => true));
        assertTrue(tester.getOutput().isDecorated(), ".run() forces color output if --ansi is passed");

        tester.run(array("--no-ansi" => true));
        assertFalse(tester.getOutput().isDecorated(), ".run() forces color output to be disabled if --no-ansi is passed");

        tester.run(array("--version" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the program version if --version is passed");

        tester.run(array("-V" => true), array("decorated" => false));
        assertStringEqualsFile(self.fixturesPath."/application_run4.txt", this.normalizeLineBreaks(tester.getDisplay()), ".run() displays the program version if -v is passed");

        tester.run(array("command" => "list", "--quiet" => true));
        assertSame("", tester.getDisplay(), ".run() removes all output if --quiet is passed");

        tester.run(array("command" => "list", "-q" => true));
        assertSame("", tester.getDisplay(), ".run() removes all output if -q is passed");

        tester.run(array("command" => "list", "--verbose" => true));
        assertSame(Output.VERBOSITY_VERBOSE, tester.getOutput().getVerbosity(), ".run() sets the output to verbose if --verbose is passed");

        tester.run(array("command" => "list", "-v" => true));
        assertSame(Output.VERBOSITY_VERBOSE, tester.getOutput().getVerbosity(), ".run() sets the output to verbose if -v is passed");*/

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application.add(new FooCommand());
        /*tester = new ApplicationTester(application);

        tester.run(array("command" => "foo:bar", "--no-interaction" => true), array("decorated" => false));
        assertSame("called".PHP_EOL, tester.getDisplay(), ".run() does not call interact() if --no-interaction is passed");

        tester.run(array("command" => "foo:bar", "-n" => true), array("decorated" => false));
        assertSame("called".PHP_EOL, tester.getDisplay(), ".run() does not call interact() if -n is passed");*/
    }

    /**
     * @expectedException LogicException
     * @dataProvider getAddingAlreadySetDefinitionElementData
     */
    /*public void testAddingAlreadySetDefinitionElementData(def) {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application
            .register("foo")
            .setDefinition(array(def))
            .setCode(void (InputInterface input, OutputInterface output) {})
        ;

        input = new ArrayInput(array("command" => "foo"));
        output = new NullOutput();
        application.run(input, output);
    }

    public List<Object> getAddingAlreadySetDefinitionElementData() {
        return Arrays.asList(
            array(new InputArgument("command", InputArgument.REQUIRED)),
            array(new InputOption("quiet", "", InputOption.VALUE_NONE)),
            array(new InputOption("query", "q", InputOption.VALUE_NONE)),
        );
    }*/
}
