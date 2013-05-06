/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.nanocom.console.formatter.OutputFormatterStyle;
import org.nanocom.console.output.OutputInterface;

/**
 * A helper to interact with the user.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class DialogHelper extends Helper {

    private static Boolean stty;
    private static String shell;

    private BufferedReader reader;

    /**
     * Asks the user to select a value.
     *
     * @param output        An Output instance
     * @param question      The question to ask
     * @param choices       List of choices to pick from
     * @param defaultAnswer The default answer if the user enters nothing
     * @param attempts      Max number of times to ask before giving up (false by default, which means infinite)
     * @param errorMessage  Message which will be shown if invalid value from choice list would be picked
     * @param multiselect   Select more than one value separated by comma
     *
     * @return The selected value or values (the key of the choices array)
     *
     * @throws IllegalArgumentException
     */
    public String[] select(OutputInterface output, String question, Map<String, String> choices, String defaultAnswer, int attempts, final String errorMessage, boolean multiselect) {
        int width = 0;
        for (String key : choices.keySet()) {
            width = Math.max(width, key.length());
        }

        List<String> messages = new ArrayList<String>();
        messages.add(question);

        for (Map.Entry<String, String> choice : choices.entrySet()) {
            messages.add(String.format("  [<info>%-" + width + "s</info>] %s", choice.getKey(), choice.getValue()));
        }

        output.writeln(messages);

        String[] result = askAndValidate(output, "> ", new Validator() {

            @Override
            public String[] validate(Map<String, String> choices, String picked) {
                if (null == choices.get(picked)) {
                    throw new IllegalArgumentException(String.format(errorMessage, picked));
                }

                return new String[] {picked};
            }
        }, attempts, defaultAnswer);

        return result;
    }

    public String[] select(OutputInterface output, String question, Map<String, String> choices, String defaultAnswer, int attempts, String errorMessage) {
        return select(output, question, choices, defaultAnswer, attempts, errorMessage, false);
    }

    public String[] select(OutputInterface output, String question, Map<String, String> choices, String defaultAnswer, int attempts) {
        return select(output, question, choices, defaultAnswer, attempts, "Value \"%s\" is invalid");
    }

    public String[] select(OutputInterface output, String question, Map<String, String> choices, String defaultAnswer) {
        return select(output, question, choices, defaultAnswer, -1);
    }

    public String[] select(OutputInterface output, String question, Map<String, String> choices) {
        return select(output, question, choices, null);
    }


    /**
     * Asks a question to the user.
     *
     * @param output        An Output instance
     * @param question      The question to ask
     * @param defaultAnswer The default answer if none is given by the user
     * @param autocomplete  List of values to autocomplete
     *
     * @return The user answer
     *
     * @throws RuntimeException If there is no data to read in the input stream
     */
    public String ask(OutputInterface output, String question, String defaultAnswer, List<String> autocomplete) throws IOException {
        output.write(question);

        if (null == reader) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String ret;
        if (null == autocomplete || false == hasSttyAvailable()) {
            ret = reader.readLine();
            if (null == ret) {
                throw new RuntimeException("Aborted");
            }
            ret = ret.trim();
        } else {
            ret = "";

            int i = 0;
            int ofs = -1;
            List<String> matches = autocomplete;
            int numMatches = matches.size();

            String sttyMode = shellExec("stty -g");

            // Disable icanon (so we can fread each keypress) and echo (we'll do echoing here instead)
            shellExec("stty -icanon -echo");

            // Add highlighted text style
            output.getFormatter().setStyle("hl", new OutputFormatterStyle("black", "white"));

            // Read a keypress
            char[] c = new char[3];
            while (true) {
                reader.read(c, 0, 1);

                // Backspace Character
                if ("\177".equals(String.valueOf(c))) {
                    if (0 == numMatches && 0 != i) {
                        i--;
                        // Move cursor backwards
                        output.write("\033[1D");
                    }

                    if (i == 0) {
                        ofs = -1;
                        matches = autocomplete;
                        numMatches = matches.size();
                    } else {
                        numMatches = 0;
                    }

                    // Pop the last character off the end of our string
                    ret = ret.substring(0, i);
                } else if ("\033".equals(String.valueOf(c))) { // Did we read an escape sequence?
                    reader.read(c, 1, 2);

                    // A = Up Arrow. B = Down Arrow
                    if ('A' == c[2] || 'B' == c[2]) {
                        if ('A' == c[2] && -1 == ofs) {
                            ofs = 0;
                        }

                        if (0 == numMatches) {
                            continue;
                        }

                        ofs += ('A' == c[2]) ? -1 : 1;
                        ofs = (numMatches + ofs) % numMatches;
                    }
                } else if (c[0] < 32) {
                    if ('\t' == c[0] || '\n' == c[0]) {
                        if (numMatches > 0 && -1 != ofs) {
                            ret = matches.get(ofs);
                            // Echo out remaining chars for current match
                            output.write(ret.substring(i));
                            i = ret.length();
                        }

                        if ('\n' == c[0]) {
                            output.write(String.valueOf(c));
                            break;
                        }

                        numMatches = 0;
                    }

                    continue;
                } else {
                    output.write(String.valueOf(c));
                    ret += c;
                    i++;

                    numMatches = 0;
                    ofs = 0;

                    for (String value : autocomplete) {
                        // If typed characters match the beginning chunk of value (e.g. [AcmeDe]moBundle)
                        if (0 == value.indexOf(ret) && i != value.length()) {
                            numMatches++;
                            matches.add(value);
                        }
                    }
                }

                // Erase characters from cursor to end of line
                output.write("\033[K");

                if (numMatches > 0 && -1 != ofs) {
                    // Save cursor position
                    output.write("\0337");
                    // Write highlighted text
                    output.write(String.format("<hl>%s</hl>", matches.get(ofs).substring(i)));
                    // Restore cursor position
                    output.write("\0338");
                }
            }

            // Reset stty so it behaves normally again
            shellExec(String.format("stty %s", sttyMode));
        }

        return 0 < ret.length() ? ret : defaultAnswer;
    }

    public String ask(OutputInterface output, String question, String defaultAnswer) throws IOException {
        return ask(output, question, defaultAnswer, null);
    }

    public String ask(OutputInterface output, String question) throws IOException {
        return ask(output, question, null);
    }

    /**
     * Asks a confirmation to the user.
     *
     * The question will be asked until the user answers by nothing, yes, or no.
     *
     * @param OutputInterface output   An Output instance
     * @param string|array    question The question to ask
     * @param Boolean         default  The default answer if the user enters nothing
     *
     * @return Boolean true if the user has confirmed, false otherwise
     */
    public boolean askConfirmation(OutputInterface output, String question, Boolean defaultAnswer) throws IOException {
        String answer = "z";
        char firstLetter = 0;
        while ('y' != firstLetter && 'n' != firstLetter) {
            answer = ask(output, question);
            firstLetter = answer.toLowerCase().charAt(0);
        }

        if (false == defaultAnswer) {
            return null != answer && 'y' == firstLetter;
        }

        return null != answer || 'y' == firstLetter;
    }

    public boolean askConfirmation(OutputInterface output, String question) throws IOException {
        return askConfirmation(output, question, true);
    }

    /**
     * Asks a question to the user, the response is hidden
     *
     * @param OutputInterface output   An Output instance
     * @param string|array    question The question
     * @param Boolean         fallback In case the response can not be hidden, whether to fallback on non-hidden question or not
     *
     * @return The answer
     *
     * @throws \RuntimeException In case the fallback is deactivated and the response can not be hidden
     */
    public String askHiddenResponse(OutputInterface output, String question, boolean fallback) throws IOException {
        /*if (SystemUtils.IS_OS_WINDOWS) {
            URL hidddeninput = getClass().getClassLoader().getResource("hiddeninput.exe");
            InputStream in = hidddeninput.openStream();
            String tempFile = SystemUtils.getJavaIoTmpDir().getAbsolutePath() + "/hiddeninput.exe";
            OutputStream out = new FileOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
               out.write(buf, 0, len);
            }
            in.close();
            out.close();

            output.write(question);
            String value = new BufferedReader(new BufferedInputStream(Runtime.getRuntime().exec(tempFile).getInputStream())).;
            output.writeln("");

            if (isset(tmpExe)) {
                unlink(tmpExe);
            }

            return value;
        }*/

        if (hasSttyAvailable()) {
            output.write(question);

            String sttyMode = shellExec("stty -g");

            shellExec("stty -echo");
            String value = reader.readLine();
            shellExec(String.format("stty %s", sttyMode));

            if (null == value) {
                throw new RuntimeException("Aborted");
            }

            value = value.trim();
            output.writeln("");

            return value;
        }

        if (null != (shell = getShell())) {
            output.write(question);
            String readCmd = "csh".equals(shell) ? "set mypassword = <" : "read -r mypassword";
            String command = String.format("/usr/bin/env %s -c 'stty -echo; %s; stty echo; echo \\mypassword'", shell, readCmd);
            String value = shellExec(command);
            output.writeln("");

            return value;
        }

        if (fallback) {
            return ask(output, question);
        }

        throw new RuntimeException("Unable to hide the response");
    }

    /**
     * Asks for a value and validates the response.
     *
     * The validator receives the data to validate. It must return the
     * validated data when the data is valid and throw an exception
     * otherwise.
     *
     * @param OutputInterface output       An Output instance
     * @param string|array    question     The question to ask
     * @param callable        validator    A PHP callback
     * @param integer         attempts     Max number of times to ask before giving up (false by default, which means infinite)
     * @param string          default      The default answer if none is given by the user
     * @param array           autocomplete List of values to autocomplete
     *
     * @return
     *
     * @throws Exception When any of the validators return an error
     */
    public String[] askAndValidate(final OutputInterface output, final String question, Validator validator, int attempts, final String defaultAnswer, final List<String> autocomplete) {
        Interviewer interviewer = new Interviewer() {

            @Override
            public String interview() throws Exception {
                return ask(output, question, defaultAnswer, autocomplete);
            }
        };

        return validateAttempts(interviewer, output, validator, attempts);
    }

    public String[] askAndValidate(final OutputInterface output, final String question, Validator validator, int attempts, final String defaultAnswer) {
        return askAndValidate(output, question, validator, attempts, defaultAnswer, null);
    }

    public String[] askAndValidate(final OutputInterface output, final String question, Validator validator, int attempts) {
        return askAndValidate(output, question, validator, attempts, null, null);
    }

    public String[] askAndValidate(final OutputInterface output, final String question, Validator validator) {
        return askAndValidate(output, question, validator, -1, null, null);
    }

    /**
     * Asks for a value, hides and validates the response.
     *
     * The validator receives the data to validate. It must return the
     * validated data when the data is valid and throw an exception
     * otherwise.
     *
     * @param output    An Output instance
     * @param question  The question to ask
     * @param validator A PHP callback
     * @param attempts  Max number of times to ask before giving up (false by default, which means infinite)
     * @param fallback  In case the response can not be hidden, whether to fallback on non-hidden question or not
     *
     * @return The response
     *
     * @throws Exception        When any of the validators return an error
     * @throws RuntimeException In case the fallback is deactivated and the response can not be hidden
     *
     */
    public String[] askHiddenResponseAndValidate(final OutputInterface output, final String question, Validator validator, int attempts, final boolean fallback) {
        Interviewer interviewer = new Interviewer() {

            @Override
            public String interview() throws Exception {
                return askHiddenResponse(output, question, fallback);
            }
        };

        return validateAttempts(interviewer, output, validator, attempts);
    }

    /**
     * Sets the input stream to read from when interacting with the user.
     *
     * This is mainly useful for testing purpose.
     *
     * @param reader The reader
     */
    public void setInputReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Returns the helper's input reader.
     *
     * @return
     */
    public BufferedReader getInputReader() {
        return reader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "dialog";
    }

    /**
     * Returns a valid unix shell.
     *
     * @return The valid shell name, null in case no valid shell is found
     */
    private String getShell() throws IOException {
        if (null != shell) {
            return shell;
        }

        if (new File("/usr/bin/env").exists()) {
            // handle other OSs with bash/zsh/ksh/csh if available to hide the answer
            String test = "/usr/bin/env %s -c 'echo OK' 2> /dev/null";
            for (String sh : Arrays.asList("bash", "zsh", "ksh", "csh")) {
                if ("OK".equals(shellExec(String.format(test, sh)))) {
                    return shell = sh;
                }
            }
        }

        return null;
    }

    private boolean hasSttyAvailable() throws IOException {
        if (null != stty) {
            return stty;
        }

        int exitCode = Runtime.getRuntime().exec("stty 2>&1").exitValue();

        return stty = 0 == exitCode;
    }

    /**
     * Validates an attempt.
     *
     * @param interviewer A callable that will ask for a question and return the result
     * @param output      An Output instance
     * @param validator   A PHP callback
     * @param attempts    Max number of times to ask before giving up ; false will ask infinitely
     *
     * @return The validated response
     *
     * @throws RuntimeException In case the max number of attempts has been reached and no valid response has been given
     */
    private String[] validateAttempts(Interviewer interviewer, OutputInterface output, Validator validator, int attempts) {
        RuntimeException error = null;
        while (0 > attempts || 0 < --attempts) {
            if (null != error) {
                FormatterHelper formatterHelper = getHelperSet().get("formatter");
                output.writeln(formatterHelper.formatBlock(error.getMessage(), "error"));
            }

            try {
                return validator.validate(null, interviewer.interview()); // FIXME
            } catch (Exception ex) {
                error = new RuntimeException(ex);
            }
        }

        throw error;
    }

    private String shellExec(String command) throws IOException {
        InputStream output = Runtime.getRuntime().exec(command).getInputStream();
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(output));

        return outputReader.readLine();
    }

    public interface Validator {

        String[] validate(Map<String, String> choices, String picked);
    }

    public interface Interviewer {

        String interview() throws Exception;
    }
}
