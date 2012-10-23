/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

/**
 * NullOutput suppresses all output.
 *
 *     NullOutput output = new NullOutput();
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class NullOutput extends Output {

    public NullOutput() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doWrite(String message, boolean newline) {
        // Do nothing
    }
}
