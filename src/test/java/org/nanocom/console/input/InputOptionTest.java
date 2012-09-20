/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class InputOptionTest {

    public InputOptionTest() {
    }

    @Test
    public void testConstructor() {
        InputOption option = new InputOption("foo");
        assertEquals("Constructor takes a name as its first argument", "foo", option.getName());
        option = new InputOption("--foo");
        assertEquals("Constructor removes the leading -- of the option name", "foo", option.getName());

        try {
            option = new InputOption("foo", "f", InputOption.VALUE_IS_ARRAY);
            fail("Constructor throws an IllegalArgumentException if VALUE_IS_ARRAY option is used when an option does not accept a value");
        } catch (Exception e) {
            assertTrue("setDefaultValue() throws an IllegalArgumentException if VALUE_IS_ARRAY option is used when an option does not accept a value", e instanceof IllegalArgumentException);
            assertEquals("Impossible to have an option mode VALUE_IS_ARRAY if the option does not accept a value.", e.getMessage());
        }

        // shortcut argument
        option = new InputOption("foo", "f");
        assertEquals("Constructor can take a shortcut as its second argument", "f", option.getShortcut());
        option = new InputOption("foo", "-f");
        assertEquals("Constructor removes the leading - of the shortcut", "f", option.getShortcut());

        // mode argument
        option = new InputOption("foo", "f");
        assertFalse("Constructor gives a \"InputOption.VALUE_NONE\" mode by default", option.acceptValue());
        assertFalse("Constructor gives a \"InputOption.VALUE_NONE\" mode by default", option.isValueRequired());
        assertFalse("Constructor gives a \"InputOption.VALUE_NONE\" mode by default", option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_NONE);
        assertFalse("Constructor can take \"InputOption.VALUE_NONE\" as its mode", option.acceptValue());
        assertFalse("Constructor can take \"InputOption.VALUE_NONE\" as its mode", option.isValueRequired());
        assertFalse("Constructor can take \"InputOption.VALUE_NONE\" as its mode", option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_REQUIRED);
        assertTrue("Constructor can take \"InputOption.VALUE_REQUIRED\" as its mode", option.acceptValue());
        assertTrue("Constructor can take \"InputOption.VALUE_REQUIRED\" as its mode", option.isValueRequired());
        assertFalse("Constructor can take \"InputOption.VALUE_REQUIRED\" as its mode",option.isValueOptional());

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL);
        assertTrue("Constructor can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.acceptValue());
        assertFalse("Constructor can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.isValueRequired());
        assertTrue("Constructor can take \"InputOption.VALUE_OPTIONAL\" as its mode", option.isValueOptional());

        try {
            option = new InputOption("foo", "f", -1);
            fail("Constructor throws an Exception if the mode is not valid");
        } catch (Exception e) {
            // assertInstanceOf("Constructor throws an Exception if the mode is not valid", "Exception", e);
            assertEquals("Option mode \"-1\" is not valid.", e.getMessage());
        }
    }

    @Test
    public void testIsArray() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        assertTrue(".isArray() returns true if the option can be an array", option.isArray());
        option = new InputOption("foo", null, InputOption.VALUE_NONE);
        assertFalse(".isArray() returns false if the option can not be an array", option.isArray());
    }

    @Test
    public void testGetDescription() {
        InputOption option = new InputOption("foo", "f", InputOption.VALUE_NONE, "Some description");
        assertEquals(".getDescription() returns the description message", "Some description", option.getDescription());
    }

    @Test
    public void testgetDefaultValue() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL, "", "default");
        assertEquals(".getDefaultValue() returns the default value", "default", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED, "", "default");
        assertEquals(".getDefaultValue() returns the default value", "default", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED);
        assertNull(".getDefaultValue() returns null if no default value is configured", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        assertEquals(".getDefaultValue() returns an empty array if option is an array", new ArrayList<Object>(), option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_NONE);
        assertFalse(".getDefaultValue() returns false if the option does not take a value", (Boolean) option.getDefaultValue());
    }

    @Test
    public void testsetDefaultValue() {
        InputOption option = new InputOption("foo", null, InputOption.VALUE_REQUIRED, "", "default");
        option.setDefaultValue(null);
        assertNull(".setDefaultValue() can reset the default value by passing null", option.getDefaultValue());
        option.setDefaultValue("another");
        assertEquals(".setDefaultValue() changes the default value", "another", option.getDefaultValue());

        option = new InputOption("foo", null, InputOption.VALUE_REQUIRED | InputOption.VALUE_IS_ARRAY);
        option.setDefaultValue(Arrays.asList(1, 2));
        assertEquals(".setDefaultValue() changes the default value", Arrays.asList(1, 2), option.getDefaultValue());

        option = new InputOption("foo", "f", InputOption.VALUE_NONE);
        try {
            option.setDefaultValue("default");
            fail(".setDefaultValue() throws an Exception if you give a default value for a VALUE_NONE option");
        } catch (Exception e) {
            // assertInstanceOf("\Exception", e, ".setDefaultValue() throws an Exception if you give a default value for a VALUE_NONE option");
            assertEquals("Cannot set a default value when using Option.VALUE_NONE mode.", e.getMessage());
        }

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY);
        try {
            option.setDefaultValue("default");
            fail(".setDefaultValue() throws an Exception if you give a default value which is not an array for a VALUE_IS_ARRAY option");
        } catch (Exception e) {
            // assertInstanceOf("\Exception", e, ".setDefaultValue() throws an Exception if you give a default value which is not an array for a VALUE_IS_ARRAY option");
            assertEquals("A default value for an array option must be an array.", e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        InputOption option = new InputOption("foo", "f", InputOption.VALUE_NONE, "Some description");
        InputOption option2 = new InputOption("foo", "f", InputOption.VALUE_NONE, "Alternative description");
        assertTrue(option.equals(option2));

        option = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description");
        option2 = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description", true);
        assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", InputOption.VALUE_NONE, "Some description");
        option2 = new InputOption("bar", "f", InputOption.VALUE_NONE, "Some description");
        assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", InputOption.VALUE_NONE, "Some description");
        option2 = new InputOption("foo", "", InputOption.VALUE_NONE, "Some description");
        assertFalse(option.equals(option2));

        option = new InputOption("foo", "f", InputOption.VALUE_NONE, "Some description");
        option2 = new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "Some description");
        assertFalse(option.equals(option2));
    }
}
