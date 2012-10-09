/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.junit.Assert.*;
import org.junit.Test;
import org.nanocom.console.output.InMemoryOutput;

public class ProgressHelperTest {

    public ProgressHelperTest() {
    }

    @Test
    public void testAdvance() {
        ProgressHelper progress = new ProgressHelper();
        InMemoryOutput output = new InMemoryOutput();
        progress.start(output);
        progress.advance();

        assertEquals(generateOutput("    1 [.--------------------------]"), output.getBuffer().toString());
    }

    @Test
    public void testAdvanceWithStep() {
        ProgressHelper progress = new ProgressHelper();
        InMemoryOutput output = new InMemoryOutput();
        progress.start(output);
        progress.advance(5);

        assertEquals(generateOutput("    5 [----.----------------------]"), output.getBuffer().toString());
    }

    @Test
    public void testAdvanceMultipleTimes() {
        ProgressHelper progress = new ProgressHelper();
        InMemoryOutput output = new InMemoryOutput();
        progress.start(output);
        progress.advance(3);
        progress.advance(2);

        assertEquals(generateOutput("    3 [--.------------------------]") + generateOutput("    5 [----.----------------------]"), output.getBuffer().toString());
    }

    @Test
    public void testCustomizations() {
        ProgressHelper progress = new ProgressHelper();
        progress.setBarWidth(10);
        progress.setBarCharacter('_');
        progress.setEmptyBarCharacter(' ');
        progress.setProgressCharacter("/");
        progress.setFormat(" %current%/%max% [%bar%] %percent%%");
        InMemoryOutput output = new InMemoryOutput();
        progress.start(output, 10);
        progress.advance();

        assertEquals(generateOutput("  1/10 [_/        ]  10%"), output.getBuffer().toString());
    }

    protected InMemoryOutput getOutputStream() {
        return new InMemoryOutput();
    }

    protected String generateOutput(String expected) {
        return repeat(String.valueOf(0x08), 80) + expected + repeat(" ", 80 - expected.length()) + repeat(String.valueOf(0x08), 80 - expected.length());
    }
}
