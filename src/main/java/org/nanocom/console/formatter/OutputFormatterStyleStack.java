/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
class OutputFormatterStyleStack {

    private List<OutputFormatterStyle> styles;
    private OutputFormatterStyleInterface emptyStyle;

    public OutputFormatterStyleStack() {
        this(null);
    }

    public OutputFormatterStyleStack(OutputFormatterStyleInterface emptyStyle) {
        this.emptyStyle = null != emptyStyle ? emptyStyle : new OutputFormatterStyle();
        reset();
    }

    /**
     * Resets stack (ie. empty internal arrays).
     */
    public final void reset() {
        styles = new ArrayList<OutputFormatterStyle>();
    }

    /**
     * Pushes a style in the stack.
     *
     * @param style
     */
    public void push(OutputFormatterStyleInterface style) {
        styles.add((OutputFormatterStyle) style);
    }

    /**
     * Pops a style from the stack.
     *
     * @param style
     *
     * @return
     *
     * @throws IllegalArgumentException When style tags incorrectly nested
     */
    public OutputFormatterStyleInterface pop(OutputFormatterStyleInterface style) {
        if (styles.isEmpty()) {
            return emptyStyle;
        }

        if (null == style) {
            return styles.remove(styles.size() - 1);
        }

        // array_reverse equivalent
        final Map<Integer, OutputFormatterStyleInterface> reversedStyles = new LinkedHashMap<Integer, OutputFormatterStyleInterface>(styles.size());
        for (int i = styles.size() - 1; i >= 0; --i) {
            reversedStyles.put(i, styles.get(i));
        }

        for (Entry<Integer, OutputFormatterStyleInterface> stackedStyle : reversedStyles.entrySet()) {
            if (style.apply("").equals(stackedStyle.getValue().apply(""))) {
                styles = styles.subList(0, stackedStyle.getKey());

                return stackedStyle.getValue();
            }
        }

        throw new IllegalArgumentException("Incorrectly nested style tag found.");
    }

    public OutputFormatterStyleInterface pop() {
        return pop(null);
    }

    /**
     * Computes current style with stacks top codes.
     *
     * @return
     */
    public OutputFormatterStyle getCurrent() {
        if (styles.isEmpty()) {
            return (OutputFormatterStyle) emptyStyle;
        }

        return styles.get(styles.size() - 1);
    }

    /**
     * @param emptyStyle
     *
     * @return OutputFormatterStyleStack
     */
    public OutputFormatterStyleStack setEmptyStyle(OutputFormatterStyleInterface emptyStyle) {
        this.emptyStyle = emptyStyle;

        return this;
    }

    /**
     * @return
     */
    public OutputFormatterStyleInterface getEmptyStyle() {
        return emptyStyle;
    }
}
