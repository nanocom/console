/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import static org.junit.Assert.*;
import org.junit.Test;

public class OutputFormatterStyleStackTest {

    public OutputFormatterStyleStackTest() {
    }

    @Test
    public void testPush() {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        OutputFormatterStyle s1 = new OutputFormatterStyle("white", "black");
        stack.push(s1);
        OutputFormatterStyle s2 = new OutputFormatterStyle("yellow", "blue");
        stack.push(s2);

        assertEquals(s2, stack.getCurrent());

        OutputFormatterStyle s3 = new OutputFormatterStyle("green", "red");
        stack.push(s3);

        assertEquals(s3, stack.getCurrent());
    }

    @Test
    public void testPop() {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        OutputFormatterStyle s1 = new OutputFormatterStyle("white", "black");
        stack.push(s1);
        OutputFormatterStyle s2 = new OutputFormatterStyle("yellow", "blue");
        stack.push(s2);

        assertEquals(s2, stack.pop());
        assertEquals(s1, stack.pop());
    }

    @Test
    public void testPopEmpty() {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        OutputFormatterStyle style = new OutputFormatterStyle();

        assertEquals(style, stack.pop());
    }

    @Test
    public void testPopNotLast() {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        OutputFormatterStyle s1 = new OutputFormatterStyle("white", "black");
        stack.push(s1);;
        OutputFormatterStyle s2 = new OutputFormatterStyle("yellow", "blue");
        stack.push(s2);
        OutputFormatterStyle s3 = new OutputFormatterStyle("green", "red");
        stack.push(s3);

        assertEquals(s2, stack.pop(s2));
        assertEquals(s1, stack.pop());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInvalidPop() {
        OutputFormatterStyleStack stack = new OutputFormatterStyleStack();
        stack.push(new OutputFormatterStyle("white", "black"));
        stack.pop(new OutputFormatterStyle("yellow", "blue"));
    }
}
