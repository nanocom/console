/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.*;
import org.nanocom.console.output.OutputInterface;

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
     * @throws RuntimeException If there is no data to read in the input stream
     */
    public String ask(OutputInterface output, List<String> questions, String defaultAnswer) {
        output.write(questions);

        String ret = System.console().readLine();
        if (null == ret) {
            throw new RuntimeException("Aborted");
        }
        ret = ret.trim();

        return ret.length() > 0 ? ret : defaultAnswer;
    }

    public String ask(OutputInterface output, List<String> questions) {
        return ask(output, questions, null);
    }

    public String ask(OutputInterface output, String question, String defaultValue) {
        return ask(output, Arrays.asList(question), defaultValue); // There's maybe something better to do
    }

    public String ask(OutputInterface output, String question) {
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
    public boolean askConfirmation(OutputInterface output, List<String> question, boolean defaultAnswer) {
        String answer = "z";
        while (isNotEmpty(answer) && !startsWithIgnoreCase(answer, "y") && !startsWithIgnoreCase(answer, "n")) {
            answer = ask(output, question, null);
        }

        if (false == defaultAnswer) {
            return isNotEmpty(answer) && startsWithIgnoreCase(answer, "y");
        }

        return isEmpty(answer) || startsWithIgnoreCase(answer, "y");
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
    public Object askAndValidate(OutputInterface output, List<String> question, Object validator, int attempts, String defaultAnswer) {
        RuntimeException error = null;
        while (attempts > 0) {
            attempts--;
            if (null != error) {
                output.writeln(((FormatterHelper) getHelperSet().get("formatter")).formatBlock(error.getMessage(), "error"));
            }

            // String value = ask(output, question, defaultAnswer);

            try {
                // TODO
                // return call_user_func(validator, value);
                return null;
            } catch (RuntimeException e) {
                error = e;
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
    public void setInputStream(InputStream stream) {
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
