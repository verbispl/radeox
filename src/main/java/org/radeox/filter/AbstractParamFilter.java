package org.radeox.filter;

import org.radeox.filter.context.FilterContext;
import org.radeox.filter.context.ParamContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;

/**
 * Base class for parameter filters.
 *
 * <p>Created on 2025-05-28</p>
 *
 * @author stephan
 * @team sonicteam
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @see ParamContext
 */
public abstract class AbstractParamFilter extends LocaleRegexTokenFilter
{
    @Override
    public void handleMatch(final StringBuffer buffer, final MatchResult result,
        final FilterContext context)
    {
        final ParamContext param = ParamContext.get(context.getRenderContext());
        final String name = result.group(1);
        if(param != null)
        {
            final Object value = param.get(name);
            if(value != null)
            {
                if(value instanceof String[])
                {
                    buffer.append(((String[]) value)[0]);
                }
                else
                {
                    buffer.append(value);
                }
                return;
            }
        }
        appendEmptyValue(buffer, name);
    }

    @Override
    protected boolean isSingleLine()
    {
        return true;
    }

    abstract void appendEmptyValue(final StringBuffer buffer, String name);

}
