/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import org.nanocom.console.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.apache.commons.lang3.StringUtils;

/**
 * The Formatter class provides helpers to format messages.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class FormatterHelper extends Helper {

    /**
     * Formats a message within a section.
     *
     * @param section The section name
     * @param message The message
     * @param style   The style to apply to the section
     *
     * @return The formatted section
     */
    public String formatSection(final String section, final String message, final String style) {
        return String.format("<%s>[%s]</%s> %s", style, section, style, message);
    }

    public String formatSection(final String section, final String message) {
        return formatSection(section, message, "info");
    }

    /**
     * Formats a message as a block of text.
     *
     * @param messages The message to write in the block
     * @param style    The style to apply to the whole block
     * @param large    Whether to return a large block
     *
     * @return The formatter message
     */
    public String formatBlock(List<String> messages, final String style, final boolean large) {
        int len = 0;
        List<String> lines = new ArrayList<String>();
        for (String message : messages) {
            lines.add(String.format(large ? "  %s  " : " %s ", message));
            len = Math.max(strlen(message) + (large ? 4 : 2), len);
        }

        // TODO Implement this part
        /*messages = large ? Arrays.asList(StringUtils.repeat(" ", len)) : new ArrayList<String>();
        for (String line : lines) {
            messages.add(line + StringUtils.repeat(" ", len - strlen(line)));
        }
        if (large) {
            messages.add(StringUtils.repeat(" ", len));
        }*/

        for (String message : messages) {
            message = String.format("<%s>%s</%s>", style, message, style);
        }

        return Util.implode("\n", (String[]) messages.toArray());
    }

    public String formatBlock(final String message, final String style, final boolean large) {
        List<String> messages = new ArrayList<String>();
        messages.add(message);

        return formatBlock(messages, style, large);
    }

    public String formatBlock(final List<String> messages, final String style) {
        return formatBlock(messages, style, false);
    }

    public String formatBlock(final String message, final String style) {
        return formatBlock(message, style, false);
    }

    /**
     * Returns the length of a string, using mb_strlen if it is available.
     *
     * @param string The string to check its length
     *
     * @return The length of the string
     */
    private int strlen(final String string) {
        return string.length(); // TODO check that encoding is managed
    }

    /**
     * Returns the helper's canonical name.
     *
     * @return The canonical name of the helper
     */
    @Override
    public String getName() {
        return "formatter";
    }

}
