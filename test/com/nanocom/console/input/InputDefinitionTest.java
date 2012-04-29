package com.nanocom.console.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class InputDefinitionTest {

    static private String fixtures;

    private Object foo;
    private Object bar;
    private Object foo1;
    private Object foo2;

    public InputDefinitionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        fixtures = "/../Fixtures/";
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConstructor() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        Assert.assertEquals("__construct() creates a new InputDefinition object", new HashMap<String, InputArgument>(), definition.getArguments());

        definition = new InputDefinition(Arrays.asList(foo, bar));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        foobar.put("bar", bar);
        Assert.assertEquals("__construct() takes an array of InputArgument objects as its first argument", foobar, definition.getArguments());

        initializeOptions();

        foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        foobar.put("bar", bar);

        definition = new InputDefinition();
        Assert.assertEquals("__construct() creates a new InputDefinition object", new HashMap<String, InputOption>(), definition.getOptions());

        definition = new InputDefinition(Arrays.asList(foo, bar));
        Assert.assertEquals("__construct() takes an array of InputOption objects as its first argument", foobar, definition.getOptions());
    }

    @Test
    public void testSetArguments() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.setArguments(Arrays.asList((InputArgument) foo));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        Assert.assertEquals(".setArguments() sets the array of InputArgument objects", foobar, definition.getArguments());
        definition.setArguments(Arrays.asList((InputArgument) bar));
        foobar.clear();
        foobar.put("bar", bar);
        Assert.assertEquals(".setArguments() clears all InputArgument objects", foobar, definition.getArguments());
    }

    @Test
    public void testAddArguments() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        Assert.assertEquals(".addArguments() adds an array of InputArgument objects", foobar, definition.getArguments());
        definition.addArguments(Arrays.asList((InputArgument) bar));
        foobar.put("bar", bar);
        Assert.assertEquals(".addArguments() does not clear existing InputArgument objects", foobar, definition.getArguments());
    }

    @Test
    public void testAddArgument() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo);
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo", foo);
        Assert.assertEquals(".addArgument() adds a InputArgument object", foobar, definition.getArguments());
        definition.addArgument((InputArgument) bar);
        foobar.put("bar", bar);
        Assert.assertEquals(".addArgument() adds a InputArgument object", foobar, definition.getArguments());

        // arguments must have different names
        try {
            definition.addArgument((InputArgument) foo1);
            Assert.fail(".addArgument() throws a Exception if another argument is already registered with the same name");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\Exception", e, ".addArgument() throws a Exception if another argument is already registered with the same name");
            Assert.assertEquals("An argument with name \"foo\" already exist.", e.getMessage());
        }

        // cannot add a parameter after an array parameter
        definition.addArgument(new InputArgument("fooarray", InputArgument.IS_ARRAY));
        try {
            definition.addArgument(new InputArgument("anotherbar"));
            Assert.fail(".addArgument() throws a Exception if there is an array parameter already registered");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\Exception", e, ".addArgument() throws a Exception if there is an array parameter already registered");
            Assert.assertEquals("Cannot add an argument after an array argument.", e.getMessage());
        }


        // cannot add a required argument after an optional one
        definition = new InputDefinition();
        definition.addArgument((InputArgument) foo);
        try {
            definition.addArgument((InputArgument) foo2);
            Assert.fail(".addArgument() throws an exception if you try to add a required argument after an optional one");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\Exception", e, ".addArgument() throws an exception if you try to add a required argument after an optional one");
            Assert.assertEquals("Cannot add a required argument after an optional one.", e.getMessage());
        }
    }

    @Test
    public void testGetArgument() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        Assert.assertEquals(".getArgument() returns a InputArgument by its name", foo, definition.getArgument("foo"));
        try {
            definition.getArgument("bar");
            Assert.fail(".getArgument() throws an exception if the InputArgument name does not exist");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\Exception", e, ".getArgument() throws an exception if the InputArgument name does not exist");
            Assert.assertEquals("The \"bar\" argument does not exist.", e.getMessage());
        }
    }

    @Test
    public void testHasArgument() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArguments(Arrays.asList((InputArgument) foo));
        Assert.assertTrue(".hasArgument() returns true if a InputArgument exists for the given name", definition.hasArgument("foo"));
        Assert.assertFalse(".hasArgument() returns false if a InputArgument exists for the given name", definition.hasArgument("bar"));
    }

    @Test
    public void testGetArgumentRequiredCount() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo2);
        Assert.assertEquals(".getArgumentRequiredCount() returns the number of required arguments", 1, definition.getArgumentRequiredCount());
        definition.addArgument((InputArgument) foo);
        Assert.assertEquals(".getArgumentRequiredCount() returns the number of required arguments", 1, definition.getArgumentRequiredCount());
    }

    @Test
    public void testGetArgumentCount() throws Exception {
        initializeArguments();

        InputDefinition definition = new InputDefinition();
        definition.addArgument((InputArgument) foo2);
        Assert.assertEquals(".getArgumentCount() returns the number of arguments", 1, definition.getArgumentCount());
        definition.addArgument((InputArgument) foo);
        Assert.assertEquals(".getArgumentCount() returns the number of arguments", 2, definition.getArgumentCount());
    }

    @Test
    public void testGetArgumentDefaults() throws Exception {
        InputDefinition definition = new InputDefinition(Arrays.asList((Object)
            new InputArgument("foo1", InputArgument.OPTIONAL),
            new InputArgument("foo2", InputArgument.OPTIONAL, "", "default"),
            new InputArgument("foo3", InputArgument.OPTIONAL | InputArgument.IS_ARRAY)
        ));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("foo1", null);
        foobar.put("foo2", "default");
        foobar.put("foo3", new ArrayList<Object>());
        Assert.assertEquals(".getArgumentDefaults() return the default values for each argument", foobar, definition.getArgumentDefaults());

        definition = new InputDefinition(Arrays.asList((Object)
            new InputArgument("foo4", InputArgument.OPTIONAL | InputArgument.IS_ARRAY, "", Arrays.asList(1, 2))
        ));
        foobar = new HashMap<String, Object>();
        foobar.put("foo4", Arrays.asList(1, 2));
        Assert.assertEquals(".getArgumentDefaults() return the default values for each argument", foobar, definition.getArgumentDefaults());
    }

    @Test
    public void testSetOptions() throws Exception {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        Assert.assertEquals(".setOptions() sets the array of InputOption objects", foobar, definition.getOptions());
        definition.setOptions(Arrays.asList((InputOption) bar));
        foobar.clear();
        foobar.put("bar", (InputOption) bar);
        Assert.assertEquals(".setOptions() clears all InputOption objects", foobar, definition.getOptions());
        try {
            definition.getOptionForShortcut("f");
            Assert.fail(".setOptions() clears all InputOption objects");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".setOptions() clears all InputOption objects");
            Assert.assertEquals("The \"-f\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testAddOptions() throws Exception {
        initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        Assert.assertEquals(".addOptions() adds an array of InputOption objects", foobar, definition.getOptions());
        foobar.put("bar", (InputOption) bar);
        definition.addOptions(Arrays.asList((InputOption) bar));
        Assert.assertEquals(".addOptions() does not clear existing InputOption objects", foobar, definition.getOptions());
    }

    @Test
    public void testAddOption() throws Exception {
        initializeOptions();

        InputDefinition definition = new InputDefinition();
        definition.addOption((InputOption) foo);
        Map<String, InputOption> foobar = new HashMap<String, InputOption>();
        foobar.put("foo", (InputOption) foo);
        Assert.assertEquals(".addOption() adds a InputOption object", foobar, definition.getOptions());
        definition.addOption((InputOption) bar);
        foobar.put("bar", (InputOption) bar);
        Assert.assertEquals(".addOption() adds a InputOption object", foobar, definition.getOptions());
        try {
            definition.addOption((InputOption) foo2);
            Assert.fail(".addOption() throws a Exception if the another option is already registered with the same name");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".addOption() throws a Exception if the another option is already registered with the same name");
            Assert.assertEquals("An option named \"foo\" already exist.", e.getMessage());
        }
        try {
            definition.addOption((InputOption) foo1);
            Assert.fail(".addOption() throws a Exception if the another option is already registered with the same shortcut");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".addOption() throws a Exception if the another option is already registered with the same shortcut");
            Assert.assertEquals("An option with shortcut \"f\" already exist.", e.getMessage());
        }
    }

    @Test
    public void testGetOption() throws Exception {
        this.initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(foo));
        Assert.assertEquals(".getOption() returns a InputOption by its name", foo, definition.getOption("foo"));
        try {
            definition.getOption("bar");
            Assert.fail(".getOption() throws an exception if the option name does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".getOption() throws an exception if the option name does not exist");
            Assert.assertEquals("The \"--bar\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testHasOption() throws Exception {
        this.initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        Assert.assertTrue(".hasOption() returns true if a InputOption exists for the given name", definition.hasOption("foo"));
        Assert.assertFalse(".hasOption() returns false if a InputOption exists for the given name", definition.hasOption("bar"));
    }

    @Test
    public void testHasShortcut() throws Exception {
        this.initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        Assert.assertTrue(".hasShortcut() returns true if a InputOption exists for the given shortcut", definition.hasShortcut("f"));
        Assert.assertFalse(".hasShortcut() returns false if a InputOption exists for the given shortcut", definition.hasShortcut("b"));
    }

    @Test
    public void testGetOptionForShortcut() throws Exception {
        this.initializeOptions();

        InputDefinition definition = new InputDefinition(Arrays.asList(this.foo));
        Assert.assertEquals(".getOptionForShortcut() returns a InputOption by its shortcut", foo, definition.getOptionForShortcut("f"));
        try {
            definition.getOptionForShortcut("l");
            Assert.fail(".getOption() throws an exception if the shortcut does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".getOption() throws an exception if the shortcut does not exist");
            Assert.assertEquals("The \"-l\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testGetOptionDefaults() throws Exception {
        InputDefinition definition = new InputDefinition(Arrays.asList((Object)
            new InputOption("foo1", null, InputOption.VALUE_NONE),
            new InputOption("foo2", null, InputOption.VALUE_REQUIRED),
            new InputOption("foo3", null, InputOption.VALUE_REQUIRED, "", "default"),
            new InputOption("foo4", null, InputOption.VALUE_OPTIONAL),
            new InputOption("foo5", null, InputOption.VALUE_OPTIONAL, "", "default"),
            new InputOption("foo6", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY),
            new InputOption("foo7", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "", Arrays.asList(1, 2))
        ));
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("foo1", null);
        defaults.put("foo2", null);
        defaults.put("foo3", "default");
        defaults.put("foo4", null);
        defaults.put("foo5", "default");
        defaults.put("foo6", new ArrayList<Object>());
        defaults.put("foo7", Arrays.asList(1, 2));
        Assert.assertEquals(".getOptionDefaults() returns the default values for all options", defaults, definition.getOptionDefaults());
    }

    @Test
    public void testGetSynopsis() throws Exception {
        InputDefinition definition = new InputDefinition(Arrays.asList((Object) new InputOption("foo")));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[--foo]", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f")));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[-f|--foo]", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED)));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[-f|--foo=\"...\"]", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_OPTIONAL)));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[-f|--foo[=\"...\"]]", definition.getSynopsis());

        definition = new InputDefinition(Arrays.asList((Object) new InputArgument("foo")));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[foo]", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputArgument("foo", InputArgument.REQUIRED)));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "foo", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputArgument("foo", InputArgument.IS_ARRAY)));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "[foo1] ... [fooN]", definition.getSynopsis());
        definition = new InputDefinition(Arrays.asList((Object) new InputArgument("foo", InputArgument.REQUIRED | InputArgument.IS_ARRAY)));
        Assert.assertEquals(".getSynopsis() returns a synopsis of arguments and options", "foo1 ... [fooN]", definition.getSynopsis());
    }

    public void testAsText() throws Exception {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("foo", "bar");

        InputDefinition definition = new InputDefinition(Arrays.asList((Object)
            new InputArgument("foo", InputArgument.OPTIONAL, "The foo argument"),
            new InputArgument("baz", InputArgument.OPTIONAL, "The baz argument", true),
            new InputArgument("bar", InputArgument.OPTIONAL | InputArgument.IS_ARRAY, "The bar argument", Arrays.asList("bar")),
            new InputOption("foo", "f", InputOption.VALUE_REQUIRED, "The foo option"),
            new InputOption("baz", null, InputOption.VALUE_OPTIONAL, "The baz option", false),
            new InputOption("bar", "b", InputOption.VALUE_OPTIONAL, "The bar option", "bar"),
            new InputOption("qux", "", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "The qux option", Arrays.asList("foo", "bar")),
            new InputOption("qux2", "", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY, "The qux2 option", foobar)
        ));
        // Assert.assertStringEqualsFile(self.fixtures."/definition_astext.txt", definition.asText(), ".asText() returns a textual representation of the InputDefinition");
    }

    /*public void testAsXml() throws Exception {
        InputDefinition definition = new InputDefinition(Arrays.asList(
            new InputArgument("foo", InputArgument.OPTIONAL, "The foo argument"),
            new InputArgument("baz", InputArgument.OPTIONAL, "The baz argument", true),
            new InputArgument("bar", InputArgument.OPTIONAL | InputArgument.IS_ARRAY, "The bar argument", Arrays.asList("bar")),
            new InputOption("foo", "f", InputOption.VALUE_REQUIRED, "The foo option"),
            new InputOption("baz", null, InputOption.VALUE_OPTIONAL, "The baz option", false),
            new InputOption("bar", "b", InputOption.VALUE_OPTIONAL, "The bar option", "bar"),
        ));
        Assert.assertXmlStringEqualsXmlFile(self.fixtures."/definition_asxml.txt", definition.asXml(), ".asText() returns a textual representation of the InputDefinition");
    }*/

    private void initializeArguments() throws Exception {
        foo = new InputArgument("foo");
        bar = new InputArgument("bar");
        foo1 = new InputArgument("foo");
        foo2 = new InputArgument("foo2", InputArgument.REQUIRED);
    }

    private void initializeOptions() throws Exception {
        foo = new InputOption("foo", "f");
        bar = new InputOption("bar", "b");
        foo1 = new InputOption("fooBis", "f");
        foo2 = new InputOption("foo", "p");
    }

}
