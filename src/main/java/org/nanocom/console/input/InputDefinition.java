/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.*;
import java.util.Map.Entry;
import static org.apache.commons.lang3.StringUtils.*;
import org.nanocom.console.exception.LogicException;

/**
 * An InputDefinition represents a set of valid command line arguments and options.
 *
 * Usage:
 *
 *     InputDefinition definition = new InputDefinition(Arrays.<InputParameterInterface>asList(
 *       new InputArgument("name", InputArgument.REQUIRED),
 *       new InputOption("foo", "f", InputOption.VALUE_REQUIRED)
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
     * Constructor.
     *
     * @param definition A ist of InputParameterInterface instances
     */
    public InputDefinition(Collection<InputParameterInterface> definition) {
        setDefinition(definition);
    }

    /**
     * Constructor.
     *
     * @param definition An array of InputParameterInterface instances
     */
    public InputDefinition(InputParameterInterface[] definition) {
        setDefinition(Arrays.asList(definition));
    }

    /**
     * Constructor.
     */
    public InputDefinition() {
        this(new ArrayList<InputParameterInterface>());
    }

    /**
     * Sets the definition of the input.
     *
     * @param definition The definition collection
     */
    public final void setDefinition(Collection<InputParameterInterface> definition) {
        List<InputArgument> locArguments = new ArrayList<InputArgument>();
        List<InputOption> locOptions = new ArrayList<InputOption>();

        for (InputParameterInterface item : definition) {
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
     * Sets the definition of the input.
     *
     * @param definition The definition array
     */
    public final void setDefinition(InputParameterInterface[] definition) {
        setDefinition(Arrays.asList(definition));
    }

    /**
     * Sets the InputArgument objects.
     *
     * @param arguments An array of InputArgument objects
     */
    public void setArguments(List<InputArgument> arguments) {
        this.arguments     = new LinkedHashMap<String, InputArgument>();
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
    public void addArguments(List<InputArgument> arguments) {
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
     * @throws LogicException When incorrect argument is given
     */
    public void addArgument(InputArgument argument) {
        if (arguments.containsKey(argument.getName())) {
            throw new LogicException(String.format("An argument with name \"%s\" already exist.", argument.getName()));
        }

        if (hasAnArrayArgument) {
            throw new LogicException("Cannot add an argument after an array argument.");
        }

        if (argument.isRequired() && hasOptional) {
            throw new LogicException("Cannot add a required argument after an optional one.");
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
    public InputArgument getArgument(String name) {
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
    public InputArgument getArgument(int position) {
        if (!hasArgument(position)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", position));
        }

        // TODO Optimize this...
        List<InputArgument> arrayArguments = new ArrayList<InputArgument>(arguments.values());

        return arrayArguments.get(position);
    }

    /**
     * Returns true if an InputArgument object exists by name.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    public boolean hasArgument(String name) {
        return arguments.containsKey(name);
    }

    /**
     * Returns true if an InputArgument object exists by position.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    public boolean hasArgument(int position) {
        return position >= 0 && arguments.size() > position;
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
    public void setOptions(List<InputOption> options) {
        this.options = new LinkedHashMap<String, InputOption>();
        shortcuts = new LinkedHashMap<String, String>();
        addOptions(options);
    }

    /**
     * Adds an array of InputOption objects.
     *
     * @param options An array of InputOption objects
     */
    public void addOptions(List<InputOption> options) {
        for (InputOption option : options) {
            addOption(option);
        }
    }

    /**
     * Adds an InputOption object.
     *
     * @param option An InputOption object
     *
     * @throws LogicException When option given already exists
     */
    public void addOption(InputOption option) {
        if (options.containsKey(option.getName()) && !option.equals(options.get(option.getName()))) {
            throw new LogicException(String.format("An option named \"%s\" already exist.", option.getName()));
        } else if (
                null != option.getShortcut()
                && shortcuts.containsKey(option.getShortcut())
                && !option.equals(options.get(shortcuts.get(option.getShortcut())))
        ) {
            throw new LogicException(String.format("An option with shortcut \"%s\" already exist.", option.getShortcut()));
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
     *
     * @return An InputOption object
     *
     * @throws IllegalArgumentException When option given doesn't exist
     */
    public InputOption getOption(String name) {
        if (!hasOption(name)) {
            throw new IllegalArgumentException("The \"--" + name + "\" option does not exist.");
        }

        return options.get(name);
    }

    /**
     * Returns true if an InputOption object exists by name.
     *
     * @param name The InputOption name
     * @return True if the InputOption object exists, false otherwise
     */
    public boolean hasOption(String name) {
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
    public boolean hasShortcut(String name) {
        return shortcuts.containsKey(name);
    }

    /**
     * Gets an InputOption by shortcut.
     *
     * @param shortcut The shortcut name
     *
     * @return An InputOption object
     */
    public InputOption getOptionForShortcut(String shortcut) {
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
     *
     * @return The InputOption name
     *
     * @throws IllegalArgumentException When option given does not exist
     */
    private String shortcutToName(String shortcut) {
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
            String shortcut = (null != option.getShortcut()) ? "-" + option.getShortcut() + "|" : EMPTY;
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
                sb.append(String.format("%s%s", argument.getName(), argument.isArray() ? "1" : EMPTY));
            } else {
                sb.append(String.format("[%s%s]", argument.getName(), (argument.isArray() ? "1" : EMPTY)));
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
    @SuppressWarnings("unchecked")
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

        List<String> text = new ArrayList<String>();

        if (!getArguments().isEmpty()) {
            text.add("<comment>Arguments:</comment>");
            String defaultValue;

            for (InputArgument argument : getArguments().values()) {
                Object argumentDefaultValue = argument.getDefaultValue();
                if (null != argumentDefaultValue
                        && ((
                                !(argumentDefaultValue instanceof Iterable)
                                || ((List<Object>) argumentDefaultValue).isEmpty()
                        ) || (
                                !(argumentDefaultValue instanceof Map)
                                || ((Map<String, Object>) argumentDefaultValue).isEmpty()
                        ))
                ) {
                    defaultValue = String.format("<comment> (default: %s)</comment>", formatDefaultValue(argument.getDefaultValue()));
                } else {
                    defaultValue = EMPTY;
                }

                String description = argument.getDescription().replaceAll("\n", "\n" + repeat(' ', max + 2));

                text.add(String.format(" <info>%-" + max + "s</info> %s%s", argument.getName(), description, defaultValue));
            }

            text.add(EMPTY);
        }

        if (!getOptions().isEmpty()) {
            text.add("<comment>Options:</comment>");

            for (InputOption option : getOptions().values()) {
                String defaultValue;
                Object optionDefaultValue = option.getDefaultValue();

                if (
                        option.acceptValue()
                        && null != optionDefaultValue
                        && ((
                                !(optionDefaultValue instanceof Iterable)
                                || ((List<Object>) optionDefaultValue).isEmpty()
                        ) || (
                                !(optionDefaultValue instanceof Map)
                                || ((Map<String, Object>) optionDefaultValue).isEmpty()
                        ))
                ) {
                    defaultValue = String.format("<comment> (default: %s)</comment>", formatDefaultValue(option.getDefaultValue()));
                } else {
                    defaultValue = EMPTY;
                }

                String multiple = option.isArray() ? "<comment> (multiple values allowed)</comment>" : EMPTY;
                String description = option.getDescription().replace("\n", "\n" + repeat(' ', max + 2));

                int optionMax = max - option.getName().length() - 2;
                text.add(String.format(" <info>%s</info> %-" + optionMax + "s%s%s%s",
                    "--" + option.getName(),
                    null != option.getShortcut() ? String.format("(-%s) ", option.getShortcut()) : EMPTY,
                    description,
                    defaultValue,
                    multiple
                ));
            }

            text.add(EMPTY);
        }

        return join(text, "\n");
    }

    @SuppressWarnings("unchecked")
	private String formatDefaultValue(Object defaultValue) {
        // PHP's json_encode equivalent
        if (defaultValue instanceof Iterable<?>) {
            return String.format("[\"%s\"]", join((Iterable) defaultValue, "\",\""));
        } else if (defaultValue instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) defaultValue;
            StringBuilder sb = new StringBuilder();

            sb.append("{");
            for (Entry<?, ?> entry : map.entrySet()) {
                sb.append(String.format("\"%s\":\"%s\"", entry.getKey().toString(), entry.getValue().toString()));
            }
            sb.append("}");

            return sb.toString();
        } else if (defaultValue instanceof Boolean) {
            return ((Boolean) defaultValue).toString();
        }

        return String.format("\"%s\"", defaultValue.toString().replace("\n", EMPTY));
    }
}
