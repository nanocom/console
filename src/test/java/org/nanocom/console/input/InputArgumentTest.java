package org.nanocom.console.input;

import org.nanocom.console.input.InputArgument;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class InputArgumentTest {

    public InputArgumentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConstructor() throws Exception {
        InputArgument argument = new InputArgument("foo");
        Assert.assertEquals("__construct() takes a name as its first argument", "foo", argument.getName());

        argument = new InputArgument("foo");
        Assert.assertFalse("__construct() gives a \"InputArgument.OPTIONAL\" mode by default", argument.isRequired());

        argument = new InputArgument("foo", null);
        Assert.assertFalse("__construct() can take \"InputArgument.OPTIONAL\" as its mode", argument.isRequired());

        argument = new InputArgument("foo", InputArgument.OPTIONAL);
        Assert.assertFalse("__construct() can take \"InputArgument.OPTIONAL\" as its mode", argument.isRequired());

        argument = new InputArgument("foo", InputArgument.REQUIRED);
        Assert.assertTrue("__construct() can take \"InputArgument.REQUIRED\" as its mode", argument.isRequired());

        try {
            argument = new InputArgument("foo", -1);
            Assert.fail("__construct() throws an Exception if the mode is not valid");
        } catch (Exception e) {
            // Assert.assertTrue(e instanceof Exception, "__construct() throws an Exception if the mode is not valid");
            Assert.assertEquals("Argument mode \"-1\" is not valid.", e.getMessage());
        }
    }

    @Test
    public void testIsArray() throws Exception {
        InputArgument argument = new InputArgument("foo", InputArgument.IS_ARRAY);
        Assert.assertTrue(".isArray() returns true if the argument can be an array", argument.isArray());
        argument = new InputArgument("foo", InputArgument.OPTIONAL | InputArgument.IS_ARRAY);
        Assert.assertTrue(".isArray() returns true if the argument can be an array", argument.isArray());
        argument = new InputArgument("foo", InputArgument.OPTIONAL);
        Assert.assertFalse(".isArray() returns false if the argument can not be an array", argument.isArray());
    }

    @Test
    public void testGetDescription() throws Exception {
        InputArgument argument = new InputArgument("foo", null, "Some description");
        Assert.assertEquals(".getDescription() return the message description", "Some description", argument.getDescription());
    }

    @Test
    public void testGetDefault() throws Exception {
        InputArgument argument = new InputArgument("foo", InputArgument.OPTIONAL, "", "default");
        Assert.assertEquals(".getDefault() return the default value", "default", argument.getDefaultValue());
    }

    @Test
    public void testSetDefault() throws Exception {
        InputArgument argument = new InputArgument("foo", InputArgument.OPTIONAL, "", "default");
        argument.setDefaultValue(null);
        Assert.assertNull(".setDefault() can reset the default value by passing null", argument.getDefaultValue());
        argument.setDefaultValue("another");
        Assert.assertEquals(".setDefaultValue() changes the default value", "another", argument.getDefaultValue());

        argument = new InputArgument("foo", InputArgument.OPTIONAL | InputArgument.IS_ARRAY);
        argument.setDefaultValue(Arrays.asList(1, 2));
        Assert.assertEquals(".setDefaultValue() changes the default value", Arrays.asList(1, 2), argument.getDefaultValue());

        try {
            argument = new InputArgument("foo", InputArgument.REQUIRED);
            argument.setDefaultValue("default");
            Assert.fail(".setDefault() throws an Exception if you give a default value for a required argument");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".parse() throws an \InvalidArgumentException exception if an invalid option is passed");
            Assert.assertEquals("Cannot set a default value except for Parameter.OPTIONAL mode.", e.getMessage());
        }

        try {
            argument = new InputArgument("foo", InputArgument.IS_ARRAY);
            argument.setDefaultValue((Object) "default");
            Assert.fail(".setDefaultValue() throws an Exception if you give a default value which is not an array for a IS_ARRAY option");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".setDefault() throws an Exception if you give a default value which is not an array for a IS_ARRAY option");
            Assert.assertEquals("A default value for an array argument must be an array.", e.getMessage());
        }
    }

}
