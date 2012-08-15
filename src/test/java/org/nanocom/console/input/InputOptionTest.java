package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class InputOptionTest {

    public InputOptionTest() {
    }

    @Test
    public void testConstructor() {
        InputOption option = new InputOption("foo");
        Assert.assertEquals("__construct() takes a name as its first argument", "foo", option.getName());
        option = new InputOption("--foo");
        Assert.assertEquals("__construct() removes the leading -- of the option name", "foo", option.getName());

        try {
            option = new InputOption("foo", "f", InputOption.VALUE_IS_ARRAY);
            Assert.fail(".setDefaultValue() throws an Exception if VALUE_IS_ARRAY option is used when an option does not accept a value");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".setDefaultValue() throws an Exception if VALUE_IS_ARRAY option is used when an option does not accept a value");
            Assert.assertEquals("Impossible to have an option mode VALUE_IS_ARRAY if the option does not accept a value.", e.getMessage());
        }

        // shortcut argument
        option = new InputOption("foo", "f");
        Assert.assertEquals("__construct() can take a shortcut as its second argument", "f", option.getShortcut());
        option = new InputOption("foo", "-f");
        Assert.assertEquals("__construct() removes the leading - of the shortcut", "f", option.getShortcut());

        // mode argument
        option = new InputOption("foo", "f");
        Assert.assertFalse("__construct() gives a \"InputOption.VALUE_NONE\" mode by default", option.acceptValue());
        Assert.assertFalse("__construct() gives a \"InputOption.VALUE_NONE\" mode by default", option.isValueRequired());
        Assert.assertFalse("__construct() gives a \"InputOption.VALUE_NONE\" mode by default", option.isValueOptional());

        option = new InputOption("foo", "f", null);
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.acceptValue());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.isValueRequired());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_NONE);
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.acceptValue());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.isValueRequired());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_NONE\" as its mode", option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_REQUIRED);
        Assert.assertTrue("__construct() can take \"InputOption.VALUE_REQUIRED\" as its mode", option.acceptValue());
        Assert.assertTrue("__construct() can take \"InputOption.VALUE_REQUIRED\" as its mode", option.isValueRequired());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_REQUIRED\" as its mode",option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL);
        Assert.assertTrue("__construct() can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.acceptValue());
        Assert.assertFalse("__construct() can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.isValueRequired());
        Assert.assertTrue("__construct() can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.isValueOptional());

        try {
            option = new InputOption("foo", "f", -1);
            Assert.fail("__construct() throws an Exception if the mode is not valid");
        } catch (Exception e) {
            // Assert.assertInstanceOf("__construct() throws an Exception if the mode is not valid", "Exception", e);
            Assert.assertEquals("Option mode \"-1\" is not valid.", e.getMessage());
        }
    }

    @Test
    public void testIsArray() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        Assert.assertTrue(".isArray() returns true if the option can be an array", option.isArray());
        option = new InputOption("foo", null, InputOption.VALUE_NONE);
        Assert.assertFalse(".isArray() returns false if the option can not be an array", option.isArray());
    }

    @Test
    public void testGetDescription() {
        InputOption option = new InputOption("foo", "f", null, "Some description");
        Assert.assertEquals(".getDescription() returns the description message", "Some description", option.getDescription());
    }

    @Test
    public void testgetDefaultValue() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL, "", "default");
        Assert.assertEquals(".getDefaultValue() returns the default value", "default", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED, "", "default");
        Assert.assertEquals(".getDefaultValue() returns the default value", "default", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED);
        Assert.assertNull(".getDefaultValue() returns null if no default value is configured", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        Assert.assertEquals(".getDefaultValue() returns an empty array if option is an array", new ArrayList<Object>(), option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_NONE);
        Assert.assertFalse(".getDefaultValue() returns false if the option does not take a value", (Boolean) option.getDefaultValue());
    }

    @Test
    public void testsetDefaultValue() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_REQUIRED, "", "default");
        option.setDefaultValue(null);
        Assert.assertNull(".setDefaultValue() can reset the default value by passing null", option.getDefaultValue());
        option.setDefaultValue("another");
        Assert.assertEquals(".setDefaultValue() changes the default value", "another", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED | InputOption.VALUE_IS_ARRAY);
        option.setDefaultValue(Arrays.asList(1, 2));
        Assert.assertEquals(".setDefaultValue() changes the default value", Arrays.asList(1, 2), option.getDefaultValue());

        option = new InputOption("foo", "f", InputOption.VALUE_NONE);
        try {
            option.setDefaultValue("default");
            Assert.fail(".setDefaultValue() throws an Exception if you give a default value for a VALUE_NONE option");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".setDefaultValue() throws an Exception if you give a default value for a VALUE_NONE option");
            Assert.assertEquals("Cannot set a default value when using Option.VALUE_NONE mode.", e.getMessage());
        }

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        try {
            option.setDefaultValue("default");
            Assert.fail(".setDefaultValue() throws an Exception if you give a default value which is not an array for a VALUE_IS_ARRAY option");
        } catch (Exception e) {
            // Assert.assertInstanceOf("\Exception", e, ".setDefaultValue() throws an Exception if you give a default value which is not an array for a VALUE_IS_ARRAY option");
            Assert.assertEquals("A default value for an array option must be an array.", e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        InputOption option = new InputOption("foo", "f", null, "Some description");
        InputOption option2 = new InputOption("foo", "f", null, "Alternative description");
        Assert.assertTrue(option.equals(option2));

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description");
        option2 = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description", true);
        Assert.assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", null, "Some description");
        option2 = new InputOption("bar", "f", null, "Some description");
        Assert.assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", null, "Some description");
        option2 = new InputOption("foo", "", null, "Some description");
        Assert.assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", null, "Some description");
        option2 = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description");
        Assert.assertFalse(option.equals(option2));
    }

}
