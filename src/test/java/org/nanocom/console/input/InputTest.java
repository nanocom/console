/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class InputTest {

    public InputTest() {
    }

    @Test
    public void testConstructor() {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("name", "foo");
        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        assertEquals("Constructor takes a InputDefinition as an argument", "foo", input.getArgument("name"));
    }

    @Test
    public void testOptions() {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("--name", "foo");
        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputOption("name"))));
        assertEquals("getOption() returns the value for the given option", "foo", input.getOption("name"));

        input.setOption("name", "bar");
        foobar.clear();
        foobar.put("name", "bar");
        assertEquals("setOption() sets the value for a given option", "bar", input.getOption("name"));
        assertEquals("getOptions() returns all option values", foobar, input.getOptions());

        foobar.clear();
        foobar.put("--name", "foo");
        input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputOption("name"), new InputOption("bar", "", InputOption.VALUE_OPTIONAL, "", "default"))));
        assertEquals("getOption() returns the default value for optional options", "default", input.getOption("bar"));
        foobar.clear();
        foobar.put("name", "foo");
        foobar.put("bar", "default");
        assertEquals("getOptions() returns all option values, even optional ones", foobar, input.getOptions());

        try {
            input.setOption("foo", "bar");
            fail("setOption() throws an IllegalArgumentException if the option does not exist");
        } catch (Exception e) {
            assertTrue("setOption() throws an IllegalArgumentException if the option does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"foo\" option does not exist.", e.getMessage());
        }

        try {
            input.getOption("foo");
            fail("getOption() throws an IllegalArgumentException if the option does not exist");
        } catch (Exception e) {
            assertTrue("setOption() throws an IllegalArgumentException if the option does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"foo\" option does not exist.", e.getMessage());
        }
    }

    @Test
    public void testArguments() {
        Map<String, String> foobar = new HashMap<String, String>();
        foobar.put("name", "foo");

        ArrayInput input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        assertEquals("getArgument() returns the value for the given argument", "foo", input.getArgument("name"));

        input.setArgument("name", "bar");
        foobar = new HashMap<String, String>();
        foobar.put("name", "bar");
        assertEquals("setArgument() sets the value for a given argument", "bar", input.getArgument("name"));
        assertEquals("getArguments() returns all argument values", foobar, input.getArguments());

        foobar.clear();
        foobar.put("name", "foo");
        foobar.put("bar", "default");
        input = new ArrayInput(foobar, new InputDefinition(Arrays.asList((Object) new InputArgument("name"), new InputArgument("bar", InputArgument.OPTIONAL, "", "default"))));
        assertEquals("getArgument() returns the default value for optional arguments", "default", input.getArgument("bar"));
        assertEquals("getArguments() returns all argument values, even optional ones", foobar, input.getArguments());

        try {
            input.setArgument("foo", "bar");
            fail("setArgument() throws an IllegalArgumentException if the argument does not exist");
        } catch (Exception e) {
            assertTrue("setOption() throws an IllegalArgumentException if the option does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"foo\" argument does not exist.", e.getMessage());
        }

        try {
            input.getArgument("foo");
            fail("getArgument() throws an IllegalArgumentException if the argument does not exist");
        } catch (Exception e) {
            assertTrue("setOption() throws an IllegalArgumentException if the option does not exist", e instanceof IllegalArgumentException);
            assertEquals("The \"foo\" argument does not exist.", e.getMessage());
        }
    }

    @Test
    public void testValidate() {
        Map<String, String> foobar = new HashMap<String, String>();
        ArrayInput input = new ArrayInput(foobar);
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name", InputArgument.REQUIRED))));

        try {
            input.validate();
            fail("validate() throws a RuntimeException if not enough arguments are given");
        } catch (Exception e) {
            assertTrue("validate() throws a RuntimeException if not enough arguments are given", e instanceof RuntimeException);
            assertEquals("Not enough arguments.", e.getMessage());
        }

        foobar.put("name", "foo");
        input = new ArrayInput(foobar);
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name", InputArgument.REQUIRED))));

        try {
            input.validate();
        } catch (RuntimeException e) {
            fail("validate() does not throw a RuntimeException if enough arguments are given");
        }
    }

    @Test
    public void testSetGetInteractive() {
        ArrayInput input = new ArrayInput(new HashMap<String, String>());
        assertTrue("isInteractive() returns whether the input should be interactive or not", input.isInteractive());
        input.setInteractive(false);
        assertFalse("setInteractive() changes the interactive flag", input.isInteractive());
    }
}
