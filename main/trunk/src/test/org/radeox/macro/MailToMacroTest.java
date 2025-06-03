package org.radeox.macro;

public class MailToMacroTest extends MacroTestSupport
{
    public MailToMacroTest(final String name)
    {
        super(name);
    }

    public void testMailto()
    {
        final String result = engine.render("{mailto:stephan@mud.de}", context);
        assertEquals("<a href=\"mailto:stephan@mud.de\">stephan@mud.de</a>",
            result);
    }

}
