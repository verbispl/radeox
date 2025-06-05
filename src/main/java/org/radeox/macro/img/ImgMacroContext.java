package org.radeox.macro.img;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.LiteralParamFilter;
import org.radeox.macro.ImgMacro;

/**
 * Context for {@link ImgMacro}.
 * <p>
 *   Should be used, whem renderer should include images into its output.
 * </p>
 *
 * <p>Created on 2025-05-28</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ImgMacroContext
{
    private static final String KEY = ImgMacroContext.class.getName()+"_KEY";

    private final List<ImgMacroFile> files;
    private Map<String, ImgMacroFile> map;
    private int index;

    public ImgMacroContext()
    {
        files = new ArrayList<>();
        index = 0;
    }

    public void addAll(final Collection<ImgMacroFile> files)
    {
        this.files.addAll(files);
        this.map = null;
    }

    public void add(final ImgMacroFile file)
    {
        this.files.add(file);
        this.map = null;
    }

    public void clear()
    {
        files.clear();
    }

    /**
     * Get file info for given filename.
     *
     * @param filename the name of the file
     * @return file info
     */
    public ImgMacroFile get(final String filename)
    {
        if(map == null)
        {
            map = files.stream()
                .collect(Collectors.toMap(ImgMacroFile::getFilename, Function.identity()));
        }
        return map.get(filename);
    }

    /**
     * Generates valuename for {@link LiteralParamFilter}, to substitute this
     * value after every filters, to protect its content.
     *
     * @return new unique value name of the shape Base64Image_1
     */
    public String nextVarialbeName()
    {
        return "Base64Image_" + (++index);
    }

    /**
     * Obtain current context for {@link ImgMacro}. If context is not avaliable
     * new context will be created.
     *
     * @param context current rendererm macro
     * @return context object
     */
    @Nonnull
    public static ImgMacroContext getOrCreate(final RenderContext context)
    {
        return (ImgMacroContext) context.computeIfAbsent(KEY, k -> new ImgMacroContext());
    }

}
