/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nanocom.console.Util;

/**
 * Formatter style class for defining styles.
 * 
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public final class OutputFormatterStyle implements OutputFormatterStyleInterface {

    private static Map<String, Integer> availableForegroundColors = getAvailableForegroundColors();
    private static Map<String, Integer> availableBackgroundColors = getAvailableBackgroundColors();
    private static Map<String, Integer> availableOptions = getAvailableOptions();

    private static Map<String, Integer> getAvailableForegroundColors() {
        Map<String, Integer> foregrounds = new HashMap<String, Integer>();
        foregrounds.put("black", 30);
        foregrounds.put("red", 31);
        foregrounds.put("green", 32);
        foregrounds.put("yellow", 33);
        foregrounds.put("blue", 34);
        foregrounds.put("magenta", 35);
        foregrounds.put("cyan", 36);
        foregrounds.put("white", 37);

        return foregrounds;
    }

    private static Map<String, Integer> getAvailableBackgroundColors() {
        Map<String, Integer> backgrounds = new HashMap<String, Integer>();
        backgrounds.put("black", 40);
        backgrounds.put("red", 41);
        backgrounds.put("green", 42);
        backgrounds.put("yellow", 43);
        backgrounds.put("blue", 44);
        backgrounds.put("magenta", 45);
        backgrounds.put("cyan", 46);
        backgrounds.put("white", 47);

        return backgrounds;
    }
    
    private static Map<String, Integer> getAvailableOptions() {
        Map<String, Integer> options = new HashMap<String, Integer>();
        options.put("bold", 1);
        options.put("underscore", 4);
        options.put("bblink", 5);
        options.put("reverse", 7);
        options.put("conceal", 8);

        return options;
    }

    private Integer foreground;
    private Integer background;
    private List<Integer> options = new ArrayList<Integer>();

    /**
     * Initializes output formatter style.
     *
     * @param foreground style foreground color name
     * @param background style background color name
     * @param options style options
     */
    public OutputFormatterStyle(final String foreground, final String background, final List<String> options) throws Exception {
        init(foreground, background, options);
    }

    public OutputFormatterStyle(final String foreground, final String background) throws Exception {
        init(foreground, background, new ArrayList<String>());
    }

    public OutputFormatterStyle(final String foreground) throws Exception {
        init(foreground, null, new ArrayList<String>());
    }

    public OutputFormatterStyle() throws Exception {
        init(null, null, new ArrayList<String>());
    }

    private void init(final String foreground, final String background, final List<String> options) throws Exception {
        if (null != foreground) {
            setForeground(foreground);
        }
        if (null != background) {
            setBackground(background);
        }
        if (options.size() > 0) {
            this.setOptions(options);
        }
    }

    /**
     * Sets style foreground color.
     *
     * @param color color name
     *
     * @throws Exception When the color name isn't defined
     */
    @Override
    public void setForeground(final String color) throws Exception {
        if (null == color) {
            this.foreground = null;

            return;
        }

        if (!availableForegroundColors.containsKey(color)) {
            Set<String> foregroundNames = availableForegroundColors.keySet();
            List<String> foregroundNamesList = new ArrayList<String>();
            foregroundNamesList.addAll(foregroundNames);
            throw new Exception(
                "Invalid foreground color specified: \"" + color
                + "\". Expected one of (" + Util.implode(", ",
                    (String[]) foregroundNamesList.toArray()) + ")"
            );
        }

        foreground = availableForegroundColors.get(color);
    }

    /**
     * Sets style background color.
     *
     * @param color  color name
     *
     * @throws Exception When the color name isn't defined
     */
    @Override
    public void setBackground(final String color) throws Exception {
        if (null == color) {
            this.background = null;

            return;
        }

        if (!availableBackgroundColors.containsKey(color)) {
            Set<String> backgroundNames = availableBackgroundColors.keySet();
            List<String> backgroundNamesList = new ArrayList<String>();
            backgroundNamesList.addAll(backgroundNames);
            throw new Exception(
                "Invalid background color specified: " + color
                + ". Expected one of (" + Util.implode(", ", (String[]) backgroundNamesList.toArray()) + ")"
            );
        }

        background = availableBackgroundColors.get(color);
    }

    /**
     * Sets some specific style option.
     *
     * @param option option name
     *
     * @throws Exception When the option name isn't defined
     */
    @Override
    public void setOption(final String option) throws Exception {
        if (!availableOptions.containsKey(option)) {
            Set<String> optionNames = availableOptions.keySet();
            List<String> optionNamesList = new ArrayList<String>();
            optionNamesList.addAll(optionNames);
            throw new Exception(
                "Invalid option specified: \"" + option
                + "\". Expected one of (" + Util.implode(", ", (String[]) optionNamesList.toArray()) + ")"
            );
        }

        if (!options.contains(availableOptions.get(option))) {
            options.add(availableOptions.get(option));
        }
    }

    /**
     * Unsets some specific style option.
     *
     * @param option option name
     *
     * @throws Exception When the option name isn't defined
     */
    @Override
    public void unsetOption(final String option) throws Exception {
        if (!availableOptions.containsKey(option)) {
            Set<String> optionNames = availableOptions.keySet();
            List<String> optionNamesList = new ArrayList<String>();
            optionNamesList.addAll(optionNames);
            throw new Exception(
                "Invalid option specified: \"" + option
                + "\". Expected one of (" + Util.implode(", ", (String[]) optionNamesList.toArray()) + ")"
            );
        }

        int pos = options.indexOf(availableOptions.get(option));
        if (pos > -1) {
            options.remove(pos);
        }
    }

    /**
     * Sets multiple style options at once.
     *
     * @param option
     */
    @Override
    public void setOptions(final List<String> options) throws Exception {
        this.options = new ArrayList<Integer>();

        for (String option : options) {
            setOption(option);
        }
    }

    /**
     * Applies the style to a given text.
     *
     * @param text The text to style
     *
     * @return
     */
    @Override
    public String apply(final String text) {
        List<String> codes = new ArrayList<String>();

        if (null != foreground) {
            codes.add(foreground.toString());
        }

        if (null != background) {
            codes.add(background.toString());
        }

        if (options.size() > 0) {
            for (Integer option : options) {
                codes.add(option.toString());
            }
        }

        return String.format("\033[%sm%s\033[0m", Util.implode(";", (String[]) codes.toArray()), text);
    }

}
