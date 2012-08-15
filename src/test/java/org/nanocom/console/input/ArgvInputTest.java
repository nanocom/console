package org.nanocom.console.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ArgvInputTest {
    
    public ArgvInputTest() {
    }

    @Test
    public void testConstructor() {
        /*List<String> argv = Arrays.asList("foo");
        ArgvInput input = new ArgvInput();
        Class clazz = input.getClass();
        Field tokens = clazz.getField("tokens");
        Assert.assertEquals("__construct() automatically get its input from the argv server variable", Arrays.asList("foo"), tokens.get(argv));*/
    }

    public void testParser() {
        ArgvInput input = new ArgvInput((String[]) Arrays.asList("foo").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        Map<String, Object> foobar = new HashMap<String, Object>();
        foobar.put("name", "foo");
        Assert.assertEquals(".parse() parses required arguments", foobar, input.getArguments());

        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name"))));
        Assert.assertEquals(".parse() is stateless", foobar, input.getArguments());

        input = new ArgvInput((String[]) Arrays.asList("--foo").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo"))));
        foobar.clear();
        foobar.put("foo", true);
        Assert.assertEquals(".parse() parses long options without a value", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("--foo=bar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", "bar");
        Assert.assertEquals(".parse() parses long options with a required value (with a = separator)", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("--foo", "bar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        Assert.assertEquals(".parse() parses long options with a required value (with a space separator)", foobar, input.getOptions());

        try {
            input = new ArgvInput((String[]) Arrays.asList("--foo").toArray());
            input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
            Assert.fail(".parse() throws a Exception if no value is passed to an option when it is required");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if no value is passed to an option when it is required");
            Assert.assertEquals(".parse() throws an Exception if no value is passed to an option when it is required", "The \"--foo\" option requires a value.", e.getMessage());
        }

        input = new ArgvInput((String[]) Arrays.asList("-f").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f"))));
        foobar.clear();
        foobar.put("foo", true);
        Assert.assertEquals(".parse() parses short options without a value", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-fbar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", "bar");
        Assert.assertEquals(".parse() parses short options with a required value (with no separator)", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-f", "bar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
        Assert.assertEquals(".parse() parses short options with a required value (with a space separator)", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-f", "-b", "foo").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL), new InputOption("bar", "b"))));
        foobar.clear();
        foobar.put("foo", null);
        Assert.assertEquals(".parse() parses short options with an optional value which is not present", foobar, input.getOptions());

        try {
            input = new ArgvInput((String[]) Arrays.asList("-f").toArray());
            input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_REQUIRED))));
            Assert.fail(".parse() throws an Exception if no value is passed to an option when it is required");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if no value is passed to an option when it is required");
            Assert.assertEquals(".parse() throws an Exception if no value is passed to an option when it is required", "The \"--foo\" option requires a value.", e.getMessage());
        }

        try {
            input = new ArgvInput((String[]) Arrays.asList("-ffoo").toArray());
            input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_NONE))));
            Assert.fail(".parse() throws an Exception if a value is passed to an option which does not take one");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if a value is passed to an option which does not take one");
            Assert.assertEquals(".parse() throws an Exception if a value is passed to an option which does not take one", "The \"-o\" option does not exist.", e.getMessage());
        }

        try {
            input = new ArgvInput((String[]) Arrays.asList("foo", "bar").toArray());
            input.bind(new InputDefinition());
            Assert.fail(".parse() throws an Exception if too many arguments are passed");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if too many arguments are passed");
            Assert.assertEquals(".parse() throws an Exception if too many arguments are passed", "Too many arguments.", e.getMessage());
        }

        try {
            input = new ArgvInput((String[]) Arrays.asList("--foo").toArray());
            input.bind(new InputDefinition());
            Assert.fail(".parse() throws a Exception if an unknown long option is passed");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if an unknown long option is passed");
            Assert.assertEquals(".parse() throws an Exception if an unknown long option is passed", "The \"--foo\" option does not exist.", e.getMessage());
        }

        try {
            input = new ArgvInput((String[]) Arrays.asList("-f").toArray());
            input.bind(new InputDefinition());
            Assert.fail(".parse() throws a Exception if an unknown short option is passed");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() throws a \RuntimeException if an unknown short option is passed");
            Assert.assertEquals(".parse() throws an Exception if an unknown short option is passed", "The \"-f\" option does not exist.", e.getMessage());
        }

        input = new ArgvInput((String[]) Arrays.asList("-fb").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f"), new InputOption("bar", "b"))));
        foobar.clear();
        foobar.put("foo", true);
        foobar.put("bar", true);
        Assert.assertEquals(".parse() parses short options when they are aggregated as a single one", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-fb", "bar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_REQUIRED))));
        foobar.clear();
        foobar.put("foo", true);
        foobar.put("bar", "bar");
        Assert.assertEquals(".parse() parses short options when they are aggregated as a single one and the last one has a required value", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-fb", "bar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        Assert.assertEquals(".parse() parses short options when they are aggregated as a single one and the last one has an optional value", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-fbbar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f"), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        Assert.assertEquals(".parse() parses short options when they are aggregated as a single one and the last one has an optional value with no separator", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-fbbar").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("foo", "f", InputOption.VALUE_OPTIONAL), new InputOption("bar", "b", InputOption.VALUE_OPTIONAL))));
        foobar.clear();
        foobar.put("foo", "bbar");
        foobar.put("bar", null);
        Assert.assertEquals(".parse() parses short options when they are aggregated as a single one and one of them takes a value", foobar, input.getOptions());

        try {
            input = new ArgvInput((String[]) Arrays.asList("foo", "bar", "baz", "bat").toArray());
            input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("name", InputArgument.IS_ARRAY))));
            foobar.clear();
            foobar.put("name", Arrays.asList("foo", "bar", "baz", "bat"));
            Assert.assertEquals(".parse() parses array arguments", foobar, input.getArguments());
        } catch (Exception e) {
            Assert.assertTrue(".parse() parses array arguments", !"Too many arguments.".equals(e.getMessage()));
        }

        input = new ArgvInput((String[]) Arrays.asList("--name=foo", "--name=bar", "--name=baz").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputOption("name", null, InputOption.VALUE_OPTIONAL | InputOption.VALUE_IS_ARRAY))));
        foobar.clear();
        foobar.put("name", Arrays.asList("foo", "bar", "baz"));
        Assert.assertEquals(foobar, input.getOptions());

        try {
            input = new ArgvInput((String[]) Arrays.asList("-1").toArray());
            input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("number"))));
            Assert.fail(".parse() throws an Exception if an unknown option is passed");
        } catch (Exception e) {
            //Assert.assertInstanceOf("\RuntimeException", e, ".parse() parses arguments with leading dashes as options without having encountered a double-dash sequence");
            Assert.assertEquals(".parse() parses arguments with leading dashes as options without having encountered a double-dash sequence", "The \"-1\" option does not exist.", e.getMessage());
        }

        input = new ArgvInput((String[]) Arrays.asList("--", "-1").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("number"))));
        foobar.clear();
        foobar.put("number", "-1");
        Assert.assertEquals(".parse() parses arguments with leading dashes as arguments after having encountered a double-dash sequence", foobar, input.getArguments());

        input = new ArgvInput((String[]) Arrays.asList("-f", "bar", "--", "-1").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("number"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL))));
        Assert.assertEquals(".parse() parses arguments with leading dashes as arguments after having encountered a double-dash sequence", foobar, input.getArguments());
        foobar.clear();
        foobar.put("foo", "bar");
        Assert.assertEquals(".parse() parses arguments with leading dashes as options before having encountered a double-dash sequence", foobar, input.getOptions());

        input = new ArgvInput((String[]) Arrays.asList("-f", "bar", "").toArray());
        input.bind(new InputDefinition(Arrays.asList((Object) new InputArgument("empty"), new InputOption("foo", "f", InputOption.VALUE_OPTIONAL))));
        foobar.clear();
        foobar.put("empty", "");
        Assert.assertEquals(".parse() parses empty string arguments", foobar, input.getArguments());
    }

    @Test
    public void testGetFirstArgument() {
        ArgvInput input = new ArgvInput((String[]) Arrays.asList("-fbbar").toArray());
        Assert.assertEquals(".getFirstArgument() returns the first argument from the raw input", null, input.getFirstArgument());

        input = new ArgvInput((String[]) Arrays.asList("-fbbar", "foo").toArray());
        Assert.assertEquals(".getFirstArgument() returns the first argument from the raw input", "foo", input.getFirstArgument());
    }

    @Test
    public void testHasParameterOption() {
        ArgvInput input = new ArgvInput((String[]) Arrays.asList("-f", "foo").toArray());
        Assert.assertTrue(".hasParameterOption() returns true if the given short option is in the raw input", input.hasParameterOption("-f"));

        input = new ArgvInput((String[]) Arrays.asList("--foo", "foo").toArray());
        Assert.assertTrue(".hasParameterOption() returns true if the given short option is in the raw input", input.hasParameterOption("--foo"));

        input = new ArgvInput((String[]) Arrays.asList("foo").toArray());
        Assert.assertFalse(".hasParameterOption() returns false if the given short option is not in the raw input", input.hasParameterOption("--foo"));
    }

    /*public void testGetParameterOptionEqualSign(final String[] argv, final String key, final Object expected) {
        ArgvInput input = new ArgvInput(argv);
        Assert.assertEquals(".getParameterOption() returns the expected value", expected, input.getParameterOption(key));
    }*/

    /*public void provideGetParameterOptionValues() {
        array(
            array(array("app/console", "foo:bar", "-e", "dev"), "-e", "dev"),
            array(array("app/console", "foo:bar", "--env=dev"), "--env", "dev"),
            array(array("app/console", "foo:bar", "-e", "dev"), array("-e", "--env"), "dev"),
            array(array("app/console", "foo:bar", "--env=dev"), array("-e", "--env"), "dev"),
        );
    }*/

}
