/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.input;

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
     * @param value The value to look for in the raw parameters
     *
     * @return True if the value is contained in the raw parameters
     */
    boolean hasParameterOption(String value);

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
    boolean hasParameterOption(List<String> values);

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
    boolean hasParameterOption(Map<String, String> values);

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param value        The value to look for in the raw parameters
     * @param defaultValue The default value to return if no result is found
     *
     * @return The option value
     */
    Object getParameterOption(String value, Object defaultValue);

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values       The values to look for in the raw parameters
     * @param defaultValue The default value to return if no result is found
     *
     * @return The option value
     */
    Object getParameterOption(List<String> values, Object defaultValue);

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param value The value to look for in the raw parameters
     *
     * @return The option value
     */
    Object getParameterOption(String value);

    /**
     * Returns the value of a raw option (not parsed).
     *
     * This method is to be used to introspect the input parameters
     * before they have been validated. It must be used carefully.
     *
     * @param values The values to look for in the raw parameters
     *
     * @return The option value
     */
    Object getParameterOption(List<String> values);

    /**
     * Binds the current Input instance with the given arguments and options.
     *
     * @param definition A InputDefinition instance
     */
    void bind(InputDefinition definition);

    /**
     * Validates if arguments given are correct.
     *
     * @throws RuntimeException When not enough arguments are given.
     */
    void validate() throws RuntimeException;

    /**
     * Returns all the given arguments merged with the default values.
     *
     * @return An array of argument values
     */
    Map<String, Object> getArguments();

    /**
     * Returns the argument value for a given argument name.
     *
     * @param name The argument name
     *
     * @return The argument value
     *
     * @throws IllegalArgumentException When argument given doesn't exist
     */
    Object getArgument(String name);

    /**
     * Sets an argument value by name.
     *
     * @param name  The argument name
     * @param value The argument value
     *
     * @throws IllegalArgumentException When argument given doesn't exist
     */
    void setArgument(String name, String value);

    /**
     * Returns true if an InputArgument object exists by name.
     *
     * @param name The InputArgument name
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    boolean hasArgument(String name);

    /**
     * Returns true if an InputArgument object exists by position.
     *
     * @param position The InputArgument position
     *
     * @return True if the InputArgument object exists, false otherwise
     */
    boolean hasArgument(int position);

    /**
     * Returns all the given options merged with the default values.
     *
     * @return An array of option values
     */
    Map<String, Object> getOptions();

    /**
     * Returns the option value for a given option name.
     *
     * @param name The option name
     *
     * @return The option value
     *
     * @throws IllegalArgumentException When option given doesn't exist
     */
    Object getOption(String name);

    /**
     * Sets an option value by name.
     *
     * @param name  The option name
     * @param value The option value
     *
     * @throws IllegalArgumentException When option given doesn't exist
     */
    void setOption(String name, String value);

    /**
     * Returns true if an InputOption object exists by name.
     *
     * @param name The InputOption name
     *
     * @return True if the InputOption object exists, false otherwise
     */
    boolean hasOption(String name);

    /**
     * Checks if the input is interactive.
     *
     * @return True if the input is interactive
     */
    boolean isInteractive();

    /**
     * Sets the input interactivity.
     *
     * @param interactive If the input should be interactive
     */
    void setInteractive(boolean interactive);
}
