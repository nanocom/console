/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Input is the base class for all concrete Input classes.
 *
 * Three concrete classes are provided by default:
 *
 *  * `ArgvInput`:   The input comes from the CLI arguments (argv)
 *  * `StringInput`: The input is provided as a string
 *  * `ArrayInput`:  The input is provided as an array
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
abstract class Input implements InputInterface {

    protected InputDefinition     definition;
    protected Map<String, Object> options;
    protected Map<String, Object> arguments;
    protected Boolean             interactive = true;

    /**
     * Constructor.
     *
     * @param definition An InputDefinition instance
     */

    public void init(InputDefinition definition) throws Exception {
        if (null == definition) {
            this.definition = new InputDefinition();
        } else {
            bind(definition);
            validate();
        }
    }

    /**
     * Binds the current Input instance with the given arguments and options.
     *
     * @param definition An InputDefinition instance
     */
    @Override
    public void bind(InputDefinition definition) throws Exception {
        arguments = new HashMap<String, Object>();
        options = new HashMap<String, Object>();
        this.definition = definition;

        parse();
    }

    /**
     * Processes command line arguments.
     */
    abstract protected void parse() throws Exception;

    /**
     * Validates the input.
     *
     * @throws Exception When not enough arguments are given
     */
    @Override
    public void validate() throws Exception {
        if (arguments.size() < definition.getArgumentRequiredCount()) {
            throw new Exception("Not enough arguments.");
        }
    }

    /**
     * Checks if the input is interactive.
     *
     * @return True if the input is interactive
     */
    @Override
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * Sets the input interactivity.
     *
     * @param interactive If the input should be interactive
     */
    @Override
    public void setInteractive(final boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * Returns the argument values.
     *
     * @return An array of argument values
     */
    @Override
    public Map<String, Object> getArguments() {
        Map<String, Object> toReturn = definition.getArgumentDefaults();
        toReturn.putAll(arguments);
 
        return toReturn;
    }

    /**
     * Returns the argument value for a given argument name.
     *
     * @param name The argument name
     *
     * @return The argument value
     *
     * @throws Exception When argument given doesn't exist
     */
    @Override
    public Object getArgument(final String name) throws Exception {
        if (!definition.hasArgument(name)) {
            throw new Exception("The \"" + name + "\" argument does not exist.");
        }

        return arguments.containsKey(name) ? arguments.get(name) : definition.getArgument(name).getDefaultValue();
    }

    /**
     * Sets an argument value by name.
     *
     * @param name  The argument name
     * @param value The argument value
     *
     * @throws Exception When argument given doesn't exist
     */
    @Override
    public void setArgument(final String name, final String value) throws Exception {
        if (!definition.hasArgument(name)) {
            throw new Exception("The \"" + name + "\" argument does not exist.");
        }

        arguments.put(name, value);
    }

    /**
     * Returns true if an InputArgument object exists by name.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    @Override
    public boolean hasArgument(final String name) {
        return definition.hasArgument(name);
    }

    /**
     * Returns true if an InputArgument object exists by position.
     *
     * @param name The InputArgument name or position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    @Override
    public boolean hasArgument(final int position) {
        return definition.hasArgument(position);
    }

    /**
     * Returns the options values.
     *
     * @return array An array of option values
     */
    @Override
    public Map<String, Object> getOptions() {
        Map<String, Object> toReturn = definition.getOptionDefaults();
        toReturn.putAll(options);
 
        return toReturn;
    }

    /**
     * Returns the option value for a given option name.
     *
     * @param name The option name
     *
     * @return The option value
     *
     * @throws Exception When option given doesn't exist
     */
    @Override
    public Object getOption(final String name) throws Exception {
        if (!definition.hasOption(name)) {
            throw new Exception(String.format("The \"%s\" option does not exist.", name));
        }

        return options.containsKey(name) ? options.get(name) : definition.getOption(name).getDefaultValue();
    }

    /**
     * Sets an option value by name.
     *
     * @param name  The option name
     * @param value The option value
     *
     * @throws Exception When option given doesn't exist
     */
    @Override
    public void setOption(final String name, final String value) throws Exception {
        if (!definition.hasOption(name)) {
            throw new Exception("The \"" + name + "\" option does not exist.");
        }

        options.put(name, value);
    }

    /**
     * Returns true if an InputOption object exists by name.
     *
     * @param name The InputOption name
     *
     * @return True if the InputOption object exists, false otherwise
     */
    @Override
    public boolean hasOption(final String name) {
        return definition.hasOption(name);
    }

}
