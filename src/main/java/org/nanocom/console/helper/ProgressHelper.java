/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import static org.apache.commons.lang3.StringUtils.*;
import org.nanocom.console.exception.LogicException;
import org.nanocom.console.output.OutputInterface;

/**
 * The Progress class helper to display progress output.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class ProgressHelper extends Helper {

    private static final String FORMAT_QUIET         = " %percent%%";
    private static final String FORMAT_NORMAL        = " %current%/%max% [%bar%] %percent%%";
    private static final String FORMAT_VERBOSE       = " %current%/%max% [%bar%] %percent%% Elapsed: %elapsed%";
    private static final String FORMAT_QUIET_NOMAX   = " %current%";
    private static final String FORMAT_NORMAL_NOMAX  = " %current% [%bar%]";
    private static final String FORMAT_VERBOSE_NOMAX = " %current% [%bar%] Elapsed: %elapsed%";

    private static final char BACKSPACE = ')';

    // Options
    private int barWidth        = 28;
    private char barChar        = '=';
    private char emptyBarChar   = '-';
    private String progressChar = ">";
    private String format       = null;
    private int redrawFreq      = 1;

    private char barCharOriginal;

    private OutputInterface output;

    /**
     * Current step
     */
    private int current;

    /**
     * Maximum number of steps
     */
    private int max;

    /**
     * Start time of the progress bar
     */
    private Long startTime;

    /**
     * List of formatting variables
     */
    private String[] defaultFormatVars = new String[] {
        "current",
        "max",
        "bar",
        "percent",
        "elapsed",
    };

    /**
     * Available formatting variables
     */
    private Map<String, Boolean> formatVars;

    /**
     * Stored format part widths (used for padding)
     */
    private Map<String, Integer> widths;

    /**
     * Various time formats
     */
    private Object[][] timeFormats = new Object[][] {
        new Object[] {0, "???"},
        new Object[] {2, "1 sec"},
        new Object[] {59, "secs", 1},
        new Object[] {60, "1 min"},
        new Object[] {3600, "mins", 60},
        new Object[] {5400, "1 hr"},
        new Object[] {86400, "hrs", 3600},
        new Object[] {129600, "1 day"},
        new Object[] {604800, "days", 86400}
    };

    public ProgressHelper() {
        widths = new HashMap<String, Integer>();
        widths.put("current", 4);
        widths.put("max", 4);
        widths.put("percent", 3);
        widths.put("elapsed", 6);
    }

    /**
     * Sets the progress bar width.
     *
     * @param size The progress bar size
     */
    public void setBarWidth(int size) {
        barWidth = size;
    }

    /**
     * Sets the bar character.
     *
     * @param ch A character
     */
    public void setBarCharacter(char ch) {
        barChar = ch;
    }

    /**
     * Sets the empty bar character.
     *
     * @param char A character
     */
    public void setEmptyBarCharacter(char ch) {
        emptyBarChar = ch;
    }

    /**
     * Sets the progress bar character.
     *
     * @param char A character
     */
    public void setProgressCharacter(String ch) {
        progressChar = ch;
    }

    /**
     * Sets the progress bar format.
     *
     * @param format The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Sets the redraw frequency.
     *
     * @param freq The frequency in seconds
     */
    public void setRedrawFrequency(int freq) {
        redrawFreq = freq;
    }

    /**
     * Starts the progress output.
     *
     * @param output An Output instance
     * @param max    Maximum steps
     */
    public void start(OutputInterface output, int max) {
        startTime   = System.currentTimeMillis();
        current     = 0;
        this.max    = max;
        this.output = output;

        if (null == format) {
            switch (output.getVerbosity()) {
                case QUIET:
                    format = FORMAT_QUIET_NOMAX;
                    if (this.max > 0) {
                        this.format = FORMAT_QUIET;
                    }
                    break;

                case VERBOSE:
                    format = FORMAT_VERBOSE_NOMAX;
                    if (this.max > 0) {
                        this.format = FORMAT_VERBOSE;
                    }
                    break;

                default:
                    format = FORMAT_NORMAL_NOMAX;
                    if (this.max > 0) {
                        format = FORMAT_NORMAL;
                    }
                    break;
            }
        }

        initialize();
    }

    /**
     * Starts the progress output.
     *
     * @param output  An Output instance
     */
    public void start(OutputInterface output) {
        start(output, 0);
    }

    /**
     * Advances the progress output X steps.
     *
     * @param step   Number of steps to advance
     * @param redraw Whether to redraw or not
     */
    public void advance(int step, boolean redraw) {
        if (null == startTime) {
            throw new LogicException("You must start the progress bar before calling advance().");
        }

        if (0 == current) {
            redraw = true;
        }

        current += step;

        if (redraw || 0 == current % redrawFreq) {
            display();
        }
    }

    /**
     * Advances the progress output X steps.
     *
     * @param step Number of steps to advance
     */
    public void advance(int step) {
        advance(step, false);
    }

    /**
     * Advances the progress output 1 step.
     */
    public void advance() {
        advance(1);
    }

    /**
     * Outputs the current progress string.
     *
     * @param finish Forces the end result
     */
    public void display(boolean finish) {
        if (null == startTime) {
            throw new LogicException("You must start the progress bar before calling display().");
        }

        String message = format;
        for (Entry<String, String> val : generate(finish).entrySet()) {
            message = message.replaceAll("%" + val.getKey() + "%", val.getValue());
        }

        overwrite(output, message);
    }

    /**
     * Outputs the current progress string.
     */
    public void display() {
        display(false);
    }

    /**
     * Finishes the progress output.
     */
    public void finish()
    {
        if (null == startTime) {
            throw new LogicException("You must start the progress bar before calling finish().");
        }

        if (null != startTime) {
            if (0 == max) {
                barChar = barCharOriginal;
                display(true);
            }

            startTime = null;
            output.writeln("");
            output = null;
        }
    }

    /**
     * Initializes the progress helper.
     */
    private void initialize() {
        formatVars = new LinkedHashMap<String, Boolean>();
        for (String var : defaultFormatVars) {
            if (format.contains("%" + var + "%")) {
                formatVars.put(var, Boolean.TRUE);
            }
        }

        if (max > 0) {
            int maxWidth = String.valueOf(max).length();
            widths.put("max", maxWidth);
            widths.put("current", maxWidth);
        } else {
            barCharOriginal = barChar;
            barChar         = emptyBarChar;
        }
    }

    /**
     * Generates the map of format variables to values.
     *
     * @param finish Forces the end result
     *
     * @return A map of format vars and values
     */
    private Map<String, String> generate(boolean finish) {
        Map<String, String> vars = new LinkedHashMap<String, String>();
        Double percent = Double.valueOf(0);

        if (max > 0) {
            percent = round((double) current / max, 1);
        }

        if (formatVars.containsKey("bar")) {
            int completeBars;

            if (max > 0) {
                completeBars = Double.valueOf(Math.floor(percent * barWidth)).intValue();
            } else {
                completeBars = finish ? barWidth : Double.valueOf(Math.floor(current % barWidth)).intValue();
            }

            int emptyBars = barWidth - completeBars - progressChar.length();
            String bar = repeat(barChar, completeBars);
            if (completeBars < barWidth) {
                bar += progressChar;
                bar += repeat(emptyBarChar, emptyBars);
            }

            vars.put("bar", bar);
        }

        if (formatVars.containsKey("elapsed")) {
            long elapsed = System.currentTimeMillis() - startTime;
            vars.put("elapsed", leftPad(humaneTime(elapsed), widths.get("elapsed"), " "));
        }

        if (formatVars.containsKey("current")) {
            vars.put("current", leftPad(String.valueOf(current), widths.get("current"), " "));
        }

        if (formatVars.containsKey("max")) {
            vars.put("max", String.valueOf(max));
        }

        if (formatVars.containsKey("percent")) {
            vars.put("percent", leftPad(String.valueOf(percent * 100), widths.get("percent"), " "));
        }

        return vars;
    }

    private double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }

    /**
     * Converts seconds into human-readable format.
     *
     * @param secs Number of seconds
     *
     * @return Time in readable format
     */
    private String humaneTime(long secs) {
        String text = "???";

        for (Object[] form : timeFormats) {
            if (secs < (Integer) form[0]) {
                if (2 == form.length) {
                    text = (String) form[1];
                    break;
                } else {
                    text = Math.ceil(secs / (Integer) form[2]) + " " + (String) form[1];
                    break;
                }
            }
        }

        return text;
    }

    /**
     * Overwrites a previous message to the output.
     *
     * @param output  An Output instance
     * @param message The message
     * @param newline Whether to add a newline or not
     * @param size    The size of line
     */
    private void overwrite(OutputInterface output, String message, boolean newline, int size) {
        output.write(repeat(BACKSPACE, size));
        output.write(message, false);
        output.write(repeat(" ", size - message.length()));

        // Clean up the end line
        output.write(repeat(BACKSPACE, size - message.length()));

        if (newline) {
            output.writeln("");
        }
    }

    /**
     * Overwrites a previous message to the output.
     *
     * @param output  An Output instance
     * @param message The message
     * @param newline Whether to add a newline or not
     */
    private void overwrite(OutputInterface output, String message, boolean newline) {
        overwrite(output, message, newline, 80);
    }

    /**
     * Overwrites a previous message to the output.
     *
     * @param output  An Output instance
     * @param message The message
     */
    private void overwrite(OutputInterface output, String message) {
        overwrite(output, message, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "progress";
    }
}
