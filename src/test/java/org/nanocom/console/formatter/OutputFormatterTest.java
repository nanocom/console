package org.nanocom.console.formatter;

import static org.junit.Assert.*;
import org.junit.Test;

public class OutputFormatterTest {

    public OutputFormatterTest() {
    }

    @Test
    public void testEmptyTag() {
        OutputFormatter formatter = new OutputFormatter(true);
        assertEquals("foo<>bar", formatter.format("foo<>bar"));
    }

    @Test
    public void testBundledStyles() {
        OutputFormatter formatter = new OutputFormatter(true);

        assertTrue(formatter.hasStyle("error"));
        assertTrue(formatter.hasStyle("info"));
        assertTrue(formatter.hasStyle("comment"));
        assertTrue(formatter.hasStyle("question"));

        assertEquals(
            "\033[37;41msome error\033[0m",
            formatter.format("<error>some error</error>")
        );
        assertEquals(
            "\033[32msome info\033[0m",
            formatter.format("<info>some info</info>")
        );
        assertEquals(
            "\033[33msome comment\033[0m",
            formatter.format("<comment>some comment</comment>")
        );
        assertEquals(
            "\033[30;46msome question\033[0m",
            formatter.format("<question>some question</question>")
        );
    }

    @Test
    public void testNestedStyles() {
        OutputFormatter formatter = new OutputFormatter(true);

        assertEquals(
            "\033[37;41msome \033[0m\033[32msome info\033[0m\033[37;41m error\033[0m",
            formatter.format("<error>some <info>some info</info> error</error>")
        );
    }

    @Test
    public void testDeepNestedStyles() {
        OutputFormatter formatter = new OutputFormatter(true);

        assertEquals(
            "\033[37;41merror\033[0m\033[32minfo\033[0m\033[33mcomment\033[0m\033[37;41merror\033[0m",
            formatter.format("<error>error<info>info<comment>comment</info>error</error>")
        );
    }

    @Test
    public void testNewStyle() {
        OutputFormatter formatter = new OutputFormatter(true);

        OutputFormatterStyle style = new OutputFormatterStyle("blue", "white");
        formatter.setStyle("test", style);

        assertEquals(style, formatter.getStyle("test"));
        assertNotSame(style, formatter.getStyle("info"));

        assertEquals("\033[34;47msome custom msg\033[0m", formatter.format("<test>some custom msg</test>"));
    }

    @Test
    public void testRedefineStyle() {
        OutputFormatter formatter = new OutputFormatter(true);

        OutputFormatterStyle style = new OutputFormatterStyle("blue", "white");
        formatter.setStyle("info", style);

        assertEquals("\033[34;47msome custom msg\033[0m", formatter.format("<info>some custom msg</info>"));
    }

    @Test
    public void testInlineStyle() {
        OutputFormatter formatter = new OutputFormatter(true);

        assertEquals("\033[34;41msome text\033[0m", formatter.format("<fg=blue;bg=red>some text</>"));
        assertEquals("\033[34;41msome text\033[0m", formatter.format("<fg=blue;bg=red>some text</fg=blue;bg=red>"));
    }

    @Test
    public void testNotDecoratedFormatter() {
        OutputFormatter formatter = new OutputFormatter(false);

        assertTrue(formatter.hasStyle("error"));
        assertTrue(formatter.hasStyle("info"));
        assertTrue(formatter.hasStyle("comment"));
        assertTrue(formatter.hasStyle("question"));

        assertEquals(
            "some error", formatter.format("<error>some error</error>")
        );
        assertEquals(
            "some info", formatter.format("<info>some info</info>")
        );
        assertEquals(
            "some comment", formatter.format("<comment>some comment</comment>")
        );
        assertEquals(
            "some question", formatter.format("<question>some question</question>")
        );

        formatter.setDecorated(true);

        assertEquals(
            "\033[37;41msome error\033[0m", formatter.format("<error>some error</error>")
        );
        assertEquals(
            "\033[32msome info\033[0m", formatter.format("<info>some info</info>")
        );
        assertEquals(
            "\033[33msome comment\033[0m", formatter.format("<comment>some comment</comment>")
        );
        assertEquals(
            "\033[30;46msome question\033[0m", formatter.format("<question>some question</question>")
        );
    }

    @Test
    public void testContentWithLineBreaks() {
        OutputFormatter formatter = new OutputFormatter(true);

        assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        assertEquals("\033[32m\nsome text\nmore text\n\033[0m", formatter.format("<info>\nsome text\nmore text\n</info>"));
    }
}
