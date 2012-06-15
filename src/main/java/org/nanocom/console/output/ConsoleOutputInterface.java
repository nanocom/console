/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.output;

/**
 * ConsoleOutputInterface is the interface implemented by ConsoleOutput class.
 * This adds information about stderr output stream.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface ConsoleOutputInterface extends OutputInterface {

    public OutputInterface getErrorOutput();

    public void setErrorOutput(final OutputInterface error);

}
