/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

/**
 * Helper is the base class for all helper classes.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public abstract class Helper implements HelperInterface {

    protected HelperSet helperSet = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHelperSet(HelperSet helperSet) {
        this.helperSet = helperSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHelperSet() {
        setHelperSet(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelperSet getHelperSet() {
        return helperSet;
    }
}
