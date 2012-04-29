package com.nanocom.console.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command line argument.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class InputArgument {

    public static final int REQUIRED = 1;
    public static final int OPTIONAL = 2;
    public static final int IS_ARRAY = 4;

    private String  name;
    private Integer mode;
    private Object  defaultValue;
    private String  description;

    /**
     * @param name         The argument name
     * @param mode         The argument mode: REQUIRED or OPTIONAL
     * @param description  A description text
     * @param defaultValue The default value (for OPTIONAL mode only)
     *
     * @throws Exception When argument mode is not valid
     */
    public InputArgument(final String name, Integer mode, final String description, final Object defaultValue) throws Exception {
        init(name, mode, description, defaultValue);
    }
    
    public InputArgument(final String name, Integer mode, final String description) throws Exception {
        init(name, mode, description, null);
    }
    
    public InputArgument(final String name, Integer mode) throws Exception {
        init(name, mode, "", null);
    }
    
    public InputArgument(final String name) throws Exception {
        init(name, null, "", null);
    }
    
    private void init(String name, Integer mode, String description, Object defaultValue) throws Exception {
        if (null == mode) {
            mode = OPTIONAL;
        } else if (mode > 7 || mode < 1) {
            throw new Exception("Argument mode \"" + mode + "\" is not valid.");
        }

        this.name        = name;
        this.mode        = mode;
        this.description = description;

        setDefaultValue(defaultValue);
    }

    /**
     * Returns the argument name.
     *
     * @return The argument name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if the argument is required.
     *
     * @return True if parameter mode is REQUIRED, false otherwise
     */
    public boolean isRequired() {
        return REQUIRED == (REQUIRED & this.mode);
    }

    /**
     * Returns true if the argument can take multiple values.
     *
     * @return True if mode is IS_ARRAY, false otherwise
     */
    public boolean isArray() {
        return IS_ARRAY == (IS_ARRAY & this.mode);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue The default value default null
     *
     * @throws Exception When incorrect defaultValue value is given
     */
    public final void setDefaultValue(Object defaultValue) throws Exception {
        if (REQUIRED == mode && null != defaultValue) {
            throw new Exception("Cannot set a default value except for Parameter.OPTIONAL mode.");
        }

        if (isArray()) {
            if (null == defaultValue) {
                defaultValue = new ArrayList<Object>();
            } else if (!(defaultValue instanceof List)) {
                throw new Exception("A default value for an array argument must be an array.");
            }
        }

        this.defaultValue = defaultValue;
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

}