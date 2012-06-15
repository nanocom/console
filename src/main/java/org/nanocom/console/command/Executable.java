/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.command;

import org.nanocom.console.input.InputInterface;
import org.nanocom.console.output.OutputInterface;

/**
 * Base class to inject executable code inside a command.
 * 
 * It simulates a closure.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public abstract class Executable {

	/**
     * Executes a command.
     */
	protected abstract int execute(InputInterface input, OutputInterface output) throws Exception;

}