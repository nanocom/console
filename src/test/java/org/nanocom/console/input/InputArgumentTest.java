/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.exception.LogicException;

public class InputArgumentTest {

    public InputArgumentTest() {
    }

    @Test
    public void testConstructor() {
        InputArgument argument = new InputArgument("foo");
        assertEquals("Constructor takes a name as its first argument", "foo", argument.getName());

        argument = new InputArgument("foo");
        assertFalse("Constructor gives a \"InputArgument.OPTIONAL\" mode by default", argument.isRequired());

        argument = new InputArgument("foo", InputArgument.OPTIONAL);
        assertFalse("Constructor can take \"InputArgument.OPTIONAL\" as its mode", argument.isRequired());

        argument = new InputArgument("foo", InputArgument.REQUIRED);
        assertTrue("Constructor can take \"InputArgument.REQUIRED\" as its mode", argument.isRequired());

        try {
            argument = new InputArgument("foo", -1);
            fail("Constructor throws an IllegalArgumentException if the mode is not valid");
        } catch (Exception e) {
            assertTrue("Constructor throws an IllegalArgumentException if the mode is not valid", e instanceof IllegalArgumentException);
            assertEquals("Argument mode \"-1\" is not valid.", e.getMessage());
        }
    }

    @Test
    public void testIsArray() {
        InputArgument argument = new InputArgument("foo", InputArgument.IS_ARRAY);
        assertTrue("isArray() returns true if the argument can be an array", argument.isArray());
        argument = new InputArgument("foo", InputArgument.OPTIONAL | InputArgument.IS_ARRAY);
        assertTrue("isArray() returns true if the argument can be an array", argument.isArray());
        argument = new InputArgument("foo", InputArgument.OPTIONAL);
        assertFalse("isArray() returns false if the argument can not be an array", argument.isArray());
    }

    @Test
    public void testGetDescription() {
        InputArgument argument = new InputArgument("foo", InputArgument.OPTIONAL, "Some description");
        assertEquals("getDescription() return the message description", "Some description", argument.getDescription());
    }

    @Test
    public void testGetDefault() {
        InputArgument argument = new InputArgument("foo", InputArgument.OPTIONAL, "", "default");
        assertEquals("getDefault() return the default value", "default", argument.getDefaultValue());
    }

    @Test
    public void testsetDefaultValue() {
        InputArgument argument = new InputArgument("foo", InputArgument.OPTIONAL, "", "default");
        argument.setDefaultValue(null);
        assertNull("setDefaultValue() can reset the default value by passing null", argument.getDefaultValue());
        argument.setDefaultValue("another");
        assertEquals("setDefaultValue() changes the default value", "another", argument.getDefaultValue());

        argument = new InputArgument("foo", InputArgument.OPTIONAL | InputArgument.IS_ARRAY);
        argument.setDefaultValue(Arrays.asList(1, 2));
        assertEquals("setDefaultValue() changes the default value", Arrays.asList(1, 2), argument.getDefaultValue());

        try {
            argument = new InputArgument("foo", InputArgument.REQUIRED);
            argument.setDefaultValue("default");
            fail("setDefaultValue() throws a LogicException if you give a default value for a required argument");
        } catch (Exception e) {
            assertTrue("parse() throws a LogicException if an invalid option is passed", e instanceof LogicException);
            assertEquals("Cannot set a default value except for Parameter.OPTIONAL mode.", e.getMessage());
        }

        try {
            argument = new InputArgument("foo", InputArgument.IS_ARRAY);
            argument.setDefaultValue((Object) "default");
            fail("setDefaultValue() throws a LogicException if you give a default value which is not an array for a IS_ARRAY option");
        } catch (Exception e) {
            assertTrue("setDefaultValue() throws a LogicException if you give a default value which is not an array for a IS_ARRAY option", e instanceof LogicException);
            assertEquals("A default value for an array argument must be an array.", e.getMessage());
        }
    }
}
