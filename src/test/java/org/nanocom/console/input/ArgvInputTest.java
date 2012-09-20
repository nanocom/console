/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;
import org.junit.Test;

public class ArgvInputTest {

    public ArgvInputTest() {
    }

    @Test
    public void testConstructor() throws NoSuchFieldException, IllegalAccessException {
        ArgvInput input = new ArgvInput(new String[]{"foo"});
        Class clazz = input.getClass();
        Field tokens = clazz.getDeclaredField("tokens");
        tokens.setAccessible(true);
        assertEquals("Constructor sets its input from the given args", Arrays.asList("foo"), tokens.get(input));
    }

    @Test
    public void testParser() {
        ArgvInput input = new ArgvInput(new String[]{"foo"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("name"))));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("name", "foo");
        assertEquals("parse() parses required arguments", foobar, input.getArguments());

        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("name"))));
        assertEquals("parse() is stateless", foobar, input.getArguments());

        input = new ArgvInput(new String[]{"--foo"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo"))));
        foobar.clear();
        foobar.put("foo", true);
        assertEquals("parse() parses long options without a value", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"--foo=bar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", "bar");
        assertEquals("parse() parses long options with a required value (with a = separator)", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"--foo", "bar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        assertEquals("parse() parses long options with a required value (with a space separator)", foobar, input.getOptions());

        try {
            input = new ArgvInput(new String[]{"--foo"});
            input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
            fail("parse() throws a RuntimeException if no value is passed to an option when it is required");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if no value is passed to an option when it is required", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if no value is passed to an option when it is required", "The \"--foo\" option requires a value.", e.getMessage());
        }

        input = new ArgvInput(new String[]{"-f"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"))));
        foobar.clear();
        foobar.put("foo", true);
        assertEquals("parse() parses short options without a value", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-fbar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", "bar");
        assertEquals("parse() parses short options with a required value (with no separator)", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-f", "bar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        assertEquals("parse() parses short options with a required value (with a space separator)", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-f", "-b", "foo"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("name"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL), new InputOption("bar", "b"))));
        foobar.clear();
        foobar.put("foo", null);
        foobar.put("bar", true);
        assertEquals("parse() parses short options with an optional value which is not present", foobar, input.getOptions());

        try {
            input = new ArgvInput(new String[]{"-f"});
            input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
            fail("parse() throws a RuntimeException if no value is passed to an option when it is required");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if no value is passed to an option when it is required", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if no value is passed to an option when it is required", "The \"--foo\" option requires a value.", e.getMessage());
        }

        try {
            input = new ArgvInput(new String[]{"-ffoo"});
            input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_NONE))));
            fail("parse() throws a RuntimeException if a value is passed to an option which does not take one");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if a value is passed to an option which does not take one", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if a value is passed to an option which does not take one", "The \"-o\" option does not exist.", e.getMessage());
        }

        try {
            input = new ArgvInput(new String[]{"foo", "bar"});
            input.bind(new InputDefinition());
            fail("parse() throws a RuntimeException if too many arguments are passed");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if too many arguments are passed", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if too many arguments are passed", "Too many arguments.", e.getMessage());
        }

        try {
            input = new ArgvInput(new String[]{"--foo"});
            input.bind(new InputDefinition());
            fail("parse() throws a RuntimeException if an unknown long option is passed");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if an unknown long option is passed", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if an unknown long option is passed", "The \"--foo\" option does not exist.", e.getMessage());
        }

        try {
            input = new ArgvInput(new String[]{"-f"});
            input.bind(new InputDefinition());
            fail("parse() throws a RuntimeException if an unknown short option is passed");
        } catch (Exception e) {
            assertTrue("parse() throws a RuntimeException if an unknown short option is passed", e instanceof RuntimeException);
            assertEquals("parse() throws a RuntimeException if an unknown short option is passed", "The \"-f\" option does not exist.", e.getMessage());
        }

        input = new ArgvInput(new String[]{"-fb"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"), new InputOption("bar", "b"))));
        foobar.clear();
        foobar.put("foo", true);
        foobar.put("bar", true);
        assertEquals("parse() parses short options when they are aggregated as a single one", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-fb", "bar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", true);
        foobar.put("bar", "bar");
        assertEquals("parse() parses short options when they are aggregated as a single one and the last one has a required value", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-fb", "bar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        assertEquals("parse() parses short options when they are aggregated as a single one and the last one has an optional value", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-fbbar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        assertEquals("parse() parses short options when they are aggregated as a single one and the last one has an optional value with no separator", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-fbbar"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("foo", "f", InputOption.VALUE_OPTIONAL), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        foobar.clear();
        foobar.put("foo", "bbar");
        foobar.put("bar", null);
        assertEquals("parse() parses short options when they are aggregated as a single one and one of them takes a value", foobar, input.getOptions());

        try {
            input = new ArgvInput(new String[]{"foo", "bar", "baz", "bat"});
            input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("name", InputArgument.IS_ARRAY))));
            foobar.clear();
            foobar.put("name", Arrays.asList("foo", "bar", "baz", "bat"));
            assertEquals("parse() parses array arguments", foobar, input.getArguments());
        } catch (Exception e) {
            assertTrue("parse() parses array arguments", !"Too many arguments.".equals(e.getMessage()));
        }

        input = new ArgvInput(new String[]{"--name=foo", "--name=bar", "--name=baz"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputOption("name", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY))));
        foobar.clear();
        foobar.put("name", Arrays.asList("foo", "bar", "baz"));
        assertEquals(foobar, input.getOptions());

        try {
            input = new ArgvInput(new String[]{"-1"});
            input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("number"))));
            fail("parse() throws a RuntimeException if an unknown option is passed");
        } catch (Exception e) {
            assertTrue("parse() parses arguments with leading dashes as options without having encountered a double-dash sequence", e instanceof RuntimeException);
            assertEquals("parse() parses arguments with leading dashes as options without having encountered a double-dash sequence", "The \"-1\" option does not exist.", e.getMessage());
        }

        input = new ArgvInput(new String[]{"--", "-1"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("number"))));
        foobar.clear();
        foobar.put("number", "-1");
        assertEquals("parse() parses arguments with leading dashes as arguments after having encountered a double-dash sequence", foobar, input.getArguments());

        input = new ArgvInput(new String[]{"-f", "bar", "--", "-1"});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("number"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL))));
        assertEquals("parse() parses arguments with leading dashes as arguments after having encountered a double-dash sequence", foobar, input.getArguments());
        foobar.clear();
        foobar.put("foo", "bar");
        assertEquals("parse() parses arguments with leading dashes as options before having encountered a double-dash sequence", foobar, input.getOptions());

        input = new ArgvInput(new String[]{"-f", "bar", EMPTY});
        input.bind(new InputDefinition(Arrays.<Object>asList(new InputArgument("empty"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL))));
        foobar.clear();
        foobar.put("empty", EMPTY);
        assertEquals("parse() parses empty string arguments", foobar, input.getArguments());
    }

    @Test
    public void testGetFirstArgument() {
        ArgvInput input = new ArgvInput(new String[]{"-fbbar"});
        assertEquals("getFirstArgument() returns the first argument from the raw input", null, input.getFirstArgument());

        input = new ArgvInput(new String[]{"-fbbar", "foo"});
        assertEquals("getFirstArgument() returns the first argument from the raw input", "foo", input.getFirstArgument());
    }

    @Test
    public void testHasParameterOption() {
        ArgvInput input = new ArgvInput(new String[]{"-f", "foo"});
        assertTrue("hasParameterOption() returns true if the given short option is in the raw input", input.hasParameterOption("-f"));

        input = new ArgvInput(new String[]{"--foo", "foo"});
        assertTrue("hasParameterOption() returns true if the given short option is in the raw input", input.hasParameterOption("--foo"));

        input = new ArgvInput(new String[]{"foo"});
        assertFalse("hasParameterOption() returns false if the given short option is not in the raw input", input.hasParameterOption("--foo"));
    }
}
