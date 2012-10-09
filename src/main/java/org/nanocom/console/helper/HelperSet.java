/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.nanocom.console.command.Command;

/**
 * HelperSet represents a set of helpers to be used with a command.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class HelperSet {

    private Map<String, HelperInterface> helpers;
    private Command command;

    /**
     * Constructor.
     *
     * @param helpers A map of helpers
     */
    public HelperSet(Map<String, Helper> helpers) {
        this.helpers = new HashMap<String, HelperInterface>();
        for (Entry<String, Helper> helper : helpers.entrySet()) {
            set(helper.getValue(), helper.getKey());
        }
    }

    /**
     * Constructor.
     *
     * @param helpers A list of helpers
     */
    public HelperSet(List<Helper> helpers) {
        this.helpers = new HashMap<String, HelperInterface>();
        for (Helper helper : helpers) {
            set(helper, null);
        }
    }

    /**
     * Constructor.
     *
     * @param helpers A list of helpers
     */
    public HelperSet() {
        this(new ArrayList<Helper>());
    }

    /**
     * Sets a helper.
     *
     * @param helper The helper instance
     * @param alias  An alias
     */
    public final void set(HelperInterface helper, String alias) {
        helpers.put(helper.getName(), helper);
        if (null != alias) {
            helpers.put(alias, helper);
        }

        helper.setHelperSet(this);
    }

    /**
     * Sets a helper.
     *
     * @param helper The helper instance
     */
    public final void set(HelperInterface helper) {
        set(helper, null);
    }

    /**
     * Returns true if the helper if defined.
     *
     * @param name The helper name
     *
     * @return True if the helper is defined, false otherwise
     */
    public boolean has(String name) {
        return helpers.containsKey(name);
    }

    /**
     * Gets a helper value.
     *
     * @param name The helper name
     *
     * @return The helper instance
     *
     * @throws IllegalArgumentException if the helper is not defined
     */
    public HelperInterface get(String name) {
        if (!has(name)) {
            throw new IllegalArgumentException(String.format("The helper \"%s\" is not defined.", name));
        }

        return helpers.get(name);
    }

    /**
     * Sets the command associated with this helper set.
     *
     * @param command A Command instance
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Sets to null the command associated with this helper set.
     */
    public void setCommand() {
        setCommand(null);
    }

    /**
     * Gets the command associated with this helper set.
     *
     * @return A Command instance
     */
    public Command getCommand() {
        return command;
    }
}
