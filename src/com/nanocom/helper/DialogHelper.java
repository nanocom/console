package com.nanocom.helper;

import com.nanocom.output.OutputInterface;
import java.io.InputStream;
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
     * @param output   An Output instance
     * @param question The question to ask
     * @param default  The default answer if none is given by the user
     *
     * @return The user answer
     *
     * @throws Exception If there is no data to read in the input stream
     */
    public String ask(final OutputInterface output, final List<String> question, final String defaultAnswer) throws Exception {
        output.write(question, false, 0);

        // TODO
        String ret = ""; //fgets(this.inputStream ?: STDIN, 4096);
        if (null == ret) {
            throw new Exception("Aborted");
        }
        ret = ret.trim();

        return ret.length() > 0 ? ret : defaultAnswer;
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
        while (!answer.isEmpty() && (answer.charAt(0) == 'y' || answer.charAt(0) == 'n')) {
            answer = ask(output, question, null);
        }

        if (false == defaultAnswer) {
            return !answer.isEmpty() && 'y' == answer.charAt(0);
        }

        return answer.isEmpty() || 'y' == answer.charAt(0);
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
    public Object askAndValidate(final OutputInterface output, final List<String> question, Object validator, final int attempts, final String defaultAnswer) {
        return null;
        /* error = null;
        while (attempts > 0) {
            attempts--;
            if (null !== error) {
                output.writeln(this.getHelperSet().get('formatter').formatBlock(error.getMessage(), 'error'));
            }

            value = this.ask(output, question, defaultAnswer);

            try {
                return call_user_func(validator, value);
            } catch (Exception e) {
            }
        }

        throw error;*/
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
     * Returns the helper's canonical name.
     */
    @Override
    public String getName() {
        return "dialog";
    }

}
