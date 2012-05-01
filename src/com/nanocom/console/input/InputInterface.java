/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.input;

import java.util.List;
import java.util.Map;

/**
 * InputInterface is the interface implemented by all input classes.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public interface InputInterface {

    /**
     * Returns the first argument from the raw parameters (not parsed).
     *
     * @return The value of the first argument or null otherwise
     */
    String getFirstArgument();

    /**
     * Returns true if the raw parameters (not parsed) contain a value.
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The values to look for in the raw parameters
     *
     * @return True if the value is contained in the raw parameters
     */
    boolean hasParameterOption(final String value);

    boolean hasParameterOption(final List<String> values);

    boolean hasParameterOption(final Map<String, String> values);

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param value The value(s) to look for in the raw parameters
     * @param defaultValue The default value to return if no result is found
     *
     * @return The option value
     */
    Object getParameterOption(final String value, final Object defaultValue);

    Object getParameterOption(final List<String> values, final Object defaultValue);

    Object getParameterOption(final String value);

    Object getParameterOption(final List<String> values);

    /**
     * Binds the current Input instance with the given arguments and options.
     *
     * @param definition A InputDefinition instance
     */
    void bind(InputDefinition definition) throws Exception;

    /**
     * Validates if arguments given are correct.
     *
     * @throws Exception When not enough arguments are given.
     */
    void validate() throws Exception;

    /**
     * Returns all the given arguments merged with the default values.
     *
     * @return
     */
    Map<String, Object> getArguments();

    /**
     * Gets argument by name.
     *
     * @param name The name of the argument
     *
     * @return
     */
    Object getArgument(final String name) throws Exception;

    /**
     * Sets an argument value by name.
     *
     * @param name  The argument name
     * @param value The argument value
     *
     * @throws Exception When argument given doesn't exist
     */
    void setArgument(final String name, final String value) throws Exception;

    /**
     * Returns true if an InputArgument object exists by name.
     *
     * @param name The InputArgument name
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    boolean hasArgument(final String name);
    
    /**
     * Returns true if an InputArgument object exists by position.
     *
     * @param position The InputArgument position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    boolean hasArgument(final int position);

    /**
     * Returns all the given options merged with the default values.
     *
     * @return
     */
    Map<String, Object> getOptions();

    /**
     * Gets an option by name.
     *
     * @param name The name of the option
     *
     * @return mixed
     */
    Object getOption(final String name) throws Exception;

    /**
     * Sets an option value by name.
     *
     * @param name  The option name
     * @param value The option value
     *
     * @throws Exception When option given doesn't exist
     */
    void setOption(final String name, final String value) throws Exception;

    /**
     * Returns true if an InputOption object exists by name.
     *
     * @param name The InputOption name
     *
     * @return True if the InputOption object exists, false otherwise
     */
    boolean hasOption(final String name);

    /**
     * Is this input means interactive?
     *
     * @return
     */
    boolean isInteractive();

    /**
     * Sets the input interactivity.
     *
     * @param interactive If the input should be interactive
     */
    void setInteractive(final boolean interactive);

}
