/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.formatter.OutputFormatterStyle;
import org.nanocom.console.output.OutputInterface.OutputType;
import org.nanocom.console.output.OutputInterface.VerbosityLevel;

public class OutputTest {

    public OutputTest() {
    }

    @Test
    public void testConstructor() {
        TestOutput output = new TestOutput(VerbosityLevel.QUIET, true);
        assertEquals("Constructor takes the verbosity as its first argument", VerbosityLevel.QUIET, output.getVerbosity());
        assertTrue("Constructor takes the decorated flag as its second argument", output.isDecorated());
    }

    @Test
    public void testSetIsDecorated() {
        TestOutput output = new TestOutput();
        output.setDecorated(true);
        assertTrue("setDecorated() sets the decorated flag", output.isDecorated());
    }

    @Test
    public void testSetGetVerbosity() {
        TestOutput output = new TestOutput();
        output.setVerbosity(OutputInterface.VerbosityLevel.QUIET);
        assertEquals("setVerbosity() sets the verbosity", VerbosityLevel.QUIET, output.getVerbosity());
    }

    @Test
    public void testWrite() {
        OutputFormatterStyle fooStyle = new OutputFormatterStyle("yellow", "red", Arrays.asList("blink"));
        TestOutput output = new TestOutput(VerbosityLevel.QUIET);
        output.writeln("foo");
        assertEquals("writeln() outputs nothing if verbosity is set to VERBOSITY_QUIET", "", output.output);

        output = new TestOutput();
        output.writeln(Arrays.asList("foo", "bar"));
        assertEquals("writeln() can take an array of messages to output", "foo\nbar\n", output.output);

        output = new TestOutput();
        output.writeln("<info>foo</info>", OutputType.RAW);
        assertEquals("writeln() outputs the raw message if OUTPUT_RAW is specified", "<info>foo</info>\n", output.output);

        output = new TestOutput();
        output.writeln("<info>foo</info>", OutputType.PLAIN);
        assertEquals("writeln() strips decoration tags if OUTPUT_PLAIN is specified", "foo\n", output.output);

        output = new TestOutput();
        output.setDecorated(false);
        output.writeln("<info>foo</info>");
        assertEquals("writeln() strips decoration tags if decoration is set to false", "foo\n", output.output);

        output = new TestOutput();
        output.getFormatter().setStyle("foo", fooStyle);
        output.setDecorated(true);
        output.writeln("<foo>foo</foo>");
        assertEquals("writeln() decorates the output", "\033[33;41;5mfoo\033[0m\n", output.output);

        output.clear();
        output.write("<bar>foo</bar>");
        assertEquals("write() do nothing when a style does not exist", "<bar>foo</bar>", output.output);

        output.clear();
        output.writeln("<bar>foo</bar>");
        assertEquals("writeln() do nothing when a style does not exist", "<bar>foo</bar>\n", output.output);
    }

    class TestOutput extends Output {

        public TestOutput() {
            super();
        }

        public TestOutput(VerbosityLevel verbosity) {
            super(verbosity);
        }

        public TestOutput(VerbosityLevel verbosity, boolean decorated) {
            super(verbosity, decorated);
        }

        public String output = "";

        public void clear() {
            this.output = "";
        }

        @Override
        protected void doWrite(String message, boolean newline) {
            output += message + (newline ? "\n" : "");
        }
    }
}
