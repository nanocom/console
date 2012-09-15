/*
 * This file is part of the Console package.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.nanocom.console.helper;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class FormatterHelperTest {

    @Test
    public void testFormatSection() {
        FormatterHelper formatter = new FormatterHelper();

        assertEquals(
            "formatSection() formats a message in a section",
            "<info>[cli]</info> Some text to display",
            formatter.formatSection("cli", "Some text to display")
        );
    }

    @Test
    public void testFormatBlock() {
        FormatterHelper formatter = new FormatterHelper();

        assertEquals(
            "formatBlock() formats a message in a block",
            "<error> Some text to display </error>",
            formatter.formatBlock("Some text to display", "error")
        );

        assertEquals(
            "formatBlock() formats a message in a block",
            "<error> Some text to display </error>" + "\n" +
            "<error> foo bar              </error>",
            formatter.formatBlock(Arrays.asList("Some text to display", "foo bar"), "error")
        );

        assertEquals(
            "formatBlock() formats a message in a block",
            "<error>                        </error>" + "\n" +
            "<error>  Some text to display  </error>" + "\n" +
            "<error>                        </error>",
            formatter.formatBlock("Some text to display", "error", true)
        );
    }

    @Test
    public void testFormatBlockWithDiacriticLetters() {
        FormatterHelper formatter = new FormatterHelper();

        assertEquals(
            "formatBlock() formats a message in a block",
            "<error>                       </error>" + "\n" +
            "<error>  Du texte à afficher  </error>" + "\n" +
            "<error>                       </error>",
            formatter.formatBlock("Du texte à afficher", "error", true)
        );
    }

    @Test
    public void testFormatBlockLGEscaping() {
        FormatterHelper formatter = new FormatterHelper();

        assertEquals(
            "formatBlock() escapes \"<\" chars",
            "<error>                            </error>\n" +
            "<error>  \\<info>some info\\</info>  </error>\n" +
            "<error>                            </error>",
            formatter.formatBlock("<info>some info</info>", "error", true)
        );
    }
}
