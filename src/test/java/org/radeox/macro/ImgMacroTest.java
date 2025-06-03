package org.radeox.macro;

import org.radeox.macro.img.ImgMacroFile;
import org.radeox.macro.img.ImgMacroContext;

/**
 * JUnit test for {@link ImgMacro}.
 *
 * <p>Created on 2025-06-02</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ImgMacroTest extends MacroTestSupport
{
    public ImgMacroTest(final String name)
    {
        super(name);
    }

    public void testContent()
    {
        final ImgMacroContext imgContext = ImgMacroContext.getOrCreate(context);
        imgContext.add(new ImgMacroFile("image01.png", "image/png",
            () -> new byte[] {0, 1, 2, 3}));

        final String result1 = engine.render("{img:image00.png}", context);
        assertEquals("{img:image00.png}", result1);

        final String result2 = engine.render("{img:image01.png}", context);
        assertEquals("<img src='data:image/png;charset=utf-8;base64,AAECAw=='>", result2);

        final String result3 = engine.render("{img:left|image01.png}", context);
        assertEquals("<img style='float:left;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result3);

        final String result4 = engine.render("{img:l|image01.png}", context);
        assertEquals("<img style='float:left;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result4);

        final String result5 = engine.render("{img:right|image01.png}", context);
        assertEquals("<img style='float:right;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result5);

        final String result6 = engine.render("{img:r|image01.png}", context);
        assertEquals("<img style='float:right;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result6);

        final String result7 = engine.render("{img:center|image01.png}", context);
        assertEquals("<img style='margin-left:auto;margin-right:auto;display:block;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result7);

        final String result8 = engine.render("{img:c|image01.png}", context);
        assertEquals("<img style='margin-left:auto;margin-right:auto;display:block;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result8);

        final String result9 = engine.render("{img:width=11em|image01.png}", context);
        assertEquals("<img style='width:11em;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result9);

        final String result10 = engine.render("{img:w=11em|image01.png}", context);
        assertEquals("<img style='width:11em;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result10);

        final String result11 = engine.render("{img:height=12px|image01.png}", context);
        assertEquals("<img style='height:12px;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result11);

        final String result12 = engine.render("{img:h=12|image01.png}", context);
        assertEquals("<img style='height:12px;' src='data:image/png;charset=utf-8;base64,AAECAw=='>", result12);
    }

    public void testMime()
    {
        final ImgMacroContext imgContext = ImgMacroContext.getOrCreate(context);
        imgContext.add(new ImgMacroFile("image01.png", "image/png", "QWSDDDRDOD"));

        final String result1 = engine.render("{img:image00.png}", context);
        assertEquals("{img:image00.png}", result1);

        final String result2 = engine.render("{img:image01.png}", context);
        assertEquals("<img src='cid:QWSDDDRDOD'>", result2);

        final String result3 = engine.render("{img:left|image01.png}", context);
        assertEquals("<img style='float:left;' src='cid:QWSDDDRDOD'>", result3);

        final String result4 = engine.render("{img:l|image01.png}", context);
        assertEquals("<img style='float:left;' src='cid:QWSDDDRDOD'>", result4);

        final String result5 = engine.render("{img:right|image01.png}", context);
        assertEquals("<img style='float:right;' src='cid:QWSDDDRDOD'>", result5);

        final String result6 = engine.render("{img:r|image01.png}", context);
        assertEquals("<img style='float:right;' src='cid:QWSDDDRDOD'>", result6);

        final String result7 = engine.render("{img:center|image01.png}", context);
        assertEquals("<img style='margin-left:auto;margin-right:auto;display:block;' src='cid:QWSDDDRDOD'>", result7);

        final String result8 = engine.render("{img:c|image01.png}", context);
        assertEquals("<img style='margin-left:auto;margin-right:auto;display:block;' src='cid:QWSDDDRDOD'>", result8);

        final String result9 = engine.render("{img:width=11em|image01.png}", context);
        assertEquals("<img style='width:11em;' src='cid:QWSDDDRDOD'>", result9);

        final String result10 = engine.render("{img:w=11em|image01.png}", context);
        assertEquals("<img style='width:11em;' src='cid:QWSDDDRDOD'>", result10);

        final String result11 = engine.render("{img:height=12px|image01.png}", context);
        assertEquals("<img style='height:12px;' src='cid:QWSDDDRDOD'>", result11);

        final String result12 = engine.render("{img:h=12|image01.png}", context);
        assertEquals("<img style='height:12px;' src='cid:QWSDDDRDOD'>", result12);
    }

}
