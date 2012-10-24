/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Formatter style class for defining styles.
 *
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
public class OutputFormatterStyle implements OutputFormatterStyleInterface {

    private static Map<String, Integer> availableForegroundColors = getAvailableForegroundColors();
    private static Map<String, Integer> availableBackgroundColors = getAvailableBackgroundColors();
    private static Map<String, Integer> availableOptions = getAvailableOptions();

    private static Map<String, Integer> getAvailableForegroundColors() {
        Map<String, Integer> foregrounds = new LinkedHashMap<String, Integer>();
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
        Map<String, Integer> backgrounds = new LinkedHashMap<String, Integer>();
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
        options.put("blink", 5);
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
     * @param foreground The style foreground color name
     * @param background The style background color name
     * @param options    The style options
     */
    public OutputFormatterStyle(String foreground, String background, String[] options) {
        init(foreground, background, options);
    }

    /**
     * Initializes output formatter style.
     *
     * @param foreground The style foreground color name
     * @param background The style background color name
     * @param options    The style options
     */
    public OutputFormatterStyle(String foreground, String background, Collection<String> options) {
        this(foreground, background, options.toArray(new String[0]));
    }

    /**
     * Initializes output formatter style.
     *
     * @param foreground The style foreground color name
     * @param background The style background color name
     */
    public OutputFormatterStyle(String foreground, String background) {
        this(foreground, background, new String[0]);
    }

    /**
     * Initializes output formatter style.
     *
     * @param foreground The style foreground color name
     */
    public OutputFormatterStyle(String foreground) {
        this(foreground, null);
    }

     /**
     * Initializes output formatter style.
     */
    public OutputFormatterStyle() {
        this(null);
    }

    private void init(String foreground, String background, String[] options) {
        if (null != foreground) {
            setForeground(foreground);
        }

        if (null != background) {
            setBackground(background);
        }

        if (null != options && options.length > 0) {
            setOptions(options);
        }
    }

    /**
     * Sets style foreground color.
     *
     * @param color The color name
     *
     * @throws IllegalArgumentException When the color name isn't defined
     */
    @Override
    public void setForeground(String color) {
        if (null == color) {
            foreground = null;
            return;
        }

        if (!availableForegroundColors.containsKey(color)) {
            Set<String> foregroundNames = availableForegroundColors.keySet();
            throw new IllegalArgumentException(String.format(
                "Invalid foreground color specified: \"%s\". Expected one of (%s)",
                color,
                StringUtils.join((String[]) foregroundNames.toArray(new String[0]), ", ")
            ));
        }

        foreground = availableForegroundColors.get(color);
    }

    /**
     * Sets style background color.
     *
     * @param color The color name
     *
     * @throws IllegalArgumentException When the color name isn't defined
     */
    @Override
    public void setBackground(String color) {
        if (null == color) {
            background = null;
            return;
        }

        if (!availableBackgroundColors.containsKey(color)) {
            Set<String> backgroundNames = availableBackgroundColors.keySet();
            List<String> backgroundNamesList = new ArrayList<String>();
            backgroundNamesList.addAll(backgroundNames);
            throw new IllegalArgumentException(String.format(
                "Invalid background color specified: \"%s\". Expected one of (%s)",
                color,
                StringUtils.join((String[]) backgroundNamesList.toArray(new String[0]), ", ")
            ));
        }

        background = availableBackgroundColors.get(color);
    }

    /**
     * Sets some specific style option.
     *
     * @param option The option name
     *
     * @throws IllegalArgumentException When the option name isn't defined
     */
    @Override
    public void setOption(String option) {
        if (!availableOptions.containsKey(option)) {
            Set<String> optionNames = availableOptions.keySet();
            List<String> optionNamesList = new ArrayList<String>(optionNames);
            throw new IllegalArgumentException(String.format(
                "Invalid option specified: \"%s\". Expected one of (%s)",
                option,
                StringUtils.join(optionNamesList.toArray(), ", ")
            ));
        }

        if (!options.contains(availableOptions.get(option))) {
            options.add(availableOptions.get(option));
        }
    }

    /**
     * Unsets some specific style option.
     *
     * @param option The option name
     *
     * @throws IllegalArgumentException When the option name isn't defined
     */
    @Override
    public void unsetOption(String option) {
        if (!availableOptions.containsKey(option)) {
            Set<String> optionNames = availableOptions.keySet();
            List<String> optionNamesList = new ArrayList<String>();
            optionNamesList.addAll(optionNames);
            throw new IllegalArgumentException(String.format(
                "Invalid option specified: \"%s\". Expected one of (%s)",
                option,
                StringUtils.join((String[]) optionNamesList.toArray(), ", ")
            ));
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
    public void setOptions(String[] options) {
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
    public String apply(String text) {
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

        String flatCodes = StringUtils.join((String[]) codes.toArray(new String[0]), ";");

        return String.format("\033[%sm%s\033[0m", flatCodes, text);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (foreground != null ? foreground.hashCode() : 0);
        hash = 71 * hash + (background != null ? background.hashCode() : 0);
        hash = 71 * hash + (options != null ? options.hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        OutputFormatterStyle other = (OutputFormatterStyle) obj;
        if (foreground != other.foreground && (foreground == null || !this.foreground.equals(other.foreground))) {
            return false;
        }

        if (background != other.background && (background == null || !this.background.equals(other.background))) {
            return false;
        }

        if (options != other.options && (options == null || !options.equals(other.options))) {
            return false;
        }

        return true;
    }
}
