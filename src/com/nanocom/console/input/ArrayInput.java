package com.nanocom.console.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    /**
     * @param parameters An array of parameters
     * @param definition A InputDefinition instance
     */
    public ArrayInput(final Map<String, String> parameters, final InputDefinition definition) throws Exception {
        this.parameters = parameters;
        init(definition);
    }

    public ArrayInput(final Map<String, String> parameters) throws Exception {
        this.parameters = parameters;
        init(null);
    }

    /**
     * Returns the first argument from the raw parameters (not parsed).
     *
     * @return The value of the first argument or null otherwise
     */
    @Override
    public String getFirstArgument() {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (null != parameter.getKey() && !parameter.getKey().isEmpty() && '-' == parameter.getKey().charAt(0)) {
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
    public boolean hasParameterOption(final String value) {

        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().equals(value)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean hasParameterOption(final List<String> values) {
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (values.contains(parameter.getValue())) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public boolean hasParameterOption(final Map<String, String> values) {
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
    public Object getParameterOption(final List<String> values, final Object defaultValue) {
        List<String> listValues = (List<String>)values;
        for (Entry<String, String> parameter : parameters.entrySet()) {
            if (listValues.contains(parameter.getValue())) {
                return true;
            }
        }

        return defaultValue;
    }

    @Override
    public Object getParameterOption(final List<String> values) {
        return getParameterOption(values, false);
    }

    @Override
    public Object getParameterOption(final String value, final Object defaultValue) {
        return getParameterOption(Arrays.asList(value), defaultValue);
    }

    @Override
    public Object getParameterOption(final String value) {
        return getParameterOption(value, false);
    }

    /**
     * Processes command line arguments.
     */
    @Override
    protected void parse() throws Exception {
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
     * @throws Exception When option given doesn't exist
     */
    private void addShortOption(final String shortcut, final Object value) throws Exception {
        if (!definition.hasShortcut(shortcut)) {
            throw new Exception("The \"" + shortcut + "\" option does not exist.");
        }

        addLongOption(definition.getOptionForShortcut(shortcut).getName(), value);
    }

    /**
     * Adds a long option value.
     *
     * @param name  The long option key
     * @param value The value for the option
     *
     * @throws Exception When option given doesn't exist
     * @throws Exception When a required value is missing
     */
    private void addLongOption(final String name, Object value) throws Exception {
        if (!definition.hasOption(name)) {
            throw new Exception("The \"" + name + "\" option does not exist.");
        }

        InputOption option = definition.getOption(name);

        if (null == value) {
            if (option.isValueRequired()) {
                throw new Exception("The \"" + name + "\" option requires a value.");
            }

            value = option.isValueOptional() ? option.getDefaultValue() : true;
        }
        
        options.put(name, value);
    }

    /**
     * Adds an argument value.
     *
     * @param name  The argument name
     * @param value The value for the argument
     *
     * @throws \InvalidArgumentException When argument given doesn't exist
     */
    private void addArgument(final String name, final Object value) throws Exception
    {
        if (!definition.hasArgument(name)) {
            throw new Exception("The \"" + name + "\" argument does not exist.");
        }

        arguments.put(name, value);
    }

}
