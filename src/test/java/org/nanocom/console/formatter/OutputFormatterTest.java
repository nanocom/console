package org.nanocom.console.formatter;

import org.nanocom.console.formatter.OutputFormatter;
import org.nanocom.console.formatter.OutputFormatterStyle;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OutputFormatterTest {
    
    public OutputFormatterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEmptyTag() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);
        Assert.assertEquals("foo<>bar", formatter.format("foo<>bar"));
    }

    @Test
    public void testBundledStyles() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        /*Assert.assertTrue(formatter.hasStyle("error"));
        Assert.assertTrue(formatter.hasStyle("info"));
        Assert.assertTrue(formatter.hasStyle("comment"));
        Assert.assertTrue(formatter.hasStyle("question"));

        Assert.assertEquals(
            "\033[37;41msome error\033[0m",
            formatter.format("<error>some error</error>")
        );
        Assert.assertEquals(
            "\033[32msome info\033[0m",
            formatter.format("<info>some info</info>")
        );
        Assert.assertEquals(
            "\033[33msome comment\033[0m",
            formatter.format("<comment>some comment</comment>")
        );
        Assert.assertEquals(
            "\033[30;46msome question\033[0m",
            formatter.format("<question>some question</question>")
        );*/
    }

    @Test
    public void testNestedStyles() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        /*Assert.assertEquals(
            "\033[37;41msome \033[0m\033[32msome info\033[0m\033[37;41m error\033[0m",
            formatter.format("<error>some <info>some info</info> error</error>")
        );*/
    }

    @Test
    public void testDeepNestedStyles() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        /*Assert.assertEquals(
            "\033[37;41merror\033[0m\033[32minfo\033[0m\033[33mcomment\033[0m\033[37;41merror\033[0m",
            formatter.format("<error>error<info>info<comment>comment</info>error</error>")
        );*/
    }

    @Test
    public void testNewStyle() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        OutputFormatterStyle style = new OutputFormatterStyle("blue", "white");
        formatter.setStyle("test", style);

        Assert.assertEquals(style, formatter.getStyle("test"));
        Assert.assertNotSame(style, formatter.getStyle("info"));

        // Assert.assertEquals("\033[34;47msome custom msg\033[0m", formatter.format("<test>some custom msg</test>"));
    }

    @Test
    public void testRedefineStyle() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        OutputFormatterStyle style = new OutputFormatterStyle("blue", "white");
        formatter.setStyle("info", style);

        // Assert.assertEquals("\033[34;47msome custom msg\033[0m", formatter.format("<info>some custom msg</info>"));
    }

    @Test
    public void testInlineStyle() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        // Assert.assertEquals("\033[34;41msome text\033[0m", formatter.format("<fg=blue;bg=red>some text</>"));
        // Assert.assertEquals("\033[34;41msome text\033[0m", formatter.format("<fg=blue;bg=red>some text</fg=blue;bg=red>"));
    }

    @Test
    public void testNotDecoratedFormatter() throws Exception {
        OutputFormatter formatter = new OutputFormatter(false);

        Assert.assertTrue(formatter.hasStyle("error"));
        Assert.assertTrue(formatter.hasStyle("info"));
        Assert.assertTrue(formatter.hasStyle("comment"));
        Assert.assertTrue(formatter.hasStyle("question"));

        /*Assert.assertEquals(
            "some error", formatter.format("<error>some error</error>")
        );
        Assert.assertEquals(
            "some info", formatter.format("<info>some info</info>")
        );
        Assert.assertEquals(
            "some comment", formatter.format("<comment>some comment</comment>")
        );
        Assert.assertEquals(
            "some question", formatter.format("<question>some question</question>")
        );*/

        formatter.setDecorated(true);

        /*Assert.assertEquals(
            "\033[37;41msome error\033[0m", formatter.format("<error>some error</error>")
        );
        Assert.assertEquals(
            "\033[32msome info\033[0m", formatter.format("<info>some info</info>")
        );
        Assert.assertEquals(
            "\033[33msome comment\033[0m", formatter.format("<comment>some comment</comment>")
        );
        Assert.assertEquals(
            "\033[30;46msome question\033[0m", formatter.format("<question>some question</question>")
        );*/
    }

    @Test
    public void testContentWithLineBreaks() throws Exception {
        OutputFormatter formatter = new OutputFormatter(true);

        /*
        Assert.assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        Assert.assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        Assert.assertEquals("\033[32m\nsome text\n\033[0m", formatter.format("<info>\nsome text\n</info>"));
        Assert.assertEquals("\033[32m\nsome text\nmore text\n\033[0m", formatter.format("<info>\nsome text\nmore text\n</info>"));
        */
    }

}
