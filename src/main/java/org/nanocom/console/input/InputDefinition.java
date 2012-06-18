/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nanocom.console.Util;

/**
 * An InputDefinition represents a set of valid command line arguments and options.
 *
 * Usage:
 *
 *     InputDefinition definition = new InputDefinition(Arrays.asList((Object)
 *       new InputArgument("name", InputArgument.REQUIRED),
 *       new InputOption("foo", "f", InputOption.VALUE_REQUIRED),
 *     ));
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */

public class InputDefinition {

    private Map<String, InputArgument> arguments;
    private Integer                    requiredCount;
    private Boolean                    hasAnArrayArgument = false;
    private Boolean                    hasOptional;
    private Map<String, InputOption>   options;
    private Map<String, String>        shortcuts;

    /**
     * @param definition An array of InputArgument and InputOption instance
     */
    public InputDefinition(final List<Object> definition) {
        setDefinition(definition);
    }
    
    public InputDefinition() throws Exception {
        setDefinition(new ArrayList<Object>());
    }

    /**
     * Sets the definition of the input.
     *
     * @param definition The definition array
     */
    public final void setDefinition(final List<Object> definition) {
        List<InputArgument> locArguments = new ArrayList<InputArgument>();
        List<InputOption> locOptions = new ArrayList<InputOption>();

        for (Object item : definition) {
            if (item instanceof InputOption) {
                locOptions.add((InputOption) item);
            } else if (item instanceof InputArgument) {
                locArguments.add((InputArgument) item);
            } else {
                throw new IllegalArgumentException("The definition list must contain only InputArgument or InputOption instances.");
            }
        }

        setArguments(locArguments);
        setOptions(locOptions);
    }

    /**
     * Sets the InputArgument objects.
     *
     * @param arguments An array of InputArgument objects
     */
    public void setArguments(final List<InputArgument> arguments) {
        this.arguments     = new HashMap<String, InputArgument>();
        requiredCount      = 0;
        hasOptional        = false;
        hasAnArrayArgument = false;
        addArguments(arguments);
    }

    /**
     * Adds an array of InputArgument objects.
     *
     * @param arguments An array of InputArgument objects
     */
    public void addArguments(final List<InputArgument> arguments) {
        if (null != arguments) {
            for (InputArgument argument : arguments) {
                addArgument(argument);
            }
        }
    }

    /**
     * Adds an InputArgument object.
     *
     * @param argument An InputArgument object
     *
     * @throws Exception When incorrect argument is given
     */
    public void addArgument(final InputArgument argument) throws Exception {
        if (arguments.containsKey(argument.getName())) {
            throw new Exception(String.format("An argument with name \"%s\" already exist.", argument.getName()));
        }

        if (hasAnArrayArgument) {
            throw new Exception("Cannot add an argument after an array argument.");
        }

        if (argument.isRequired() && hasOptional) {
            throw new Exception("Cannot add a required argument after an optional one.");
        }

        if (argument.isArray()) {
            hasAnArrayArgument = true;
        }

        if (argument.isRequired()) {
            ++requiredCount;
        } else {
            hasOptional = true;
        }

        arguments.put(argument.getName(), argument);
    }

