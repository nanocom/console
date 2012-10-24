/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class OutputFormatterStyleTest {

    public OutputFormatterStyleTest() {
    }

    @Test
    public void testConstructor() {
        OutputFormatterStyle style = new OutputFormatterStyle("green", "black", Arrays.asList("bold", "underscore"));
        assertEquals("\033[32;40;1;4mfoo\033[0m", style.apply("foo"));

        style = new OutputFormatterStyle("red", null, Arrays.asList("blink"));
        assertEquals("\033[31;5mfoo\033[0m", style.apply("foo"));

        style = new OutputFormatterStyle(null, "white");
        assertEquals("\033[47mfoo\033[0m", style.apply("foo"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testForeground() {
        OutputFormatterStyle style = new OutputFormatterStyle();

        style.setForeground("black");
        assertEquals("\033[30mfoo\033[0m", style.apply("foo"));

        style.setForeground("blue");
        assertEquals("\033[34mfoo\033[0m", style.apply("foo"));

        style.setForeground("undefined-color");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBackground() {
        OutputFormatterStyle style = new OutputFormatterStyle();

        style.setBackground("black");
        assertEquals("\033[40mfoo\033[0m", style.apply("foo"));

        style.setBackground("yellow");
        assertEquals("\033[43mfoo\033[0m", style.apply("foo"));

        style.setBackground("undefined-color");
    }

    @Test
    public void testOptions() {
        OutputFormatterStyle style = new OutputFormatterStyle();

        style.setOptions(new String[] {"reverse", "conceal"});
        assertEquals("\033[7;8mfoo\033[0m", style.apply("foo"));

        style.setOption("bold");
        assertEquals("\033[7;8;1mfoo\033[0m", style.apply("foo"));

        style.unsetOption("reverse");
        assertEquals("\033[8;1mfoo\033[0m", style.apply("foo"));

        style.setOption("bold");
        assertEquals("\033[8;1mfoo\033[0m", style.apply("foo"));

        style.setOptions(new String[] {"bold"});
        assertEquals("\033[1mfoo\033[0m", style.apply("foo"));
    }
}
