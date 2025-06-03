package org.radeox.macro;

import java.io.IOException;
import java.io.Writer;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.context.ParamContext;
import org.radeox.macro.img.ImageMacroFile;
import org.radeox.macro.img.ImgMacroContext;
import org.radeox.macro.parameter.MacroParameter;

/**
 * Html image rendering macro.
 * <p>
 *   It renders <code>img</code> html tag when placed into html page:
 *   <pre>
 *       &lt;img src='data:image/png;charset=utf-8;base64,AAECAw=='>
 *   </pre>
 *   and when use in MIME content:
 *   <pre>
 *      &lt;img src='cid:QWSDDDRDOD'>
 *   </pre>
 * </p>
 * <p>Created on 2025-05-28</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @see ImgMacroContext
 * @see ImageMacroFile
 */
public class ImgMacro extends BaseLocaleMacro
{
    @Override
    public String getLocaleKey()
    {
        return "macro.img";
    }

    @Override
    public void execute(final Writer writer, final MacroParameter params)
        throws IllegalArgumentException, IOException
    {
        final RenderContext context = params.getContext();

        final String width = toCss(params.get("width", "w"));
        final String height = toCss(params.get("height", "h"));
        boolean left = false;
        boolean right = false;
        boolean center = false;
        String filename = null;

        final int paramLength = params.getLength();
        for(int i = 0; i < paramLength; ++i)
        {
            final String value = params.get(i);
            if(value == null || value.startsWith("width") || value.startsWith("height") || value.equals("w") || value.equals("h"))
            {
                continue;
            }
            if(value.equals("left") || value.equals("l"))
            {
                left = true;
            }
            else if(value.equals("right") || value.equals("r"))
            {
                right = true;
            }
            else if(value.equals("center") || value.equals("c"))
            {
                center = true;
            }
            else
            {
                filename = value;
            }
        }
        if(filename == null)
        {
            writer.write("{img:right|width=20em|height=10em|example-file.png}");
            return;
        }
        final ImgMacroContext imgContext = ImgMacroContext.getOrCreate(context);
        final ImageMacroFile file = imgContext.get(filename);
        if(file == null || file.isNoContent())
        {
            writeImageNotFound(writer, width, height, left, right, center, filename);
            return;
        }
        writer.write("<img ");
        writeStyle(writer, width, height, left, right, center);
        if(file.getContent() != null)
        {
            writeBase64Content(writer, context, imgContext, file);
        }
        else
        {
            writeMimeRef(writer, file);
        }
        writer.write(">");
    }

    private void writeMimeRef(final Writer writer, final ImageMacroFile file)
        throws IOException
    {
        writer.write("src='cid:");
        writer.write(file.getContentId());
        writer.write("'");
    }

    private void writeBase64Content(final Writer writer,
        final RenderContext context, final ImgMacroContext imgContext,
        final ImageMacroFile file) throws IOException
    {
        writer.write("src='data:");
        writer.write(file.getMime());
        writer.write(";charset=utf-8;base64,{$$");
        final String varialbeName = imgContext.nextVarialbeName();
        writer.write(varialbeName);
        writer.write("}'");
        // value of the imgage will be
        final ParamContext paramContext = ParamContext.getOrCreate(context);
        final String base64 = Base64.getEncoder().encodeToString(file.getContent());
        paramContext.put(varialbeName, base64);
    }

    private void writeStyle(final Writer writer, final String width,
        final String height, final boolean left, final boolean right, final boolean center)
        throws IOException
    {
        final boolean hasStyle = left || right || center || height != null ||
            width != null;
        if(hasStyle)
        {
            writer.write("style='");
        }
        if(left)
        {
            writer.write("float:left;");
        }
        if(right)
        {
            writer.write("float:right;");
        }
        if(center)
        {
            writer.write("margin-left:auto;margin-right:auto;display:block;");
        }
        if(width != null)
        {
            writer.write("width:");
            writer.write(width);
            writer.write(";");
        }
        if(height != null)
        {
            writer.write("height:");
            writer.write(height);
            writer.write(";");
        }
        if(hasStyle)
        {
            writer.write("' ");
        }
    }

    private void writeImageNotFound(final Writer writer, final String width,
        final String height, final boolean left, final boolean right, final boolean center,
        final String filename) throws IOException
    {
        writer.write("{img:");
        if(left)
        {
            writer.write("left|");
        }
        if(right)
        {
            writer.write("right|");
        }
        if(center)
        {
            writer.write("center|");
        }
        if(width != null)
        {
            writer.write("width=");
            writer.write(width);
            writer.write("|");
        }
        if(height != null)
        {
            writer.write("height=");
            writer.write(height);
            writer.write("|");
        }
        writer.write(filename);
        writer.write("}");
    }

    private static String toCss(final String size)
    {
        final String trimmed = StringUtils.trimToNull(size);
        if(trimmed == null)
        {
            return null;
        }
        final char last = trimmed.charAt(trimmed.length() - 1);
        if(Character.isDigit(last))
        {
            return trimmed + "px";
        }
        return trimmed;
    }

}
