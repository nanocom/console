/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * ArgvInput represents an input coming from the CLI arguments.
 *
 * Usage:
 *
 *     public static void main(String[] args) {
 *         InputInterface input = new ArgvInput(args);
 *     }
 *
 * Don't forget that the first element of the array
 * is the name of the running application.
 *
 * When passing an argument to the constructor, be sure that it respects
 * the same rules as the args one. It's almost always better to use the
 * `StringInput` when you want to provide your own input.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ArgvInput extends Input {

    protected List<String> tokens;
    private List<String> parsed;

    /**
     * Constructor.
     *
     * @param args       An array of parameters (in the args format)
     * @param definition An InputDefinition instance
     */
    public ArgvInput(String[] args) {
        this(args, null);
    }

    /**
     * Constructor.
     *
     * @param args       An array of parameters (in the args format)
     * @param definition An InputDefinition instance
     */
    public ArgvInput(String[] argv, InputDefinition definition) {
        tokens = new ArrayList<String>();

        if (null != argv) {
            tokens.addAll(Arrays.asList(argv));
        }

        super.init(definition);
    }

    protected void setTokens(String[] tokens) {
        this.tokens = new ArrayList<String>(Arrays.asList(tokens));
    }

    /**
     * Processes command line arguments.
     */
    @Override
    protected void parse() {
        boolean parseOptions = true;
        parsed = new ArrayList<String>(tokens);
        String token;

        while (!parsed.isEmpty()) {
            token = parsed.remove(0);
            if (parseOptions && EMPTY.equals(token)) {
                parseArgument(token);
            } else if (parseOptions && "--".equals(token)) {
                parseOptions = false;
            } else if (parseOptions && token.startsWith("--")) {
                parseLongOption(token);
            } else if (parseOptions && '-' == token.charAt(0)) {
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
    private void parseShortOption(String token) {
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
     * @throws RuntimeException When option given doesn't exist
     */
    private void parseShortOptionSet(String name) throws RuntimeException {
        int nameLength = name.length();

        for (int i = 0; i < nameLength; i++) {
            if (!definition.hasShortcut(name.substring(i, i + 1))) {
                throw new RuntimeException(String.format("The \"-%s\" option does not exist.", name.substring(i, i + 1)));
            }

            InputOption option = definition.getOptionForShortcut(name.substring(i, i + 1));

            if (option.acceptValue()) {
                addLongOption(option.getName(), i == nameLength - 1 ? null : name.substring(i + 1));
                break;
            } else {
                addLongOption(option.getName(), null);
            }
        }
    }

    /**
     * Parses a long option.
     *
     * @param token The current token
     */
    private void parseLongOption(String token)
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
     * @throws RuntimeException When too many arguments are given
     */
    private void parseArgument(String token) throws RuntimeException {
        int c = arguments.size();

        if (definition.hasArgument(c)) {
            // If input is expecting another argument, add it
            InputArgument arg = definition.getArgument(c);

            if (arg.isArray()) {
                List<String> tokensList = new ArrayList<String>();
                tokensList.add(token);
                arguments.put(arg.getName(), tokensList);
            } else {
                arguments.put(arg.getName(), token);
            }
        } else if (definition.hasArgument(c - 1) && definition.getArgument(c - 1).isArray()) {
            // If last argument isArray(), append token to last argument
            InputArgument arg = definition.getArgument(c - 1);
            ((List<String>) arguments.get(arg.getName())).add(token);
        } else {
            // Unexpected argument
            throw new RuntimeException("Too many arguments.");
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
            throw new RuntimeException(String.format("The \"-%s\" option does not exist.", shortcut));
        }

        addLongOption(definition.getOptionForShortcut(shortcut).getName(), value);
    }

    /**
     * Adds a long option value.
     *
     * @param name  The long option key
     * @param value The value for the option
     *
     * @throws RuntimeException When option given doesn't exist
     */
    @SuppressWarnings("unchecked")
    private void addLongOption(String name, String value) throws RuntimeException {
        if (!definition.hasOption(name)) {
            throw new RuntimeException(String.format("The \"--%s\" option does not exist.", name));
        }

        InputOption option = definition.getOption(name);

        if (null == value && option.acceptValue() && !parsed.isEmpty()) {
            // If option accepts an optional or mandatory argument
            // Let's see if there is one provided
            String next = parsed.get(0);
            if ('-' != next.charAt(0)) {
                value = next;
                parsed.remove(0);
            }
        }

        Object parsedValue;

        if (null == value) {
            if (option.isValueRequired()) {
                throw new RuntimeException(String.format("The \"--%s\" option requires a value.", name));
            }

            parsedValue = option.isValueOptional() ? option.getDefaultValue() : true;
        } else {
            parsedValue = value;
        }

        if (option.isArray()) {
            if (options.containsKey(name)) {
                 ((List<Object>) options.get(name)).add(value);
            } else {
                List<Object> valueList = new ArrayList<Object>();
                valueList.add(value);
                options.put(name, valueList);
            }
        } else {
            options.put(name, parsedValue);
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
     * @param values The value to look for in the raw parameters
     *
     * @return True if the value is contained in the raw parameters
     */
    @Override
    public boolean hasParameterOption(String value) {
        return hasParameterOption(Arrays.asList(value));
    }

    /**
     * Returns true if the raw parameters (not parsed) contain a value.
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The values to look for in the raw parameters
     *
     * @return True if the values are contained in the raw parameters
     */
    @Override
    public boolean hasParameterOption(List<String> values) {
        for (String value : tokens) {
            if (values.contains(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the raw parameters (not parsed) contain a value.
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The values to look for in the raw parameters
     *
     * @return True if the values are contained in the raw parameters
     */
    @Override
    public boolean hasParameterOption(Map<String, String> values) {
        for (String value : tokens) {
            if (values.containsValue(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameterOption(List<String> values, Object defaultValue) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameterOption(List<String> values) {
        return getParameterOption(values, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameterOption(String value) {
        return getParameterOption(Arrays.asList(value), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getParameterOption(String value, Object defaultValue) {
        return getParameterOption(Arrays.asList(value), defaultValue);
    }
}
