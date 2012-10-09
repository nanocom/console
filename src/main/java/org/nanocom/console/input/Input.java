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
    public Input(InputDefinition definition) {
    	init(definition);
    }

    /**
     * Constructor.
     */
    public Input() {
    	init(null);
    }

    protected final void init(InputDefinition definition) {
        if (null == definition) {
            this.definition = new InputDefinition();
        } else {
            bind(definition);
            validate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(InputDefinition definition) {
        arguments = new HashMap<String, Object>();
        options = new HashMap<String, Object>();
        this.definition = definition;

        parse();
    }

    /**
     * Processes command line arguments.
     */
    abstract protected void parse() throws RuntimeException;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() throws RuntimeException {
        if (arguments.size() < definition.getArgumentRequiredCount()) {
            throw new RuntimeException("Not enough arguments.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInteractive() {
        return interactive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getArguments() {
        Map<String, Object> toReturn = definition.getArgumentDefaults();
        toReturn.putAll(arguments);

        return toReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getArgument(String name) throws IllegalArgumentException {
        if (!definition.hasArgument(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", name));
        }

        return arguments.containsKey(name) ? arguments.get(name) : definition.getArgument(name).getDefaultValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArgument(String name, String value) throws IllegalArgumentException {
        if (!definition.hasArgument(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" argument does not exist.", name));
        }

        arguments.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasArgument(String name) {
        return definition.hasArgument(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasArgument(int position) {
        return definition.hasArgument(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getOptions() {
        Map<String, Object> toReturn = definition.getOptionDefaults();
        toReturn.putAll(options);

        return toReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOption(String name) throws IllegalArgumentException {
        if (!definition.hasOption(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" option does not exist.", name));
        }

        return options.containsKey(name) ? options.get(name) : definition.getOption(name).getDefaultValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOption(String name, String value) throws IllegalArgumentException {
        if (!definition.hasOption(name)) {
            throw new IllegalArgumentException(String.format("The \"%s\" option does not exist.", name));
        }

        options.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasOption(String name) {
        return definition.hasOption(name);
    }
}
