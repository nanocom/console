package com.nanocom.console.formatter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arnaud Kleinpeter <arnaud.kleinpeter at gmail dot com>
 */
final class OutputFormatterStyleStack {

//    private List<OutputFormatterStyle> styles;
//
//    public  OutputFormatterStyleStack() {
//        reset();
//    }
//
//    /**
//     * Resets stack (ie. empty internal arrays).
//     */
//    public void reset() {
//        styles = new ArrayList<OutputFormatterStyle>();
//    }
//
//    /**
//     * Pushes a style in the stack.
//     *
//     * @param style
//     */
//    public void push(OutputFormatterStyleInterface style) {
//        styles.add((OutputFormatterStyle) style);
//    }
//
//    /**
//     * Pops a style from the stack.
//     *
//     * @param style
//     *
//     * @return
//     *
//     * @throws Exception When style tags incorrectly nested
//     */
//    public OutputFormatterStyleInterface pop(OutputFormatterStyleInterface style) throws Exception {
//        if (null == style) {
//            return array_pop(this.styles); // Never happens
//        }
//
//        if (styles.isEmpty()) {
//            return new OutputFormatterStyle();
//        }
//
//        // for (array_reverse(styles, true) as index => stackedStyle) {
//        // TODO reverse the array and change for something more adapted than ArrayList
//        for (int i = 0; i < styles.size(); i++) {
//            OutputFormatterStyle stackedStyle = styles.get(i);
//            if (style.apply("").equals(stackedStyle.apply(""))) {
//                styles = array_slice(styles, 0, i);
//
//                return stackedStyle;
//            }
//        }
//
//        throw new Exception("Incorrectly nested style tag found.");
//    }
//
//    /**
//     * Computes current style with stacks top codes.
//     *
//     * @return
//     */
//    public OutputFormatterStyle getCurrent() throws Exception {
//        if (styles.isEmpty()) {
//            return new OutputFormatterStyle();
//        }
//
//        return styles.get(styles.size() - 1);
//    }

}
