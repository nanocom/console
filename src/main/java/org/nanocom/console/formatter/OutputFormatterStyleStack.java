/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.formatter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
class OutputFormatterStyleStack {

    private List<OutputFormatterStyle> styles;

    public OutputFormatterStyleStack() {
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
     * @throws Exception  When style tags incorrectly nested
     */
    public OutputFormatterStyleInterface pop(OutputFormatterStyleInterface style) {
        if (styles.isEmpty()) {
            return new OutputFormatterStyle();
        }

        if (null == style) {
            return styles.remove(0);
        }

        /*foreach (array_reverse($this->styles, true) as $index => $stackedStyle) {
            if ($style->apply('') === $stackedStyle->apply('')) {
                $this->styles = array_slice($this->styles, 0, $index);

                return $stackedStyle;
            }
        }*/

        throw new IllegalArgumentException("Incorrectly nested style tag found.");
    }

    public OutputFormatterStyleInterface pop() throws Exception {
        return pop(null);
    }

    /**
     * Computes current style with stacks top codes.
     *
     * @return
     */
    public OutputFormatterStyle getCurrent() throws Exception {
        if (styles.isEmpty()) {
            return new OutputFormatterStyle();
        }

        return styles.get(styles.size() - 1);
    }

}
