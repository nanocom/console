/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.SystemUtils.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.command.Command;
import org.nanocom.console.command.Executable;
import org.nanocom.console.command.HelpCommand;
import org.nanocom.console.exception.LogicException;
import org.nanocom.console.fixtures.Foo1Command;
import org.nanocom.console.fixtures.Foo2Command;
import org.nanocom.console.fixtures.Foo3Command;
import org.nanocom.console.fixtures.FooCommand;
import org.nanocom.console.helper.FormatterHelper;
import org.nanocom.console.helper.Helper;
import org.nanocom.console.helper.HelperSet;
import org.nanocom.console.input.ArgsInput;
import org.nanocom.console.input.ArrayInput;
import org.nanocom.console.input.InputArgument;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputInterface;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.input.InputParameterInterface;
import org.nanocom.console.output.ConsoleOutput;
import org.nanocom.console.output.NullOutput;
import org.nanocom.console.output.Output;
import org.nanocom.console.output.OutputInterface;
import org.nanocom.console.tester.ApplicationTester;

public class ApplicationTest {

    public ApplicationTest() {
    }

    protected String normalizeLineBreaks(String text) {
        return text.replaceAll(LINE_SEPARATOR, "\n");
    }

    /**
     * Replaces the dynamic placeholders of the command help text with a static version.
     * The placeholder %command.full_name% includes the script path that is not predictable
     * and can not be tested against.
     */
    protected void ensureStaticCommandHelp(Application application) {
        for (Command command : application.all().values()) {
            if (null != command.getHelp()) {
                command.setHelp(command.getHelp().replaceAll("%command.full_name%", "console.jar %command.name%"));
            }
        }
    }

