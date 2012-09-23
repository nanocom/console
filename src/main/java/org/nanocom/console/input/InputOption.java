/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nanocom.console.exception.LogicException;

/**
 * Represents a command line option.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class InputOption {

    public static final int VALUE_NONE     = 1;
    public static final int VALUE_REQUIRED = 2;
    public static final int VALUE_OPTIONAL = 4;
    public static final int VALUE_IS_ARRAY = 8;

    private String  name;
    private String  shortcut;
    private Integer mode;
    private Object  defaultValue;
    private String  description;

    /**
     * @param name         The option name
     * @param shortcut     The shortcut (can be null)
     * @param mode         The option mode: One of the VALUE_* constants
     * @param description  A description text
     * @param defaultValue The default value (must be null for VALUE_REQUIRED or VALUE_NONE)
     *
     * @throws IllegalArgumentException If option mode is invalid or incompatible
     */
    public InputOption(String name, String shortcut, int mode, String description, Object defaultValue) {
        init(name, shortcut, mode, description, defaultValue);
    }

    public InputOption(String name, String shortcut, int mode, String description) {
        this(name, shortcut, mode, description, null);
    }

    public InputOption(String name, String shortcut, int mode) {
        this(name, shortcut, mode, "");
    }

    public InputOption(String name, String shortcut) {
        this(name, shortcut, VALUE_NONE);
    }

    public InputOption(String name) {
        this(name, null);
    }

    private void init(String name, String shortcut, int mode, String description, Object defaultValue) {
        if (null != name) {
            if (name.startsWith("--")) {
                name = name.substring(2);
            }
        }

        if (null != shortcut) {
            if (shortcut.isEmpty()) {
                shortcut = null;
            } else if ('-' == shortcut.charAt(0)) {
                shortcut = shortcut.substring(1);
                if (shortcut.isEmpty()) {
                    shortcut = null;
                }
            }
        }

        if (mode > 15 || mode < 1) {
            throw new IllegalArgumentException(String.format("Option mode \"%d\" is not valid.", mode));
        }

        this.name        = name;
        this.shortcut    = shortcut;
        this.mode        = mode;
        this.description = description;

        if (isArray() && !acceptValue()) {
            throw new IllegalArgumentException("Impossible to have an option mode VALUE_IS_ARRAY if the option does not accept a value.");
        }

        setDefaultValue(defaultValue);
    }

    /**
     * Returns the option shortcut.
     *
     * @return The shortcut
     */
    public String getShortcut() {
        return shortcut;
    }

    /**
     * Returns the option name.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if the option accepts a value.
     *
     * @return True if value mode is not VALUE_NONE, false otherwise
     */
    public boolean acceptValue() {
        return isValueRequired() || isValueOptional();
    }

    /**
     * Returns true if the option requires a value.
     *
     * @return True if value mode is VALUE_REQUIRED, false otherwise
     */
    public boolean isValueRequired() {
        return VALUE_REQUIRED == (VALUE_REQUIRED & mode);
    }

    /**
     * Returns true if the option takes an optional value.
     *
     * @return True if value mode is VALUE_OPTIONAL, false otherwise
     */
    public boolean isValueOptional() {
        return VALUE_OPTIONAL == (VALUE_OPTIONAL & mode);
    }

    /**
     * Returns true if the option can take multiple values.
     *
     * @return True if mode is VALUE_IS_ARRAY, false otherwise
     */
    public boolean isArray() {
        return VALUE_IS_ARRAY == (VALUE_IS_ARRAY & mode);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue The default value
     *
     * @throws LogicException When incorrect default value is given
     */
    public void setDefaultValue(Object defaultValue) {
        if (VALUE_NONE == (VALUE_NONE & mode) && null != defaultValue) {
            throw new LogicException("Cannot set a default value when using Option.VALUE_NONE mode.");
        }

        if (isArray()) {
            if (null == defaultValue) {
                defaultValue = new ArrayList<Object>();
            } else if (!(defaultValue instanceof List || defaultValue instanceof Map)) {
                throw new LogicException("A default value for an array option must be an array.");
            }
        }

        this.defaultValue = acceptValue() ? defaultValue : false;
    }

    /**
     * Returns the default value.
     *
     * @return The default value
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the description text.
     *
     * @return The description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether the given option equals this one.
     *
     * @param option Option to compare
     *
     * @return
     */
    public boolean equals(InputOption option) {
        return (option.getName() == null ? getName() == null : option.getName().equals(getName()))
            && (option.getShortcut() == null ? getShortcut() == null : option.getShortcut().equals(getShortcut()))
            && option.getDefaultValue() == getDefaultValue()
            && option.isArray() == isArray()
            && option.isValueRequired() == isValueRequired()
            && option.isValueOptional() == isValueOptional()
        ;
    }
}
