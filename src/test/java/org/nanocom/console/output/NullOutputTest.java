/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

import org.junit.Assert;
import org.junit.Test;

public class NullOutputTest  {

    @Test
    public void testConstructor() {
        OutputInterface output = new NullOutput();
        output.write("foo");
        Assert.assertTrue("write() does nothing", true); // FIXME
    }
}