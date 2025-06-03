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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.FilterSupport;
import org.radeox.regex.Pattern;

/**
 * Class that stores regular expressions, can be subclassed for special Filters.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: RegexFilter.java,v 1.11 2004/04/15 13:56:14 stephan Exp $
 */
public abstract class RegexFilter extends FilterSupport
{
    private static Log log = LogFactory.getLog(RegexFilter.class);

    protected List<Pattern> pattern = new ArrayList<>();
    protected List<String> substitute = new ArrayList<>();

    public static final boolean SINGLELINE = false;
    public static final boolean MULTILINE = true;

    protected RegexFilter()
    {

    }

    /**
     * Create a new regular expression that takes input as multiple lines.
     */
    protected RegexFilter(final String regex, final String substitute)
    {
        addRegex(regex, substitute);
    }

    /**
     * Create a new regular expression and set.
     */
    protected RegexFilter(final String regex, final String substitute,
        final boolean multiline)
    {
        addRegex(regex, substitute, multiline);
    }

    public void clearRegex()
    {
        pattern.clear();
        substitute.clear();
    }

    public void addRegex(final String regex, final String substitute)
    {
        addRegex(regex, substitute, MULTILINE);
    }

    public void addRegex(final String regex, final String substitute,
        final boolean multiline)
    {
        // compiler.compile(regex, (multiline ? Perl5Compiler.MULTILINE_MASK :
        // Perl5Compiler.SINGLELINE_MASK) | Perl5Compiler.READ_ONLY_MASK));
        try
        {
            final org.radeox.regex.Compiler compiler = org.radeox.regex.Compiler
                .create();
            compiler.setMultiline(multiline);
            this.pattern.add(compiler.compile(regex));
            // Pattern.DOTALL
            this.substitute.add(substitute);
        }
        catch(final Exception e)
        {
            log.warn("bad pattern: " + regex + " -> " + substitute + " " + e);
        }
    }

}
