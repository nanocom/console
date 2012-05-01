/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.helper;

/**
 * Helper is the base class for all helper classes.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public abstract class Helper implements HelperInterface {

    protected HelperSet helperSet = null;

    /**
     * Sets the helper set associated with this helper.
     *
     * @param helperSet A HelperSet instance
     */
    @Override
    public void setHelperSet(HelperSet helperSet) {
        this.helperSet = helperSet;
    }

    @Override
    public void setHelperSet() {
        setHelperSet(null);
    }

    /**
     * Gets the helper set associated with this helper.
     *
     * @return A HelperSet instance
     */
    @Override
    public final HelperSet getHelperSet() {
        return helperSet;
    }

}
