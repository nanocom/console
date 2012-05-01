/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.nanocom.console.helper;

import com.nanocom.console.output.OutputInterface;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * The Dialog class provides helpers to interact with the user.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class DialogHelper extends Helper {

    private InputStream inputStream;

    /**
     * Asks a question to the user.
     *
     * @param output        An Output instance
     * @param question      The question to ask
     * @param defaultAnswer The default answer if none is given by the user
     *
     * @return The user answer
     *
     * @throws Exception If there is no data to read in the input stream
     */
    public String ask(final OutputInterface output, final List<String> questions, final String defaultAnswer) throws Exception {
        output.write(questions);

        String ret = System.console().readLine();
        if (null == ret) {
            throw new Exception("Aborted");
        }
        ret = ret.trim();

        return ret.length() > 0 ? ret : defaultAnswer;
    }

    public String ask(final OutputInterface output, final List<String> questions) throws Exception {
        return ask(output, questions, null);
    }

    public String ask(final OutputInterface output, final String question, final String defaultValue) throws Exception {
        return ask(output, Arrays.asList(question), defaultValue); // There's maybe something better to do
    }

    public String ask(OutputInterface output, final String question) throws Exception {
        return ask(output, question, null);
    }

    /**
     * Asks a confirmation to the user.
     *
     * The question will be asked until the user answers by nothing, yes, or no.
     *
     * @param output        An Output instance
     * @param question      The question to ask
     * @param defaultAnswer The default answer if the user enters nothing
     *
     * @return True if the user has confirmed, false otherwise
     */
    public boolean askConfirmation(final OutputInterface output, final List<String> question, final boolean defaultAnswer) throws Exception {
        String answer = "z";
        while (null != answer && !("y".equals(answer.substring(0, 1).toLowerCase()) || "n".equals(answer.substring(0, 1).toLowerCase()))) {
            answer = ask(output, question, null);
        }

        if (false == defaultAnswer) {
            return null != answer && "y".equals(answer.substring(0, 1).toLowerCase());
        }

        return null == answer || "y".equals(answer.substring(0, 1).toLowerCase());
    }

    /**
     * Asks for a value and validates the response.
     *
     * The validator receives the data to validate. It must return the
     * validated data when the data is valid and throw an exception
     * otherwise.
     *
     * @param output        An Output instance
     * @param question      The question to ask
     * @param callback      validator A PHP callback
     * @param attempts      Max number of times to ask before giving up (false by default, which means infinite)
     * @param defaultAnswer The default answer if none is given by the user
     *
     * @return
     *
     * @throws Exception When any of the validators return an error
     */
    public Object askAndValidate(final OutputInterface output, final List<String> question, Object validator, int attempts, final String defaultAnswer) throws Exception {
        Exception error = null;
        while (attempts > 0) {
            attempts--;
            if (null != error) {
                output.writeln(((FormatterHelper) getHelperSet().get("formatter")).formatBlock(error.getMessage(), "error"));
            }

            String value = ask(output, question, defaultAnswer);

            try {
                // TODO Imitate this behavior
                // return call_user_func(validator, value);
                return null;
            } catch (Exception e) {
            }
        }

        throw error;
    }

    /**
     * Sets the input stream to read from when interacting with the user.
     *
     * This is mainly useful for testing purpose.
     *
     * @param stream The input stream
     */
    public void setInputStream(final InputStream stream) {
        this.inputStream = stream;
    }

    /**
     * Returns the helper's input stream
     *
     * @return
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Returns the helper's canonical name.
     */
    @Override
    public String getName() {
        return "dialog";
    }

}
