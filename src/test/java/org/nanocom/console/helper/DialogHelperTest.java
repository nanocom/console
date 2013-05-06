/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.nanocom.console.output.InMemoryOutput;

public class DialogHelperTest {

    public DialogHelperTest() {
    }

    @Test
    public void testSelect() {
        DialogHelper dialog = new DialogHelper();

        HelperSet helperSet = new HelperSet(Arrays.<Helper>asList(new FormatterHelper()));
        dialog.setHelperSet(helperSet);

        Map<String, String> heroes = new HashMap<String, String>();
        heroes.put("1", "Superman");
        heroes.put("2", "Batman");
        heroes.put("3", "Spiderman");

        dialog.setInputReader(new BufferedReader(new StringReader("\n1\n  1  \nFabien\n1\nFabien\n1\n0,2\n 0 , 2  \n\n\n")));
        assertEquals("2", dialog.select(new InMemoryOutput(), "What is your favorite superhero?", heroes, "2"));
        assertEquals("1", dialog.select(new InMemoryOutput(), "What is your favorite superhero?", heroes));
        assertEquals("1", dialog.select(new InMemoryOutput(), "What is your favorite superhero?", heroes));
        InMemoryOutput output = new InMemoryOutput();
        assertEquals("1", dialog.select(output, "What is your favorite superhero?", heroes, null, -1, "Input \"%s\" is not a superhero!", false));
        assertTrue(output.getBuffer().toString().contains("Input \"Fabien\" is not a superhero!"));

        output = new InMemoryOutput();
        try {
            dialog.select(output, "What is your favorite superhero?", heroes, null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Value \"Fabien\" is invalid", e.getMessage());
        }

        assertArrayEquals(new String[] {"1"}, dialog.select(output, "What is your favorite superhero?", heroes, null, -1, "Input \"%s\" is not a superhero!", true));
        assertArrayEquals(new String[] {"0", "2"}, dialog.select(output, "What is your favorite superhero?", heroes, null, -1, "Input \"%s\" is not a superhero!", true));
        assertArrayEquals(new String[] {"0", "2"}, dialog.select(output, "What is your favorite superhero?", heroes, null, -1, "Input \"%s\" is not a superhero!", true));
        assertArrayEquals(new String[] {"0", "1"}, dialog.select(output, "What is your favorite superhero?", heroes, "0,1", -1, "Input \"%s\" is not a superhero!", true));
        assertArrayEquals(new String[] {"0", "1"}, dialog.select(output, "What is your favorite superhero?", heroes, " 0 , 1 ", -1, "Input \"%s\" is not a superhero!", true));
    }

   /* @Test
    public void testAsk() {
        DialogHelper dialog = new DialogHelper();

        dialog.setInputStream(this.getInputStream("\n8AM\n"));

        assertEquals("2PM", dialog.ask(this.getOutputStream(), "What time is it?", "2PM"));
        assertEquals("8AM", dialog.ask(output = this.getOutputStream(), "What time is it?", "2PM"));

        rewind(output.getStream());
        assertEquals("What time is it?", stream_get_contents(output.getStream()));
    }

    @Test
    public void testAskWithAutocomplete() {
        if (!hasSttyAvailable()) {
            markTestSkipped("`stty` is required to test autocomplete functionality");
        }

        // Acm<NEWLINE>
        // Ac<BACKSPACE><BACKSPACE>s<TAB>Test<NEWLINE>
        // <NEWLINE>
        // <UP ARROW><UP ARROW><NEWLINE>
        // <UP ARROW><UP ARROW><UP ARROW><UP ARROW><UP ARROW><TAB>Test<NEWLINE>
        // <DOWN ARROW><NEWLINE>
        // S<BACKSPACE><BACKSPACE><DOWN ARROW><DOWN ARROW><NEWLINE>
        inputStream = this.getInputStream("Acm\nAc\177\177s\tTest\n\n\033[A\033[A\n\033[A\033[A\033[A\033[A\033[A\tTest\n\033[B\nS\177\177\033[B\033[B\n");

        DialogHelper dialog = new DialogHelper();
        dialog.setInputStream(inputStream);

        bundles = array("AcmeDemoBundle", "AsseticBundle", "SecurityBundle", "FooBundle");

        assertEquals("AcmeDemoBundle", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("AsseticBundleTest", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("FrameworkBundle", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("SecurityBundle", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("FooBundleTest", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("AcmeDemoBundle", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
        assertEquals("AsseticBundle", dialog.ask(this.getOutputStream(), "Please select a bundle", "FrameworkBundle", bundles));
    }

    @Test
    public void testAskHiddenResponse() {
        if (defined("PHP_WINDOWS_VERSION_BUILD")) {
            this.markTestSkipped("This test is not supported on Windows");
        }

        DialogHelper dialog = new DialogHelper();

        dialog.setInputStream(this.getInputStream("8AM\n"));

        assertEquals("8AM", dialog.askHiddenResponse(this.getOutputStream(), "What time is it?"));
    }

    @Test
    public void testAskConfirmation() {
        DialogHelper dialog = new DialogHelper();

        dialog.setInputStream(getInputStream("\n\n"));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?"));
        assertFalse(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));

        dialog.setInputStream(getInputStream("y\nyes\n"));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));

        dialog.setInputStream(this.getInputStream("n\nno\n"));
        assertFalse(dialog.askConfirmation(this.getOutputStream(), "Do you like French fries?", true));
        assertFalse(dialog.askConfirmation(this.getOutputStream(), "Do you like French fries?", true));
    }

    @Test
    public void testAskAndValidate() {
        DialogHelper dialog = new DialogHelper();
        HelperSet helperSet = new HelperSet(array(new FormatterHelper()));
        dialog.setHelperSet(helperSet);

        question ="What color was the white horse of Henry IV?";
        error = "This is not a color!";
        validator = function (color) use (error) {
            if (!in_array(color, array("white", "black"))) {
                throw new IllegalArgumentException(error);
            }

            return color;
        };

        dialog.setInputStream(getInputStream("\nblack\n"));
        assertEquals("white", dialog.askAndValidate(getOutputStream(), question, validator, 2, "white"));
        assertEquals("black", dialog.askAndValidate(getOutputStream(), question, validator, 2, "white"));

        dialog.setInputStream(getInputStream("green\nyellow\norange\n"));
        try {
            assertEquals("white", dialog.askAndValidate(getOutputStream(), question, validator, 2, "white"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(error, e.getMessage());
        }
    }*/

    private boolean hasSttyAvailable() throws IOException {
        return 0 == Runtime.getRuntime().exec("stty 2>&1").exitValue();
    }
}
