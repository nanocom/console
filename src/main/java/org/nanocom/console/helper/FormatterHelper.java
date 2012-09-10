/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nanocom.console.formatter.OutputFormatter;

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
    public String formatSection(String section, String message, String style) {
        return String.format("<%s>[%s]</%s> %s", style, section, style, message);
    }

    public String formatSection(String section, String message) {
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
    public String formatBlock(List<String> messages, String style, boolean large) {
        int len = 0;
        List<String> lines = new ArrayList<String>();
        for (String message : messages) {
            message = OutputFormatter.escape(message);
            lines.add(String.format(large ? "  %s  " : " %s ", message));
            len = Math.max(StringUtils.length(message) + (large ? 4 : 2), len);
        }

        $messages = $large ? array(str_repeat(' ', $len)) : array();
        foreach ($lines as $line) {
            $messages[] = $line.str_repeat(' ', $len - $this->strlen($line));
        }
        if ($large) {
            $messages[] = str_repeat(' ', $len);
        }

        foreach ($messages as &$message) {
            $message = sprintf('<%s>%s</%s>', $style, $message, $style);
        }

        return implode("\n", $messages);
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
     * Returns the helper's canonical name.
     *
     * @return The canonical name of the helper
     */
    @Override
    public String getName() {
        return "formatter";
    }
}
