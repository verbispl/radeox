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

package org.radeox.filter.regex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.context.FilterContext;
import org.radeox.regex.MatchResult;
import org.radeox.regex.Matcher;
import org.radeox.regex.Pattern;

/**
 * Filter that calls a special handler method
 * {@link #handleMatch(StringBuffer, MatchResult, FilterContext)} for every
 * occurance of a regular expression.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: RegexTokenFilter.java,v 1.11 2004/04/16 07:47:41 stephan Exp $
 */
public abstract class RegexTokenFilter extends RegexFilter
{
    private static final Log LOG = LogFactory.getLog(RegexTokenFilter.class);

    protected RegexTokenFilter()
    {
        super();
    }

    /**
     * create a new regular expression and set
     */
    protected RegexTokenFilter(final String regex, final boolean multiline)
    {
        super(regex, "", multiline);
    }

    /**
     * create a new regular expression and set
     */
    protected RegexTokenFilter(final String regex)
    {
        super(regex, "");
    }

    protected void setUp(final FilterContext context)
    {
    }

    /**
     * Method is called for every occurance of a regular expression.
     *
     * @param buffer Buffer to write replacement string to
     * @param result Hit with the found regualr expression
     * @param context FilterContext for filters
     */
    public abstract void handleMatch(StringBuffer buffer, MatchResult result,
        FilterContext context);

    @Override
    public String filter(final String input, final FilterContext context)
    {
        setUp(context);

        String result = input;
        final int size = pattern.size();
        for(int i = 0; i < size; i++)
        {
            final Pattern p = pattern.get(i);
            try
            {
                final Matcher m = Matcher.create(result, p);
                result = m.substitute((buffer, result1) -> handleMatch(buffer, result1, context));
            }
            catch(final Exception e)
            {
                LOG.warn("<span class=\"error\">Exception</span>: " + this, e);
            }
        }
        return result;
    }

}
