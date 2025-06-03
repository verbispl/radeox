/*
 *      Copyright 2001-2004 Fraunhofer Gesellschaft, Munich, Germany, for its
 *      Fraunhofer Institute Computer Architecture and Software Technology
 *      (FIRST), Berlin, Germany
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.radeox.filter;

import java.text.MessageFormat;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;

/**
 * Transforms header style lines into subsections. A header starts with a 1 for
 * first level headers and 1.1 for secend level headers. Headers are
 * numbered automatically.
 *
 * @author leo
 * @team other
 * @version $Id: HeadingFilter.java,v 1.8 2004/04/15 13:56:14 stephan Exp $
 */
public class HeadingFilter extends LocaleRegexTokenFilter implements CacheFilter
{
    private MessageFormat formatter;

    @Override
    protected String getLocaleKey()
    {
        return "filter.heading";
    }

    @Override
    public void handleMatch(final StringBuffer buffer,
        final MatchResult result, final FilterContext context)
    {
        buffer.append(handleMatch(result));
    }

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        super.setInitialContext(context);
        final String outputTemplate = outputMessages.get(getLocaleKey() + ".print");
        formatter = new MessageFormat("");
        formatter.applyPattern(outputTemplate);
    }

    private String handleMatch(final MatchResult result)
    {
        String match = result.group(1);
        match = match.replaceAll("\\.", "");
        match = match.replaceAll(".", "1-");
        match = match.substring(0, match.length() - 1);
        return formatter.format(new Object[] {match, result.group(3)});
    }

}
