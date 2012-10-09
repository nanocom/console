/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.List;
import org.nanocom.console.exception.LogicException;

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
     * Constructor.
     *
     * @param name         The argument name
     * @param mode         The argument mode: REQUIRED or OPTIONAL
     * @param description  A description text
     * @param defaultValue The default value (for OPTIONAL mode only)
     *
     * @throws IllegalArgumentException When argument mode is not valid
     */
    public InputArgument(String name, int mode, String description, Object defaultValue) {
        init(name, mode, description, defaultValue);
    }

    /**
     * Constructor.
     *
     * @param name        The argument name
     * @param mode        The argument mode: REQUIRED or OPTIONAL
     * @param description A description text
     *
     * @throws IllegalArgumentException When argument mode is not valid
     */
    public InputArgument(String name, int mode, String description) {
        this(name, mode, description, null);
    }

    /**
     * Constructor.
     *
     * @param name The argument name
     * @param mode The argument mode: REQUIRED or OPTIONAL
     *
     * @throws IllegalArgumentException When argument mode is not valid
     */
    public InputArgument(String name, int mode) {
        this(name, mode, "");
    }

    /**
     * Constructor.
     *
     * @param name The argument name
     */
    public InputArgument(String name) {
        this(name, OPTIONAL);
    }

    private void init(String name, int mode, String description, Object defaultValue) {
        if (mode > 7 || mode < 1) {
            throw new IllegalArgumentException(String.format("Argument mode \"%d\" is not valid.", mode));
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
        return REQUIRED == (REQUIRED & mode);
    }

    /**
     * Returns true if the argument can take multiple values.
     *
     * @return True if mode is IS_ARRAY, false otherwise
     */
    public boolean isArray() {
        return IS_ARRAY == (IS_ARRAY & mode);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue The default value default null
     *
     * @throws LogicException When incorrect default value is given
     */
    public final void setDefaultValue(Object defaultValue) {
        if (REQUIRED == mode && null != defaultValue) {
            throw new LogicException("Cannot set a default value except for Parameter.OPTIONAL mode.");
        }

        if (isArray()) {
            if (null == defaultValue) {
                defaultValue = new ArrayList<Object>();
            } else if (!(defaultValue instanceof List)) {
                throw new LogicException("A default value for an array argument must be an array.");
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