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

import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;
import org.radeox.util.Encoder;

/**
 * Transforms multiple \ into single backspaces and escapes other characters.
 *
 * @author leo
 * @team other
 * @version $Id: EscapeFilter.java,v 1.13 2004/04/15 13:56:14 stephan Exp $
 */
public class EscapeFilter extends LocaleRegexTokenFilter implements CacheFilter
{
    @Override
    protected String getLocaleKey()
    {
        return "filter.escape";
    }

    @Override
    public void handleMatch(final StringBuffer buffer, final MatchResult result,
        final FilterContext context)
    {
        buffer.append(handleMatch(result));
    }

    private String handleMatch(final MatchResult result)
    {
        if(result.group(1) == null)
        {
            String match = result.group(2);
            if(match == null)
            {
                match = result.group(3);
            }
            if("\\".equals(match))
            {
                return "\\\\";
            }
            return Encoder.toEntity(match.charAt(0));
        }
        else
        {
            return "&#92;";
        }
    }

    @Override
    public String[] before()
    {
        return FilterPipe.FIRST_BEFORE;
    }

}
