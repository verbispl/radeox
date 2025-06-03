package org.radeox.macro.code;

import org.radeox.api.engine.context.RenderContext;
import org.radeox.macro.CodeMacro;

/**
 * Context for {@link CodeMacro}.
 *
 * <p>Created on 2025-06-02</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class CodeMacroContext
{
    private static final String KEY = CodeMacroContext.class.getName()+"_KEY";

    private String defaultFormatter = "java";

    public String getDefaultFormatter()
    {
        return defaultFormatter;
    }

    public void setDefaultFormatter(final String defaultFormatter)
    {
        this.defaultFormatter = defaultFormatter;
    }

    public static CodeMacroContext getOrCreate(final RenderContext context)
    {
        return (CodeMacroContext) context.computeIfAbsent(KEY, k -> new CodeMacroContext());
    }

}
