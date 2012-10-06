/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

public class DialogHelperTest {

    public DialogHelperTest() {
    }

    /*@Test
    public void testAsk() {
        DialogHelper dialog = new DialogHelper();

        InputStream stream = new ObjectInputStream

        dialog.setInputStream(getInputStream("\n8AM\n"));

        InMemoryOutput output = new InMemoryOutput();
        assertEquals("2PM", dialog.ask(output, "What time is it?", "2PM"));
        assertEquals("8AM", dialog.ask(output, "What time is it?", "2PM"));

        assertEquals("What time is it?", output.getBuffer().toString());
    }

    /*@Test
    public void testAskConfirmation() {
        DialogHelper dialog = new DialogHelper();

        dialog.setInputStream(getInputStream("\n\n"));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?"));
        assertFalse(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));

        dialog.setInputStream(getInputStream("y\nyes\n"));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));
        assertTrue(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", false));

        dialog.setInputStream(getInputStream("n\nno\n"));
        assertFalse(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", true));
        assertFalse(dialog.askConfirmation(getOutputStream(), "Do you like French fries?", true));
    }

    @Test
    public void testAskAndValidate() {
        DialogHelper dialog = new DialogHelper();
        HelperSet helperSet = new HelperSet(Arrays.<Helper>asList(new FormatterHelper()));
        dialog.setHelperSet(helperSet);

        question = "What color was the white horse of Henry IV?";
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
    }

    protected void getInputStream(input) {
        stream = fopen("php://memory", "r+", false);
        fputs(stream, input);
        rewind(stream);

        return stream;
    }*/
}
