/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.exception.LogicException;

public class InputDefinitionTest {

    private InputParameterInterface foo;
    private InputParameterInterface bar;
    private Object foo1;
    private Object foo2;

    public InputDefinitionTest() {
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
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        assertEquals("Constructor creates a new InputDefinition object", new HashMap<String, InputArgument>(), definition.getArguments());

        definition = new InputDefinition(new InputParameterInterface[] {foo, bar});
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        foobar.put("bar", bar);
        assertEquals("Constructor takes an array of InputArgument objects as its first argument", foobar, definition.getArguments());

        initializeOptions();

        foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        foobar.put("bar", bar);

        definition = new InputDefinition();
        assertEquals("Constructor creates a new InputDefinition object", new HashMap<String, InputOption>(), definition.getOptions());

        definition = new InputDefinition(new InputParameterInterface[] {foo, bar});
        assertEquals("Constructor takes an array of InputOption objects as its first argument", foobar, definition.getOptions());
    }

    @Test
    public void testSetArguments() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.setArguments(Arrays.asList((InputArgument) foo));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        assertEquals("setArguments() sets the array of InputArgument objects", foobar, definition.getArguments());
        definition.setArguments(Arrays.asList((InputArgument) bar));
        foobar.clear();
        foobar.put("bar", bar);
        assertEquals("setArguments() clears all InputArgument objects", foobar, definition.getArguments());
    }

    @Test
    public void testAddArguments() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        assertEquals("addArguments() adds an array of InputArgument objects", foobar, definition.getArguments());
        definition.addArguments(Arrays.asList((InputArgument) bar));
        foobar.put("bar", bar);
        assertEquals("addArguments() does not clear existing InputArgument objects", foobar, definition.getArguments());
    }

    @Test
    public void testAddArgument() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo);
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        assertEquals("addArgument() adds a InputArgument object", foobar, definition.getArguments());
        definition.addArgument((InputArgument) bar);
        foobar.put("bar", bar);
        assertEquals("addArgument() adds a InputArgument object", foobar, definition.getArguments());

        // arguments must have different names
        try {
            definition.addArgument((InputArgument) foo1);
            fail("addArgument() throws a LogicException if another argument is already registered with the same name");
        } catch (Exception e) {
            assertTrue("addArgument() throws a LogicException if another argument is already registered with the same name", e instanceof LogicException);
            assertEquals("An argument with name \"foo\" already exist.", e.getMessage());
        }

        // cannot add a parameter after an array parameter
        definition.addArgument(new InputArgument("fooarray", InputArgument.IS_ARRAY));
        try {
            definition.addArgument(new InputArgument("anotherbar"));
            fail("addArgument() throws a LogicException if there is an array parameter already registered");
        } catch (Exception e) {
            assertTrue("addArgument() throws a LogicException if there is an array parameter already registered", e instanceof LogicException);
            assertEquals("Cannot add an argument after an array argument.", e.getMessage());
        }

        // cannot add a required argument after an optional one
        definition = new InputDefinition();
        definition.addArgument((InputArgument) foo);
        try {
            definition.addArgument((InputArgument) foo2);
            fail("addArgument() throws a LogicException if you try to add a required argument after an optional one");
        } catch (Exception e) {
            assertTrue("addArgument() throws a LogicException if you try to add a required argument after an optional one", e instanceof LogicException);
            assertEquals("Cannot add a required argument after an optional one.", e.getMessage());
        }
    }

    @Test
    public void testGetArgument() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        assertEquals("getArgument() returns a InputArgument by its name", foo, definition.getArgument("foo"));
        try {
            definition.getArgument("bar");
            fail("getArgument() throws an IllegalArgumentException if the InputArgument name does not exist");
        } catch (Exception e) {
            assertTrue("getArgument() throws an IllegalArgumentException if the InputArgument name does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"bar\" argument does not exist.", e.getMessage());
        }
    }

    @Test
    public void testHasArgument() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        assertTrue("hasArgument() returns true if a InputArgument exists for the given name", definition.hasArgument("foo"));
        assertFalse("hasArgument() returns false if a InputArgument exists for the given name", definition.hasArgument("bar"));
    }

    @Test
    public void testGetArgumentRequiredCount() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo2);
        assertEquals("getArgumentRequiredCount() returns the number of required arguments", 1, definition.getArgumentRequiredCount());
        definition.addArgument((InputArgument) foo);
        assertEquals("getArgumentRequiredCount() returns the number of required arguments", 1, definition.getArgumentRequiredCount());
    }

    @Test
    public void testGetArgumentCount() {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo2);
        assertEquals("getArgumentCount() returns the number of arguments", 1, definition.getArgumentCount());
        definition.addArgument((InputArgument) foo);
        assertEquals("getArgumentCount() returns the number of arguments", 2, definition.getArgumentCount());
    }

    @Test
    public void testGetArgumentDefaults() {
        InputDefinition definition = new InputDefinition(new InputParameterInterface[] {
            new InputArgument("foo1", InputArgument.OPTIONAL),
            new InputArgument("foo2", InputArgument.OPTIONAL, "", "default"),
            new InputArgument("foo3", InputArgument.OPTIONAL | InputArgument.IS_ARRAY)
        });
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo1", null);
        foobar.put("foo2", "default");
        foobar.put("foo3", new ArrayList<Object>());
        assertEquals("getArgumentDefaults() return the default values for each argument", foobar, definition.getArgumentDefaults());

        definition = new InputDefinition(new InputParameterInterface[] {
            new InputArgument("foo4", InputArgument.OPTIONAL | InputArgument.IS_ARRAY, "", Arrays.asList(1, 2))
        });
        foobar = new HashMap<String, Object>();
        foobar.put("foo4", Arrays.asList(1, 2));
        assertEquals("getArgumentDefaults() return the default values for each argument", foobar, definition.getArgumentDefaults());
    }

    @Test
    public void testSetOptions() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        assertEquals("setOptions() sets the array of InputOption objects", foobar, definition.getOptions());
        definition.setOptions(Arrays.asList((InputOption) bar));
        foobar.clear();
        foobar.put("bar", (InputOption) bar);
        assertEquals("setOptions() clears all InputOption objects", foobar, definition.getOptions());
        try {
            definition.getOptionForShortcut("f");
            fail("setOptions() clears all InputOption objects");
        } catch (Exception e) {
            assertTrue("setOptions() clears all InputOption objects", e instanceof IllegalArgumentException);
            assertEquals("The \"-f\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testAddOptions() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        assertEquals("addOptions() adds an array of InputOption objects", foobar, definition.getOptions());
        foobar.put("bar", (InputOption) bar);
        definition.addOptions(Arrays.asList((InputOption) bar));
        assertEquals("addOptions() does not clear existing InputOption objects", foobar, definition.getOptions());
    }

    @Test
    public void testAddOption() {
        initializeOptions();

        InputDefinition definition = new InputDefinition();
        definition.addOption((InputOption) foo);
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        assertEquals("addOption() adds a InputOption object", foobar, definition.getOptions());
        definition.addOption((InputOption) bar);
        foobar.put("bar", (InputOption) bar);
        assertEquals("addOption() adds a InputOption object", foobar, definition.getOptions());

        try {
            definition.addOption((InputOption) foo2);
            fail("addOption() throws a LogicException if the another option is already registered with the same name");
        } catch (Exception e) {
            assertTrue("addOption() throws a LogicException if the another option is already registered with the same name", e instanceof LogicException);
            assertEquals("An option named \"foo\" already exist.", e.getMessage());
        }

        try {
            definition.addOption((InputOption) foo1);
            fail("addOption() throws a LogicException if the another option is already registered with the same shortcut");
        } catch (Exception e) {
            assertTrue("addOption() throws a LogicException if the another option is already registered with the same shortcut", e instanceof LogicException);
            assertEquals("An option with shortcut \"f\" already exist.", e.getMessage());
        }
    }

    @Test
    public void testGetOption() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        assertEquals("getOption() returns a InputOption by its name", foo, definition.getOption("foo"));

        try {
            definition.getOption("bar");
            fail("getOption() throws an IllegalArgumentException if the option name does not exist");
        } catch (Exception e) {
            assertTrue("getOption() throws an IllegalArgumentException if the option name does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"--bar\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testHasOption() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        assertTrue("hasOption() returns true if a InputOption exists for the given name", definition.hasOption("foo"));
        assertFalse("hasOption() returns false if a InputOption exists for the given name", definition.hasOption("bar"));
    }

    @Test
    public void testHasShortcut() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        assertTrue("hasShortcut() returns true if a InputOption exists for the given shortcut", definition.hasShortcut("f"));
        assertFalse("hasShortcut() returns false if a InputOption exists for the given shortcut", definition.hasShortcut("b"));
    }

    @Test
    public void testGetOptionForShortcut() {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        assertEquals("getOptionForShortcut() returns a InputOption by its shortcut", foo, definition.getOptionForShortcut("f"));
        try {
            definition.getOptionForShortcut("l");
            fail("getOption() throws an IllegalArgumentException if the shortcut does not exist");
        } catch (Exception e) {
            assertTrue("getOption() throws an IllegalArgumentException if the shortcut does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"-l\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testGetOptionDefaults() {
        InputDefinition definition = new InputDefinition(new InputParameterInterface[] {
            new InputOption("foo1", null, InputOption.VALUE_NONE),
            new InputOption("foo2", null, InputOption.VALUE_REQUIRED),
            new InputOption("foo3", null, InputOption.VALUE_REQUIRED, "", "default"),
            new InputOption("foo4", null, InputOption.VALUE_OPTIONAL),
            new InputOption("foo5", null, InputOption.VALUE_OPTIONAL, "", "default"),
            new InputOption("foo6", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY),
            new InputOption("foo7", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "", Arrays.asList(1, 2))
        });

        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("foo1", false); // TODO was null before, but getOptionDefaults returns false
        defaults.put("foo2", null);
        defaults.put("foo3", "default");
        defaults.put("foo4", null);
        defaults.put("foo5", "default");
        defaults.put("foo6", new ArrayList<Object>());
        defaults.put("foo7", Arrays.asList(1, 2));
        assertEquals("getOptionDefaults() returns the default values for all options", defaults, definition.getOptionDefaults());
    }

    @Test
    public void testGetSynopsis() {
        InputDefinition definition = new InputDefinition(new InputParameterInterface[] {new InputOption("foo")});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[--foo]", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputOption("foo", "f")});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[-f|--foo]", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputOption("foo", "f", InputOption.VALUE_REQUIRED)});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[-f|--foo=\"...\"]", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputOption("foo", "f", InputOption.VALUE_OPTIONAL)});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[-f|--foo[=\"...\"]]", definition.getSynopsis());

        definition = new InputDefinition(new InputParameterInterface[] {new InputArgument("foo")});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[foo]", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputArgument("foo", InputArgument.REQUIRED)});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "foo", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputArgument("foo", InputArgument.IS_ARRAY)});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "[foo1] ... [fooN]", definition.getSynopsis());
        definition = new InputDefinition(new InputParameterInterface[] {new InputArgument("foo", InputArgument.REQUIRED | InputArgument.IS_ARRAY)});
        assertEquals("getSynopsis() returns a synopsis of arguments and options", "foo1 ... [fooN]", definition.getSynopsis());
    }

    @Test
    public void testAsText() {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("foo", "bar");

        InputDefinition definition = new InputDefinition(new InputParameterInterface[] {
            new InputArgument("foo", InputArgument.OPTIONAL, "The foo argument"),
            new InputArgument("baz", InputArgument.OPTIONAL, "The baz argument", true),
            new InputArgument("bar", InputArgument.OPTIONAL | InputArgument.IS_ARRAY, "The bar argument", Arrays.asList("http://foo.com/")),
            new InputOption("foo", "f", InputOption.VALUE_REQUIRED, "The foo option"),
            new InputOption("baz", null, InputOption.VALUE_OPTIONAL, "The baz option", false),
            new InputOption("bar", "b", InputOption.VALUE_OPTIONAL, "The bar option", "bar"),
            new InputOption("qux", "", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "The qux option", Arrays.asList("http://foo.com/", "bar")),
            new InputOption("qux2", "", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "The qux2 option", foobar)
        });

        assertEquals("asText() returns a textual representation of the InputDefinition", getResource("definition_astext.txt"), definition.asText());
    }

    private void initializeArguments() {
        foo = new InputArgument("foo");
        bar = new InputArgument("bar");
        foo1 = new InputArgument("foo");
        foo2 = new InputArgument("foo2", InputArgument.REQUIRED);
    }

    private void initializeOptions() {
        foo = new InputOption("foo", "f");
        bar = new InputOption("bar", "b");
        foo1 = new InputOption("fooBis", "f");
        foo2 = new InputOption("foo", "p");
    }
}
