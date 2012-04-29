package com.nanocom.console.helper;

import com.nanocom.console.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * The Formatter class provides helpers to format messages.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class FormatterHelper /*extends Helper*/ {

//    /**
//     * Formats a message within a section.
//     *
//     * @param section The section name
//     * @param message The message
//     * @param style   The style to apply to the section
//     * 
//     * @return The formatted section
//     */
//    public String formatSection(final String section, final String message, final String style) {
//        return String.format("<%s>[%s]</%s> %s", style, section, style, message);
//    }
//
//    public String formatSection(final String section, final String message) {
//        return formatSection(section, message, "info");
//    }
//
//    /**
//     * Formats a message as a block of text.
//     *
//     * @param messages The message to write in the block
//     * @param style    The style to apply to the whole block
//     * @param large    Whether to return a large block
//     *
//     * @return The formatter message
//     */
//    public String formatBlock(final List<String> messages, final String style, final boolean large) {
//        int len = 0;
//        List<String> lines = new ArrayList<String>();
//        for (String message : messages) {
//            lines.add(String.format(large ? "  %s  " : " %s ", message));
//            len = Math.max(strlen(message) + (large ? 4 : 2), len);
//        }
//
//        messages = large ? array(str_repeat(" ", len)) : new ArrayList();
//        for (String line : lines) {
//            messages.add(line.str_repeat(" ", len - strlen(line));
//        }
//        if (large) {
//            messages.add(str_repeat(" ", len);
//        }
//
//        for (String message : messages) {
//            message = String.format("<%s>%s</%s>", style, message, style);
//        }
//
//        return Util.implode("\n", messages);
//    }
//
//    public String formatBlock(final String message, final String style, final boolean large) {
//        List<String> messages = new ArrayList<String>();
//        messages.add(message);
//
//        return formatBlock(messages, style, large);
//    }
//
//    public String formatBlock(final List<String> messages, final String style) {
//        return formatBlock(messages, style, false);
//    }
//
//    public String formatBlock(final String message, final String style) {
//        return formatBlock(message, style, false);
//    }
//
//    /**
//     * Returns the length of a string, using mb_strlen if it is available.
//     *
//     * @param string The string to check its length
//     *
//     * @return The length of the string
//     */
//    private int strlen(final String string) {
//        return string.length(); // TODO check that encoding is managed
//    }
//
//    /**
//     * Returns the helper's canonical name.
//     *
//     * @return string The canonical name of the helper
//     */
//    @Override
//    public String getName() {
//        return "formatter";
//    }

}
