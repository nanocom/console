/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.junit.Assert;
import org.junit.Test;

public class ConsoleOutputTest {

    @Test
    public void testConstructor() {
        OutputInterface output = new ConsoleOutput(Output.VerbosityLevel.QUIET, true);
        Assert.assertEquals("constructor takes the verbosity as its first argument", Output.VerbosityLevel.QUIET, output.getVerbosity());
    }
}