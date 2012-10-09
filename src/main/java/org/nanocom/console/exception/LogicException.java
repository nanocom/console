/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.exception;

/**
 * An exception used for errors of logic.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class LogicException extends RuntimeException {

    static final long serialVersionUID = -7038497190745765339L;

    public LogicException(String message) {
        super(message);
    }
}
