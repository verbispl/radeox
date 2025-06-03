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
import org.radeox.regex.Matcher;
import org.radeox.regex.Pattern;

/**
 * Class that applies a RegexFilter, can be subclassed for special Filters.
 * <p>
 *   Regular expressions in the input are replaced with strings.
 * </p>
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: RegexReplaceFilter.java,v 1.8 2004/04/15 13:56:14 stephan Exp $
 */
public class RegexReplaceFilter extends RegexFilter
{
    private static final Log LOG = LogFactory.getLog(RegexReplaceFilter.class);

    public RegexReplaceFilter()
    {

    }

    public RegexReplaceFilter(final String regex, final String substitute)
    {
        super(regex, substitute);
    }

    public RegexReplaceFilter(final String regex, final String substitute,
        final boolean multiline)
    {
        super(regex, substitute, multiline);
    }

    @Override
    public String filter(final String input, final FilterContext context)
    {
        String result = input;
        final int size = pattern.size();
        Pattern p;
        String s;
        for(int i = 0; i < size; i++)
        {
            p = pattern.get(i);
            s = substitute.get(i);
            try
            {
                final Matcher matcher = Matcher.create(result, p);
                result = matcher.substitute(s);

                // Util.substitute(matcher, p, new Perl5Substitution(s,
                // interps), result, limit);
            }
            catch(final Exception e)
            {
                LOG.warn("Exception for: " + this, e);
            }
            catch(final Error err)
            {
                LOG.warn("Error for: " + this, err);
            }
        }
        return result;
    }

}
