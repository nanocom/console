package com.nanocom.input;

import java.util.ArrayList;

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
     * Constructor.
     *
     * @param name         The option name
     * @param shortcut     The shortcut (can be null)
     * @param mode         The option mode: One of the VALUE_* constants
     * @param description  A description text
     * @param defaultValue The default value (must be null for VALUE_REQUIRED or VALUE_NONE)
     *
     * @throws Exception If option mode is invalid or incompatible, or if name is null
     */
    public InputOption(String name, String shortcut, Integer mode, final String description, final Object defaultValue) throws Exception {
        init(name, shortcut, mode, description, defaultValue);
    }

    public InputOption(String name, String shortcut, Integer mode, final String description) throws Exception {
        init(name, shortcut, mode, description, null);
    }

    public InputOption(String name, String shortcut, Integer mode) throws Exception {
        init(name, shortcut, mode, "", null);
    }

    public InputOption(String name, String shortcut) throws Exception {
        init(name, shortcut, null, "", null);
    }
    
    public InputOption(String name) throws Exception {
        init(name, null, null, "", null);
    }

    private void init(String name, String shortcut, Integer mode, final String description, final Object defaultValue) throws Exception {
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

        if (null == mode) {
            mode = VALUE_NONE;
        } else if (mode > 15 || mode < 1) {
            throw new Exception("Option mode \"" + mode + "\" is not valid.");
        }

        this.name        = name;
        this.shortcut    = shortcut;
        this.mode        = mode;
        this.description = description;

        if (isArray() && !acceptValue()) {
            throw new Exception("Impossible to have an option mode VALUE_IS_ARRAY if the option does not accept a value.");
        }

        this.setdefaultValue(defaultValue);
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
     * Sets the defaultValue value.
     *
     * @param defaultValue The defaultValue value
     *
     * @throws Exception When incorrect defaultValue value is given
     */
    public void setdefaultValue(Object defaultValue) throws Exception
    {
        if (VALUE_NONE == (VALUE_NONE & mode) && null != defaultValue) {
            throw new Exception("Cannot set a defaultValue value when using Option::VALUE_NONE mode.");
        }

        if (isArray()) {
            if (null == defaultValue) {
                defaultValue = new ArrayList<Object>();
            } else if (!(defaultValue instanceof ArrayList)) {
                throw new Exception("A defaultValue value for an array option must be an array.");
            }
        }

        this.defaultValue = acceptValue() ? defaultValue : false;
    }

    /**
     * Returns the defaultValue value.
     *
     * @return The defaultValue value
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
        return this.description;
    }

    /**
     * Checks whether the given option equals this one
     * TODO Check that this method works as expected
     *
     * @param option Option to compare
     * @return
     */
    public boolean equals(InputOption option) {
        return option.getName().equals(getName())
            && option.getShortcut().equals(getShortcut())
            && option.getDefaultValue() == getDefaultValue()
            && option.isArray() == isArray()
            && option.isValueRequired() == isValueRequired()
            && option.isValueOptional() == isValueOptional()
        ;
    }

}
