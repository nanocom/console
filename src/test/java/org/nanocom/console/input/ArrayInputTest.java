/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayInputTest {

    @Test
    public void testGetFirstArgument() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        InputInterface input = new ArrayInput(map);
        assertNull("getFirstArgument() returns null if no argument were passed", input.getFirstArgument());
        map.put("name", "Arnaud");
        input = new ArrayInput(map);
        assertEquals("getFirstArgument() returns the first passed argument", "Arnaud", input.getFirstArgument());
        map.clear();
        map.put("--foo", "bar");
        map.put("name", "Arnaud");
        input = new ArrayInput(map);
        assertEquals("getFirstArgument() returns the first passed argument", "Arnaud", input.getFirstArgument());
    }

    @Test
    public void testHasParameterOption() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "Arnaud");
        map.put("--foo", "bar");
        InputInterface input = new ArrayInput(map);
        assertTrue("hasParameterOption() returns true if an option is present in the passed parameters", input.hasParameterOption("--foo"));
        assertFalse("hasParameterOption() returns false if an option is not present in the passed parameters", input.hasParameterOption("--bar"));

        map.clear();
        map.put("--foo", null);
        input = new ArrayInput(map);
        assertTrue("hasParameterOption() returns true if an option is present in the passed parameters", input.hasParameterOption("--foo"));
    }

    @Test
    public void testParse() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "foo");
        InputInterface input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputArgument("name"))));
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "foo");
        assertEquals("parse() parses required arguments", map2, input.getArguments());

        try {
            map.clear();
            map.put("foo", "foo");
            input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputArgument("name"))));
            fail("parse() throws an IllegalArgumentException exception if an invalid argument is passed");
        } catch (Exception e) {
            assertTrue("parse() throws an IllegalArgumentException exception if an invalid argument is passed", e instanceof IllegalArgumentException);
            assertEquals("parse() throws an IllegalArgumentException exception if an invalid argument is passed", "The \"foo\" argument does not exist.", e.getMessage());
        }

        map.clear();
        map.put("--foo", "bar");
        input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputOption("foo"))));
        map2.clear();
        map2.put("foo", "bar");
        assertEquals("parse() parses long options", map2, input.getOptions());

        input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "", "default"))));
        assertEquals("parse() parses long options with a default value", map2, input.getOptions());

        map.clear();
        map.put("--foo", null);
        input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_OPTIONAL, "", "default"))));
        map2.clear();
        map2.put("foo", "default");
        assertEquals("parse() parses long options with a default value", map2, input.getOptions());

        try {
            input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
            fail("parse() throws an IllegalArgumentException exception if a required option is passed without a value");
        } catch (Exception e) {
            assertTrue("parse() throws an IllegalArgumentException exception if a required option is passed without a value", e instanceof IllegalArgumentException);
            assertEquals("parse() throws an IllegalArgumentException exception if a required option is passed without a value", "The \"--foo\" option requires a value.", e.getMessage());
        }

        try {
            map.clear();
            map.put("--foo", "foo");
            input = new ArrayInput(map, new InputDefinition());
            fail("parse() throws an IllegalArgumentException exception if an invalid option is passed");
        } catch (Exception e) {
            assertTrue("parse() throws an IllegalArgumentException exception if an invalid option is passed", e instanceof IllegalArgumentException);
            assertEquals("parse() throws an IllegalArgumentException exception if an invalid option is passed", "The \"--foo\" option does not exist.", e.getMessage());
        }

        map.clear();
        map.put("-f", "bar");
        input = new ArrayInput(map, new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"))));
        map2.clear();
        map2.put("foo", "bar");
        assertEquals("parse() parses short options", map2, input.getOptions());

        map.clear();
        map.put("-o", "foo");
        try {
            input = new ArrayInput(map, new InputDefinition());
            fail("parse() throws an IllegalArgumentException exception if an invalid option is passed");
        } catch (Exception e) {
            assertTrue("parse() throws an IllegalArgumentException exception if an invalid option is passed", e instanceof IllegalArgumentException);
            assertEquals("parse() throws an IllegalArgumentException exception if an invalid option is passed", "The \"-o\" option does not exist.", e.getMessage());
        }
    }
}
