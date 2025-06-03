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

import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.IncludeRenderEngine;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.RegexTokenFilter;
import org.radeox.macro.Macro;
import org.radeox.macro.MacroRepository;
import org.radeox.macro.Repository;
import org.radeox.macro.parameter.MacroParameter;
import org.radeox.regex.MatchResult;
import org.radeox.util.StringBufferWriter;

/**
 * Class that finds snippets (macros) like
 * {link:neotis|http://www.neotis.de} ---> <a href="....>
 * {!neotis} -> include neotis object, e.g. a wiki page
 *
 * Macros can built with a start and an end, e.g.
 * {code}
 *     ...
 * {code}
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: MacroFilter.java,v 1.18 2004/04/15 13:56:14 stephan Exp $
 */
public class MacroFilter extends RegexTokenFilter
{
    private static final Log LOG = LogFactory.getLog(MacroFilter.class);

    // Map of known macros with name and macro object
    private MacroRepository macros;

    public MacroFilter()
    {
        // optimized by Jeffrey E.F. Friedl
        super("\\{([^:}]+)(?::([^\\}]*))?\\}(.*?)\\{\\1\\}", SINGLELINE);
        addRegex("\\{([^:}]+)(?::([^\\}]*))?\\}", "", MULTILINE);
    }

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        macros = MacroRepository.getInstance();
        macros.setInitialContext(context);
    }

    protected Repository<Macro> getMacroRepository()
    {
        return macros;
    }

    @Override
    public void handleMatch(final StringBuffer buffer, final MatchResult result,
        final FilterContext context)
    {
        final String command = result.group(1);

        // {$peng} are variables not macros.
        if(command != null && !command.startsWith("$"))
        {
            final MacroParameter mParams = context.getMacroParameter();
            switch(result.groups())
            {
                case 3:
                    mParams.setContent(result.group(3));
                    mParams.setContentStart(result.beginOffset(3));
                    mParams.setContentEnd(result.endOffset(3));
                case 2:
                    mParams.setParams(result.group(2));
            }
            mParams.setStart(result.beginOffset(0));
            mParams.setEnd(result.endOffset(0));

            // @DANGER: recursive calls may replace macros in included
            // source code
            try
            {
                if(getMacroRepository().containsKey(command))
                {
                    final Macro macro = getMacroRepository()
                        .get(command);
                    // recursively filter macros within macros
                    if(null != mParams.getContent())
                    {
                        mParams.setContent(
                            filter(mParams.getContent(), context));
                    }
                    final Writer writer = new StringBufferWriter(buffer);
                    macro.execute(writer, mParams);
                }
                else if(command.startsWith("!"))
                {
                    // @TODO including of other snips
                    final RenderEngine engine = context.getRenderContext().getRenderEngine();
                    if(engine instanceof IncludeRenderEngine)
                    {
                        final String include = ((IncludeRenderEngine) engine)
                            .include(command.substring(1));
                        if(null != include)
                        {
                            buffer.append(include);
                        }
                        else
                        {
                            buffer.append(command.substring(1) + " not found.");
                        }
                    }
                }
                else
                {
                    buffer.append(result.group(0));
                }
            }
            catch(final IllegalArgumentException e)
            {
                buffer.append("<div class=\"error\">" + command + ": " +
                    e.getMessage() + "</div>");
            }
            catch(final Exception e)
            {
                LOG.warn("MacroFilter: unable to format macro: " + result.group(1), e);
                buffer.append("<div class=\"error\">" + command + ": " +
                    e.getMessage() + "</div>");
            }
        }
        else
        {
            buffer.append(result.group(0));
        }
    }

}
