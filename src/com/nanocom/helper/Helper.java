package com.nanocom.helper;

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

    /**
     * Gets the helper set associated with this helper.
     *
     * @return A HelperSet instance
     */
    @Override
    public HelperSet getHelperSet() {
        return helperSet;
    }

}
