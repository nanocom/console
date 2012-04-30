package com.nanocom.console.helper;

/**
 * HelperInterface is the interface all helpers must implement.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface HelperInterface {

    /**
     * Sets the helper set associated with this helper.
     *
     * @param helperSet A HelperSet instance
     */
    void setHelperSet(final HelperSet helperSet);

    void setHelperSet();

    /**
     * Gets the helper set associated with this helper.
     *
     * @return A HelperSet instance
     */
    HelperSet getHelperSet();

    /**
     * Returns the canonical name of this helper.
     *
     * @return The canonical name
     */
    String getName();

}