    /**
     * Returns an InputArgument by name.
     *
     * @param name The InputArgument name or position
     *
     * @return An InputArgument object
     *
     * @throws IllegalArgumentException When argument given doesn't exist
     */
    public InputArgument getArgument(final String name) throws IllegalArgumentException {
        if (!hasArgument(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", name));
        }

        return arguments.get(name);
    }
    
    /**
     * Returns an InputArgument by position.
     *
     * @param position The InputArgument name or position
     *
     * @return An InputArgument object
     *
     * @throws IllegalArgumentException When argument given doesn't exist
     */
    public InputArgument getArgument(final Integer position) throws IllegalArgumentException {
        if (!hasArgument(position)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", position);
        }

        /* 
         * TODO Optimize this part if possible
         * Maybe change Map<String, InputArgument> to something more adapted?
         */
        Collection<InputArgument> valuesArguments = arguments.values();
        List<InputArgument> arrayArguments = new ArrayList<InputArgument>();
        arrayArguments.addAll(valuesArguments);

        return arrayArguments.get(position);
    }

    /**
     * Returns true if an InputArgument object exists by name.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    public boolean hasArgument(final String name) {
        return arguments.containsKey(name);
    }
    
    /**
     * Returns true if an InputArgument object exists by position.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    public boolean hasArgument(final Integer position) {
        return arguments.size() > position;
    }

    /**
     * Gets the array of InputArgument objects.
     *
     * @return An array of InputArgument objects
     */
    public Map<String, InputArgument> getArguments() {
        return arguments;
    }

    /**
     * Returns the number of InputArguments.
     *
     * @return The number of InputArguments
     */
    public int getArgumentCount() {
        return hasAnArrayArgument ? Integer.MAX_VALUE : arguments.size();
    }

    /**
     * Returns the number of required InputArguments.
     *
     * @return The number of required InputArguments
     */
    public int getArgumentRequiredCount() {
        return requiredCount;
    }

    /**
     * Gets the default values.
     *
     * @return An array of default values
     */
    public Map<String, Object> getArgumentDefaults() {
        Map<String, Object> values = new HashMap<String, Object>();
        for (InputArgument argument : arguments.values()) {
            values.put(argument.getName(), argument.getDefaultValue());
        }

        return values;
    }

    /**
     * Sets the InputOption objects.
     *
     * @param options An array of InputOption objects
     */
    public void setOptions(final List<InputOption> options) {
        this.options = new HashMap<String, InputOption>();
        shortcuts = new HashMap<String, String>();
        addOptions(options);
    }

    /**
     * Adds an array of InputOption objects.
     *
     * @param options An array of InputOption objects
     */
    public void addOptions(final List<InputOption> options) {
        for (InputOption option : options) {
            addOption(option);
        }
    }

    /**
     * Adds an InputOption object.
     *
     * @param option An InputOption object
     *
     * @throws Exception When option given already exist
     */
    public void addOption(final InputOption option) throws Exception {
        if (options.containsKey(option.getName()) && !option.equals(options.get(option.getName()))) {
            throw new Exception(String.format("An option named \"%s\" already exist.", option.getName()));
        } else if (
                shortcuts.containsKey(option.getShortcut())
                && !option.equals(options.get(shortcuts.get(option.getShortcut())))
        ) {
            throw new Exception(String.format("An option with shortcut \"%s\" already exist.", option.getShortcut()));
        }

        options.put(option.getName(), option);
        if (null != option.getShortcut()) {
            shortcuts.put(option.getShortcut(), option.getName());
        }
    }

    /**
     * Returns an InputOption by name.
     *
     * @param name The InputOption name
     * @return An InputOption object
     */
    public InputOption getOption(final String name) throws Exception {
        if (!hasOption(name)) {
            throw new Exception("The \"--" + name + "\" option does not exist.");
        }

        return options.get(name);
    }

    /**
     * Returns true if an InputOption object exists by name.
     *
     * @param name The InputOption name
     * @return True if the InputOption object exists, false otherwise
     */
    public boolean hasOption(final String name) {
        return options.containsKey(name);
    }

    /**
     * Gets the array of InputOption objects.
     *
     * @return An array of InputOption objects
     */
    public Map<String, InputOption> getOptions() {
        return options;
    }

    /**
     * Returns true if an InputOption object exists by shortcut.
     *
     * @param name The InputOption shortcut
     * @return True if the InputOption object exists, false otherwise
     */
    public boolean hasShortcut(final String name) {
        return shortcuts.containsKey(name);
    }

    /**
     * Gets an InputOption by shortcut.
     *
     * @param shortcut The shortcut name
     * 
     * @return An InputOption object
     */
    public InputOption getOptionForShortcut(final String shortcut) throws Exception {
        return getOption(shortcutToName(shortcut));
    }

    /**
     * Gets an array of default values.
     *
     * @return An array of all default values
     */
    public Map<String, Object> getOptionDefaults() {
        Map<String, Object> values = new HashMap<String, Object>();
        for (InputOption option : options.values()) {
            values.put(option.getName(), option.getDefaultValue());
        }

        return values;
    }

    /**
     * Returns the InputOption name given a shortcut.
     *
     * @param shortcut The shortcut
     * @return The InputOption name
     *
     * @throws IllegalArgumentException When option given does not exist
     */
    private String shortcutToName(final String shortcut) throws IllegalArgumentException {
        if (!shortcuts.containsKey(shortcut)) {
            throw new IllegalArgumentException(String.format("The \"-%s\" option does not exist.", shortcut));
        }

        return shortcuts.get(shortcut);
    }

    /**
     * Gets the synopsis.
     *
     * @return The synopsis
     */
    public String getSynopsis() {
        StringBuilder sb = new StringBuilder();
        for (InputOption option : options.values()) {
            String shortcut = (null != option.getShortcut()) ? "-" + option.getShortcut() + "|" : "";
            sb.append("[");

            if (option.isValueRequired()) {
                sb.append(String.format("%s--%s=\"...\"", shortcut, option.getName()));
            } else if (option.isValueOptional()) {
                sb.append(String.format("%s--%s[=\"...\"]", shortcut, option.getName()));
            } else {
                sb.append(String.format("%s--%s", shortcut, option.getName()));
            }

            sb.append("] ");
        }

        for (InputArgument argument : arguments.values()) {
            if (argument.isRequired()) {
                sb.append(String.format("%s%s", argument.getName(), argument.isArray() ? "1" : ""));
            } else {
                sb.append(String.format("[%s%s]", argument.getName(), (argument.isArray() ? "1" : "")));
            }

            if (argument.isArray()) {
                sb.append(String.format(" ... [%sN]", argument.getName()));
            }
        }

        return sb.toString().trim();
    }

    /**
     * Returns a textual representation of the InputDefinition.
     *
     * @return A string representing the InputDefinition
     */
    public String asText() {
        // Find the largest option or argument name
        int max = 0;
        for (InputOption option : options.values()) {
            int nameLength = option.getName().length() + 2;
            if (null != option.getShortcut()) {
                nameLength += option.getShortcut().length() + 3;
            }

            max = Math.max(max, nameLength);
        }
        for (InputArgument argument : arguments.values()) {
            max = Math.max(max, argument.getName().length());
        }
        ++max;

        StringBuilder sb = new StringBuilder();

        if (!getArguments().isEmpty()) {
            sb.append("<comment>Arguments:</comment>");
            String defaultValue;
            for (InputArgument argument : getArguments().values()) {
                if (null != argument.getDefaultValue()
                        && (
                                !(argument.getDefaultValue() instanceof ArrayList)
                                || ((ArrayList)argument.getDefaultValue()).isEmpty()
                        )
                ) {
                    defaultValue = "<comment> (default: " + formatDefaultValue(argument.getDefaultValue()) + ")</comment>";
                } else {
                    defaultValue = "";
                }

                String replaceBy = String.format("%1$-" + max + 2 + "s", ""); // PHP's str_pad (right) equivalent
                String description = argument.getDescription().replaceAll("\n", "\n" + replaceBy);

                // FIXME Add max value
                sb.append(" <info>" + argument.getName() + "</info> " + description + defaultValue);
            }
        }

        if (!getOptions().isEmpty()) {
            sb.append("<comment>Options:</comment>");

            for (InputOption option : getOptions().values()) {
                String defaultValue;
                if (
                        option.acceptValue()
                        && null != option.getDefaultValue()
                        && (
                                !(option.getDefaultValue() instanceof ArrayList)
                                || ((ArrayList)option.getDefaultValue()).isEmpty()
                        )
                ) {
                    defaultValue = "<comment> (default: " + formatDefaultValue(option.getDefaultValue()) + ")</comment>";
                } else {
                    defaultValue = "";
                }

                String multiple = option.isArray() ? "<comment> (multiple values allowed)</comment>" : "";

                String replaceBy = String.format("%1$-" + max + 2 + "s", ""); // PHP's str_pad (right) equivalent
                String description = option.getDescription().replaceAll("\n", "\n" + replaceBy);

                int optionMax = max - option.getName().length() - 2;
                sb.append(" <info>--" + option.getName() + "</info> ");
                sb.append(null != option.getShortcut() ? "(-" + option.getShortcut() + ") " : "");
                sb.append(description);
                sb.append(defaultValue);
                sb.append(multiple);
            }
        }

        return sb.toString();
    }

    /**
     * Returns an XML representation of the InputDefinition.
     *
     * @param Boolean asDom Whether to return a DOM or an XML string
     *
     * @return string|DOMDocument An XML string representing the InputDefinition
     */
    public String asXml(final boolean asDom) throws Exception {
        throw new Exception("Not implemented yet.");
    }

    @SuppressWarnings("unchecked")
    private String formatDefaultValue(final Object defaultValue) {
        if (defaultValue instanceof ArrayList) {
            return String.format("array('%s')",
                    Util.implode("', '", (String[]) ((List<String>) defaultValue).toArray()));
        }

        //TODO implement this part
        //return str_replace("\n", '', var_export(defaultValue, true));
        return "";
    }

}
