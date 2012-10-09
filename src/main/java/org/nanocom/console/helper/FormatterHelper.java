/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.*;
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

    /**
     * Formats a message within a section.
     *
     * @param section The section name
     * @param message The message
     *
     * @return The formatted section
     */
    public String formatSection(String section, String message) {
        return formatSection(section, message, "info");
    }

    /**
     * Formats a message as a block of text.
     *
     * @param messages The messages to write in the block
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
            len = Math.max(length(message) + (large ? 4 : 2), len);
        }

        messages = new ArrayList<String>();
        if (large) {
            messages.add(repeat(' ', len));
        }

        for (String line : lines) {
            messages.add(line + repeat(' ', len - length(line)));
        }

        if (large) {
            messages.add(repeat(' ', len));
        }

        lines = new ArrayList<String>(messages.size());
        for (String message : messages) {
            lines.add(String.format("<%s>%s</%s>", style, message, style));
        }

        return join(lines, "\n");
    }

    /**
     * Formats a message as a block of text.
     *
     * @param message The message to write in the block
     * @param style   The style to apply to the whole block
     * @param large   Whether to return a large block
     *
     * @return The formatter message
     */
    public String formatBlock(String message, String style, boolean large) {
        List<String> messages = new ArrayList<String>();
        messages.add(message);

        return formatBlock(messages, style, large);
    }

    /**
     * Formats a message as a block of text.
     *
     * @param messages The messages to write in the block
     * @param style    The style to apply to the whole block
     *
     * @return The formatter message
     */
    public String formatBlock(List<String> messages, String style) {
        return formatBlock(messages, style, false);
    }

    /**
     * Formats a message as a block of text.
     *
     * @param message The message to write in the block
     * @param style   The style to apply to the whole block
     *
     * @return The formatter message
     */
    public String formatBlock(String message, String style) {
        return formatBlock(message, style, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "formatter";
    }
}
