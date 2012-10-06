/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * ArrayInput represents an input provided as an array.
 *
 * Usage:
 *
 *     Input input = new ArrayInput(new HashMap<String, String>("name" => "foo", "--bar" => "foobar"));
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ArrayInput extends Input {

    private Map<String, String> parameters;

    public ArrayInput(Map<String, String> parameters) {
        this(parameters, null);
    }

    /**
     * @param parameters An array of parameters
     * @param definition A InputDefinition instance
     */
    public ArrayInput(Map<String, String> parameters, InputDefinition definition) {
        this.parameters = parameters;
        init(definition);
    }

    /**
     * Returns the first argument from the raw parameters (not parsed).
     *
     * @return The value of the first argument or null otherwise
     */
    @Override
    public String getFirstArgument() {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            String key = parameter.getKey();
            if (isNotEmpty(key) && '-' == key.charAt(0)) {
                continue;
            }

            return parameter.getValue();
        }

        return null;
    }

    /**
     * Returns true if the raw parameters (not parsed) contain a value.
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param value The values to look for in the raw parameters (can be an array)
     *
     * @return True if the value is contained in the raw parameters
     */
    @Override
    public boolean hasParameterOption(String value) {

        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().equals(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasParameterOption(List<String> values) {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (values.contains(parameter.getKey())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasParameterOption(Map<String, String> values) {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (values.containsKey(parameter.getKey())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The value(s) to look for in the raw parameters (can be an array)
     * @param defaultValue The default value to return if no result is found default false
     *
     * @return The option value
     */
    @Override
    public Object getParameterOption(List<String> values, Object defaultValue) {
        List<String> listValues = (List<String>)values;
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (listValues.contains(parameter.getValue())) {
                return true;
            }
        }

        return defaultValue;
    }

    @Override
    public Object getParameterOption(List<String> values) {
        return getParameterOption(values, false);
    }

    @Override
    public Object getParameterOption(String value, Object defaultValue) {
        return getParameterOption(Arrays.asList(value), defaultValue);
    }

    @Override
    public Object getParameterOption(String value) {
        return getParameterOption(value, false);
    }

    /**
     * Processes command line arguments.
     */
    @Override
    protected void parse() {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().startsWith("--")) {
                addLongOption(parameter.getKey().substring(2), parameter.getValue());
            } else if ('-' == parameter.getKey().charAt(0)) {
                addShortOption(parameter.getKey().substring(1), parameter.getValue());
            } else {
                addArgument(parameter.getKey(), parameter.getValue());
            }
        }
    }

    /**
     * Adds a short option value.
     *
     * @param shortcut The short option key
     * @param value    The value for the option
     *
     * @throws RuntimeException When option given doesn't exist
     */
    private void addShortOption(String shortcut, String value) throws RuntimeException {
        if (!definition.hasShortcut(shortcut)) {
            throw new IllegalArgumentException(String.format("The \"-%s\" option does not exist.", shortcut));
        }

        addLongOption(definition.getOptionForShortcut(shortcut).getName(), value);
    }

    /**
     * Adds a long option value.
     *
     * @param name  The long option key
     * @param value The value for the option
     *
     * @throws IllegalArgumentException When option given doesn't exist
     * @throws IllegalArgumentException When a required value is missing
     */
    private void addLongOption(String name, String value) throws IllegalArgumentException {
        if (!definition.hasOption(name)) {
            throw new IllegalArgumentException(String.format("The \"--%s\" option does not exist.", name));
        }

        InputOption option = definition.getOption(name);

        if (null == value) {
            if (option.isValueRequired()) {
                throw new IllegalArgumentException(String.format("The \"--%s\" option requires a value.", name));
            }

            value = option.isValueOptional() ? String.valueOf(option.getDefaultValue()) : "true";
        }

        Object parsedValue = "true".equalsIgnoreCase(value) ? true : "false".equalsIgnoreCase(value) ? false : value;

        options.put(name, parsedValue);
    }

    /**
     * Adds an argument value.
     *
     * @param name  The argument name
     * @param value The value for the argument
     *
     * @throws IllegalArgumentException When argument given doesn't exist
     */
    private void addArgument(String name, Object value) throws IllegalArgumentException {
        if (!definition.hasArgument(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", name));
        }

        arguments.put(name, value);
    }

}
