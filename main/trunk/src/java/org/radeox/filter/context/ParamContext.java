package org.radeox.filter.context;

import java.util.HashMap;
import java.util.Map;

import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.LiteralParamFilter;
import org.radeox.filter.ParamFilter;

/**
 * Parameters for this execution context.
 * <p>
 *   These parameters are read when encountering a variable in macros like
 *   {search:$query} or by {@link ParamFilter} in {$query} or by
 *   {@link LiteralParamFilter} in {$$query}. Query is then read from the
 *   parameter map before given to the macro.
 * </p>
 *
 * <p>Created on 2025-05-28</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ParamContext
{
    private static final String KEY = ParamContext.class.getName()+"_KEY";

    private final Map<String, Object> params = new HashMap<>();

    public Object get(final String key)
    {
        return params.get(key);
    }

    public Object put(final String key, final Object value)
    {
        return params.put(key, value);
    }

    /**
     * Return current object of {@link ParamFilter} context.
     *
     * @param context curent render context
     * @return object of context
     */
    public static ParamContext getOrCreate(final RenderContext context)
    {
        return (ParamContext) context.computeIfAbsent(KEY, k -> new ParamContext());
    }

    /**
     * Return object of filter context when avaliable.
     *
     * @param context curent render context
     * @return param context or <code>null</code> when not avaliable.
     */
    public static ParamContext get(final RenderContext context)
    {
        return (ParamContext) context.get(KEY);
    }

}
