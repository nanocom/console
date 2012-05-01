/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.input;

import java.util.*;

/**
 * ArgvInput represents an input coming from the CLI arguments.
 *
 * Usage:
 *
 *     InputInterface input = new ArgvInput();
 *
 * When passing an argument to the constructor, be sure that it respects
 * the same rules as the argv one. It's almost always better to use the
 * `StringInput` when you want to provide your own input.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ArgvInput extends Input {

    protected ArrayList<String> tokens;
    private LinkedList<String> parsed;

    /**
     * @param argv An array of parameters from the CLI (in the argv format)
     * @param definition A InputDefinition instance
     */
    public ArgvInput(String[] argv, final InputDefinition definition) throws Exception {
        tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(argv));
        init(definition);
    }

    public ArgvInput(String[] argv) throws Exception {
        tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(argv));
        init(null);
    }

    public ArgvInput() throws Exception {
        tokens = new ArrayList<String>();
        init(null);
    }

    protected void setTokens(String[] tokens) {
        this.tokens = new ArrayList<String>();
        this.tokens.addAll(Arrays.asList(tokens));
    }

    /**
     * Processes command line arguments.
     */
    @Override
    protected void parse() throws Exception {
        parsed = new LinkedList<String>();
        parsed.addAll(tokens);
        
        while (!parsed.isEmpty()) {
            String token = parsed.poll();

            if (token.startsWith("--")) {
                parseLongOption(token);
            } else if ('-' == token.charAt(0)) {
                parseShortOption(token);
            } else {
                parseArgument(token);
            }
        }
    }

    /**
     * Parses a short option.
     *
     * @param token The current token.
     */
    private void parseShortOption(final String token) throws Exception {
        String name = token.substring(1);

        if (name.length() > 1) {
            if (
                    definition.hasShortcut(name.substring(0, 1))
                    && definition.getOptionForShortcut(name.substring(0, 1)).acceptValue()
            ) {
                // An option with a value (with no space)
                addShortOption(name.substring(0, 1), name.substring(1));
            } else {
                parseShortOptionSet(name);
            }
        } else {
            addShortOption(name, null);
        }
    }

    /**
     * Parses a short option set.
     *
     * @param name The current token
     *
     * @throws Exception When option given doesn't exist
     */
    private void parseShortOptionSet(final String name) throws Exception {
        int nameLength = name.length();
        for (int i = 0; i < nameLength; i++) {
            if (!definition.hasShortcut(name.substring(i, i + 1))) {
                throw new Exception("The \"-" + name.substring(i, i + 1) + "\" option does not exist.");
            }

            InputOption option = definition.getOptionForShortcut(name.substring(i, i + 1));
            if (option.acceptValue()) {
                addLongOption(option.getName(), i == nameLength - 1 ? null : name.substring(i + 1));

                break;
            } else {
                addLongOption(option.getName(), true);
            }
        }
    }

    /**
     * Parses a long option.
     *
     * @param token The current token
     */
    private void parseLongOption(final String token) throws Exception
    {
        String name = token.substring(2);

        int index = name.indexOf('=');
        if (-1 != index) {
            addLongOption(name.substring(0, index), name.substring(index + 1));
        } else {
            addLongOption(name, null);
        }
    }

    /**
     * Parses an argument.
     *
     * @param token The current token
     *
     * @throws Exception When too many arguments are given
     */
    private void parseArgument(final String token) throws Exception {
        int c = arguments.size();

        // If input is expecting another argument, add it
        if (definition.hasArgument(c)) {
            InputArgument arg = definition.getArgument(c);

            if (arg.isArray()) {
                ArrayList<String> tokenAsList = new ArrayList<String>();
                tokenAsList.add(token);
                arguments.put(arg.getName(), tokenAsList);
            } else {
                arguments.put(arg.getName(), token);
            }

        // If last argument isArray(), append token to last argument
        } else if (definition.hasArgument(c - 1) && definition.getArgument(c - 1).isArray()) {
            InputArgument arg = definition.getArgument(c - 1);
            arguments.put(arg.getName(), token);

        // Unexpected argument
        } else {
            throw new Exception("Too many arguments.");
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
            throw new Exception("The \"-" + shortcut + "\" option does not exist.");
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
     */
    @SuppressWarnings("unchecked")
    private void addLongOption(final String name, Object value) throws Exception {
        if (!definition.hasOption(name)) {
            throw new Exception("The \"--" + name + "\" option does not exist.");
        }

        InputOption option = definition.getOption(name);

        if (null == value && option.acceptValue()) {
            // If option accepts an optional or mandatory argument
            // Let's see if there is one provided
            String next = parsed.getFirst();
            if ('-' != next.charAt(0)) {
                value = next;
                parsed.removeFirst();
            }
        }

        if (null == value) {
            if (option.isValueRequired()) {
                throw new Exception("The \"--" + name + "\" option requires a value.");
            }

            value = option.isValueOptional() ? option.getDefaultValue() : true;
        }

        if (option.isArray()) {
            // This line is giving an "unchecked" warning
            ((List<Object>) options.get(name)).add(value);
        } else {
            options.put(name, value);
        }
    }

    /**
     * Returns the first argument from the raw parameters (not parsed).
     *
     * @return The value of the first argument or null otherwise
     */
    @Override
    public String getFirstArgument() {
        for (String token : tokens) {
            if (!token.isEmpty() && '-' == token.charAt(0)) {
                continue;
            }

            return token;
        }
        
        return null;
    }

    /**
     * Returns true if the raw parameters (not parsed) contain a value.
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The value(s) to look for in the raw parameters (can be an array)
     *
     * @return True if the value is contained in the raw parameters
     */
    @Override
    public boolean hasParameterOption(final String value) {
        return hasParameterOption(Arrays.asList(value));
    }
 
    @Override
    public boolean hasParameterOption(final List<String> values) {
        for (String value : tokens) {
            if (values.contains(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasParameterOption(final Map<String, String> values) {
        for (String value : tokens) {
            if (values.containsValue(value)) {
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
     * @param values  The value(s) to look for in the raw parameters (can be an array)
     * @param default The default value to return if no result is found
     *
     * @return The option value
     */
    @Override
    public Object getParameterOption(final List<String> values, final Object defaultValue) {
        LinkedList<String> locTokens = new LinkedList<String>();
        locTokens.addAll(tokens);

        while (!locTokens.isEmpty()) {
            String token = locTokens.poll();

            for (String value : values) {
                if (value.startsWith(token)) {
                    int pos = token.indexOf("=");
                    if (pos > -1) {
                        return token.substring(pos + 1);
                    }

                    return locTokens.poll();
                }
            }
        }

        return defaultValue;
    }
    
    @Override
    public Object getParameterOption(final List<String> values) {
        return getParameterOption(values, false);
    }

    @Override
    public Object getParameterOption(final String value) {
        return getParameterOption(Arrays.asList(value), false);
    }

    @Override
    public Object getParameterOption(final String value, final Object defaultValue) {
        return getParameterOption(Arrays.asList(value), defaultValue);
    }

}
