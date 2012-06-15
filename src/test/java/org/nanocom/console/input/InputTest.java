package org.nanocom.console.input;

import org.nanocom.console.input.ArrayInput;
import org.nanocom.console.input.InputDefinition;
import org.nanocom.console.input.InputOption;
import org.nanocom.console.input.InputArgument;
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
public class InputTest {
    
    public InputTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConstructor() throws Exception {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("name", "foo");
        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        Assert.assertEquals(".__construct() takes a InputDefinition as an argument", "foo", input.getArgument("name"));
    }

    @Test
    public void testOptions() throws Exception {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("--name", "foo");
        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputOption("name"))));
        Assert.assertEquals(".getOption() returns the value for the given option", "foo", input.getOption("name"));

        input.setOption("name", "bar");
        foobar.clear();
        foobar.put("name", "bar");
        Assert.assertEquals(".setOption() sets the value for a given option", "bar", input.getOption("name"));
        Assert.assertEquals(".getOptions() returns all option values", foobar, input.getOptions());

        foobar.clear();
        foobar.put("--name", "foo");
        input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputOption("name"), new InputOption("bar", "", InputOption.VALUE_OPTIONAL, "", "default"))));
        Assert.assertEquals(".getOption() returns the default value for optional options", "default", input.getOption("bar"));
        foobar.clear();
        foobar.put("name", "foo");
        foobar.put("bar", "default");
        Assert.assertEquals(".getOptions() returns all option values, even optional ones", foobar, input.getOptions());

        try {
            input.setOption("foo", "bar");
            Assert.fail(".setOption() throws an Exception if the option does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setOption() throws a \InvalidArgumentException if the option does not exist");
            Assert.assertEquals("The \"foo\" option does not exist.", e.getMessage());
        }

        try {
            input.getOption("foo");
            Assert.fail(".getOption() throws an Exception if the option does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setOption() throws a \InvalidArgumentException if the option does not exist");
            Assert.assertEquals("The \"foo\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testArguments() throws Exception {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("name", "foo");

        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        Assert.assertEquals(".getArgument() returns the value for the given argument", "foo", input.getArgument("name"));

        input.setArgument("name", "bar");
        foobar = new HashMap<String, String>();
        foobar.clear();
        foobar.put("name", "bar");
        Assert.assertEquals(".setArgument() sets the value for a given argument", "bar", input.getArgument("name"));
        Assert.assertEquals(".getArguments() returns all argument values", foobar, input.getArguments());

        foobar.clear();
        foobar.put("name", "foo");
        foobar.put("bar", "default");
        input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"), new InputArgument("bar", InputArgument.OPTIONAL, "", "default"))));
        Assert.assertEquals(".getArgument() returns the default value for optional arguments", "default", input.getArgument("bar"));
        Assert.assertEquals(".getArguments() returns all argument values, even optional ones", foobar, input.getArguments());

        try {
            input.setArgument("foo", "bar");
            Assert.fail(".setArgument() throws an Exception if the argument does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setOption() throws a \InvalidArgumentException if the option does not exist");
            Assert.assertEquals("The \"foo\" argument does not exist.", e.getMessage());
        }

        try {
            input.getArgument("foo");
            Assert.fail(".getArgument() throws an Exception if the argument does not exist");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\InvalidArgumentException", e, ".setOption() throws a \InvalidArgumentException if the option does not exist");
            Assert.assertEquals("The \"foo\" argument does not exist.", e.getMessage());
        }
    }

    @Test
    public void testValidate() throws Exception {
        ArrayInput input = new ArrayInput(new HashMap<String, String>());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name", InputArgument.REQUIRED))));

        try {
            input.validate();
            Assert.fail(".validate() throws an Exception if not enough arguments are given");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\RuntimeException", e, ".validate() throws a \RuntimeException if not enough arguments are given");
            Assert.assertEquals("Not enough arguments.", e.getMessage());
        }

        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("name", "foo");
        input = new ArrayInput(foobar);
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name", InputArgument.REQUIRED))));

        try {
            input.validate();
        } catch (Exception e) {
            Assert.fail(".validate() does not throw an Exception if enough arguments are given");
        }
    }

    @Test
    public void testSetFetInteractive() throws Exception {
        ArrayInput input = new ArrayInput(new HashMap<String, String>());
        Assert.assertTrue(".isInteractive() returns whether the input should be interactive or not", input.isInteractive());
        input.setInteractive(false);
        Assert.assertFalse(".setInteractive() changes the interactive flag", input.isInteractive());
    }

}
