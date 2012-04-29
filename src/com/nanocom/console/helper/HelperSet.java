package com.nanocom.console.helper;

import com.nanocom.console.command.Command;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HelperSet represents a set of helpers to be used with a command.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class HelperSet {

    private Map<String, HelperInterface> helpers;
    private Command command;

    /**
     * @param helpers An array of helper.
     */
    public HelperSet(Map<String, Helper> helpers) {
        this.helpers = new HashMap<String, HelperInterface>();
        for (Entry<String, Helper> helper : helpers.entrySet()) {
            set(helper.getValue(), helper.getKey());
        }
    }

    /**
     * Sets a helper.
     *
     * @param helper The helper instance
     * @param alias  An alias
     */
    public final void set(HelperInterface helper, final String alias) {
        helpers.put(helper.getName(), helper);
        if (null != alias) {
            helpers.put(alias, helper);
        }

        helper.setHelperSet(this);
    }

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
    public boolean has(final String name) {
        return helpers.containsKey(name);
    }

    /**
     * Gets a helper value.
     *
     * @param name The helper name
     *
     * @return The helper instance
     *
     * @throws Exception if the helper is not defined
     */
    public HelperInterface get(final String name) throws Exception {
        if (!has(name)) {
            throw new Exception("The helper \"" + name + "\" is not defined.");
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
     * Gets the command associated with this helper set.
     *
     * @return A Command instance
     */
    public Command getCommand() {
        return command;
    }

}