    protected String getResource(String file) {
        List<String> contents = new ArrayList<String>();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(file)));
            try {
                String line;
                while (null != (line = input.readLine())) {
                    contents.add(line);
                }
            } finally {
                input.close();
            }

        } catch (IOException ex){
            fail("Unaccessible resource file");
        }

        return join(contents, "\n");
    }

    @Test
    public void testConstructor() {
        Application application = new Application("foo", "bar");
        assertEquals("Constructor takes the application name as its first argument", "foo", application.getName());
        assertEquals("Constructor takes the application version as its first argument", "bar", application.getVersion());
        assertTrue("Constructor registers the help and list commands by default", application.all().containsKey("help") && application.all().containsKey("list"));
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
            assertTrue("find() throws an IllegalArgumentException if the abbreviation is ambiguous for a namespace", e.getMessage().contains("Command \"f\" is not defined."));
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
    public void testFindAlternativeExceptionMessage() {
        Application application = new Application();
        application.add(new FooCommand());

        // Command + singular
        try {
            application.find("foo:baR");
            fail("find() throws an IllegalArgumentException if command does not exist, with one alternative");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with one alternative", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with one alternative", e.getMessage().contains("Did you mean this"));
        }

        // Namespace + singular
        try {
            application.find("foO:bar");
            fail("find() throws an IllegalArgumentException if command does not exist, with one alternative");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with one alternative", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with one alternative", e.getMessage().contains("Did you mean this"));
        }


        application.add(new Foo1Command());
        application.add(new Foo2Command());

        // Command + plural
        try {
            application.find("foo:baR");
            fail("find() throws an IllegalArgumentException if command does not exist, with alternatives");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e.getMessage().contains("Did you mean one of these"));
        }

        // Namespace + plural
        try {
            application.find("foo2:bar");
            fail("find() throws an IllegalArgumentException if command does not exist, with alternatives");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e.getMessage().contains("Did you mean one of these"));
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
            commandName = "Unknown command";
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
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e.getMessage().contains(String.format("Command \"%s\" is not defined.", commandName)));
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative: \"foo:bar\"", e.getMessage().contains("foo:bar"));
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative: \"foo1:bar\"", e.getMessage().contains("foo1:bar"));
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternative: \"foo:bar1\"", e.getMessage().contains("foo:bar1"));
        }

        // Test if "foo1" command throws an "IllegalArgumentException" and does not contain
        // "foo:bar" as alternative because "foo1" is too far from "foo:bar"
        try {
            commandName = "foo1";
            application.find(commandName);
            fail("find() throws an IllegalArgumentException if command does not exist");
        } catch (Exception e) {
            assertTrue("find() throws an IllegalArgumentException if command does not exist", e instanceof IllegalArgumentException);
            assertTrue("find() throws an IllegalArgumentException if command does not exist, with alternatives", e.getMessage().contains(String.format("Command \"%s\" is not defined.", commandName)));
            assertFalse("find() throws an IllegalArgumentException if command does not exist, without \"foo:bar\" alternative", e.getMessage().indexOf("foo:bar") > -1);
        }
    }

    @Test
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

    @Test
    public void testSetCatchExceptions() {
        Application application = new Application() {

            @Override
            protected Integer getTerminalWidth() {
                return 120;
            }
        };
        application.setAutoExit(false);

        ApplicationTester tester = new ApplicationTester(application);
        application.setCatchExceptions(true);
        Map<String, String> input = new HashMap<String, String>();
        input.put("command", "foo");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("decorated", false);
        tester.run(input, options);
        assertEquals("setCatchExceptions() sets the catch exception flag", getResource("application_renderexception1.txt"), normalizeLineBreaks(tester.getDisplay()));

        application.setCatchExceptions(false);
        try {
            tester.run(input, options);
            fail("setCatchExceptions() sets the catch exception flag");
        } catch (Exception e) {
            assertTrue("setCatchExceptions() sets the catch exception flag", e instanceof IllegalArgumentException);
            assertEquals("setCatchExceptions() sets the catch exception flag", "Command \"foo\" is not defined.", e.getMessage());
        }
    }

    @Test
    public void testAsText() {
        Application application = new Application();
        application.add(new FooCommand());
        ensureStaticCommandHelp(application);
        assertEquals("asText() returns a text representation of the application", getResource("application_astext1.txt"), normalizeLineBreaks(application.asText()));
        assertEquals("asText() returns a text representation of the application", getResource("application_astext2.txt"), normalizeLineBreaks(application.asText("foo")));
    }

    @Test
    public void testRenderException() {
        Application application = new Application() {

            @Override
            protected Integer getTerminalWidth() {
                return 120;
            }
        };
        application.setAutoExit(false);

        ApplicationTester tester = new ApplicationTester(application);
        Map<String, String> input = new HashMap<String, String>();
        input.put("command", "foo");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("decorated", false);
        tester.run(input, options);
        assertEquals("renderException() renders a pretty exception", getResource("application_renderexception1.txt"), normalizeLineBreaks(tester.getDisplay()));

        options.put("verbosity", Output.VerbosityLevel.VERBOSE);
        tester.run(input, options);
        assertTrue("renderException() renders a pretty exception with a stack trace when verbosity is verbose", tester.getDisplay().contains("Exception trace"));

        input.put("command", "list");
        input.put("--foo", null);
        options.remove("verbosity");
        tester.run(input, options);
        assertEquals("renderException() renders the command synopsis when an exception occurs in the context of a command", getResource("application_renderexception2.txt"), normalizeLineBreaks(tester.getDisplay()));

        application.add(new Foo3Command());
        tester = new ApplicationTester(application);
        input.clear();
        input.put("command", "foo3:bar");
        tester.run(input, options);
        assertEquals("renderException() renders a pretty exception with previous exceptions", getResource("application_renderexception3.txt"), normalizeLineBreaks(tester.getDisplay()));

        application = new Application() {

            @Override
            protected Integer getTerminalWidth() {
                return 32;
            }
        };
        application.setAutoExit(false);

        tester = new ApplicationTester(application);

        input.clear();
        input.put("command", "foo");
        tester.run(input, options);
        assertEquals("renderException() wraps messages when they are bigger than the terminal", getResource("application_renderexception4.txt"), normalizeLineBreaks(tester.getDisplay()));
    }

    @Test
    public void testRun() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        Foo1Command command = new Foo1Command();
        application.add(command);
        application.run(new ArgsInput(new String[]{"foo:bar1"}));

        assertEquals("run() creates a ConsoleOutput by default if none is given", ConsoleOutput.class, command.output.getClass());

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        ensureStaticCommandHelp(application);
        ApplicationTester tester = new ApplicationTester(application);

        Map<String, Object> options = new HashMap<String, Object>();
        Map<String, String> input = new HashMap<String, String>();
        options.put("decorated", false);
        tester.run(input, options);
        assertEquals("run() runs the list command if no argument is passed", getResource("application_run1.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.put("--help", null);
        tester.run(input, options);
        assertEquals("run() runs the help command if --help is passed", getResource("application_run2.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.clear();
        input.put("-h", null);
        tester.run(input, options);
        assertEquals("run() runs the help command if -h is passed", getResource("application_run2.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.clear();
        input.put("command", "list");
        input.put("--help", null);
        tester.run(input, options);
        assertEquals("run() displays the help if --help is passed", getResource("application_run3.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.remove("--help");
        input.put("-h", null);
        tester.run(input, options);
        assertEquals("run() displays the help if -h is passed", getResource("application_run3.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.clear();
        input.put("--ansi", null);
        tester.run(input, new HashMap<String, Object>());
        assertTrue("run() forces color output if --ansi is passed", tester.getOutput().isDecorated());

        input.clear();
        input.put("--no-ansi", null);
        tester.run(input, new HashMap<String, Object>());
        assertFalse("run() forces color output to be disabled if --no-ansi is passed", tester.getOutput().isDecorated());

        input.clear();
        input.put("--version", null);
        tester.run(input, options);
        assertEquals("run() displays the program version if --version is passed", getResource("application_run4.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.clear();
        input.put("-V", null);
        tester.run(input, options);
        assertEquals("run() displays the program version if -V is passed", getResource("application_run4.txt"), normalizeLineBreaks(tester.getDisplay()));

        input.clear();
        input.put("command", "list");
        input.put("--quiet", null);
        tester.run(input, new HashMap<String, Object>());
        assertEquals("run() removes all output if --quiet is passed", EMPTY, tester.getDisplay());

        input.remove("--quiet");
        input.put("-q", null);
        tester.run(input, new HashMap<String, Object>());
        assertEquals("run() removes all output if -q is passed", EMPTY, tester.getDisplay());

        input.remove("-q");
        input.put("--verbose", null);
        tester.run(input, new HashMap<String, Object>());
        assertSame("run() sets the output to verbose if --verbose is passed", Output.VerbosityLevel.VERBOSE, tester.getOutput().getVerbosity());

        input.remove("--verbose");
        input.put("-v", null);
        tester.run(input, new HashMap<String, Object>());
        assertSame("run() sets the output to verbose if -v is passed", Output.VerbosityLevel.VERBOSE, tester.getOutput().getVerbosity());

        application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application.add(new FooCommand());
        tester = new ApplicationTester(application);

        input.clear();
        input.put("command", "foo:bar");
        input.put("--no-interaction", null);
        tester.run(input, options);
        assertEquals("run() does not call interact() if --no-interaction is passed", "called" + LINE_SEPARATOR, tester.getDisplay());

        input.remove("--no-interaction");
        input.put("-n", null);
        tester.run(input, options);
        assertEquals("run() does not call interact() if -n is passed", "called" + LINE_SEPARATOR, tester.getDisplay());
    }

    @Test(expected=LogicException.class)
    public void testAddingAlreadySetDefinitionElementData() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);
        application
            .register("foo")
            .setDefinition(getAddingAlreadySetDefinitionElementData())
            .setCode(new Executable() {

                @Override
                protected int execute(InputInterface input, OutputInterface output) {
                    return 0;
                }
            })
        ;

        Map<String, String> params = new HashMap<String, String>();
        params.put("command", "foo");
        InputInterface input = new ArrayInput(params);
        OutputInterface output = new NullOutput();
        application.run(input, output);
    }

    public InputParameterInterface[] getAddingAlreadySetDefinitionElementData() {
        return new InputParameterInterface[] {
            new InputArgument("command", InputArgument.REQUIRED),
            new InputOption("quiet", "", InputOption.VALUE_NONE),
            new InputOption("query", "q", InputOption.VALUE_NONE)
        };
    }

    @Test
    public void testGetDefaultHelperSetReturnsDefaultValues() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        HelperSet helperSet = application.getHelperSet();

        assertTrue(helperSet.has("formatter"));
        assertTrue(helperSet.has("dialog"));
        // assertTrue(helperSet.has("progress"));
    }

    @Test
    public void testAddingSingleHelperSetOverwritesDefaultValues() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        application.setHelperSet(new HelperSet(Arrays.<Helper>asList(new FormatterHelper())));

        HelperSet helperSet = application.getHelperSet();

        assertTrue(helperSet.has("formatter"));

        // No other default helper set should be returned
        assertFalse(helperSet.has("dialog"));
        // assertFalse(helperSet.has("progress"));
    }

    @Test
    public void testOverwritingDefaultHelperSetOverwritesDefaultValues() {
        Application application = new CustomApplication();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        application.setHelperSet(new HelperSet(Arrays.<Helper>asList(new FormatterHelper())));

        HelperSet helperSet = application.getHelperSet();

        assertTrue(helperSet.has("formatter"));

        // No other default helper set should be returned
        assertFalse(helperSet.has("dialog"));
        assertFalse(helperSet.has("progress"));
    }

    @Test
    public void testGetDefaultInputDefinitionReturnsDefaultValues() {
        Application application = new Application();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        InputDefinition inputDefinition = application.getDefinition();

        assertTrue(inputDefinition.hasArgument("command"));

        assertTrue(inputDefinition.hasOption("help"));
        assertTrue(inputDefinition.hasOption("quiet"));
        assertTrue(inputDefinition.hasOption("verbose"));
        assertTrue(inputDefinition.hasOption("version"));
        assertTrue(inputDefinition.hasOption("ansi"));
        assertTrue(inputDefinition.hasOption("no-ansi"));
        assertTrue(inputDefinition.hasOption("no-interaction"));
    }

    @Test
    public void testOverwritingDefaultInputDefinitionOverwritesDefaultValues() {
        Application application = new CustomApplication();
        application.setAutoExit(false);
        application.setCatchExceptions(false);

        InputDefinition inputDefinition = application.getDefinition();

        // Check wether the default arguments and options are not returned anymore
        assertFalse(inputDefinition.hasArgument("command"));

        assertFalse(inputDefinition.hasOption("help"));
        assertFalse(inputDefinition.hasOption("quiet"));
        assertFalse(inputDefinition.hasOption("verbose"));
        assertFalse(inputDefinition.hasOption("version"));
        assertFalse(inputDefinition.hasOption("ansi"));
        assertFalse(inputDefinition.hasOption("no-ansi"));
        assertFalse(inputDefinition.hasOption("no-interaction"));

        assertTrue(inputDefinition.hasOption("custom"));
    }

    class CustomApplication extends Application {

        /**
         * Overwrites the default input definition.
         *
         * @return InputDefinition An InputDefinition instance
         */
        @Override
        protected InputDefinition getDefaultInputDefinition() {
            return new InputDefinition(new InputParameterInterface[] {new InputOption("--custom", "-c", InputOption.VALUE_NONE, "Set the custom input definition.")});
        }

        /**
         * Gets the default helper set with the helpers that should always be available.
         *
         * @return HelperSet A HelperSet instance
         */
        @Override
        protected HelperSet getDefaultHelperSet() {
            return new HelperSet(Arrays.<Helper>asList(new FormatterHelper()));
        }
    }
}
