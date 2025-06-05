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

package org.radeox.macro;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.context.BaseFilterContext;
import org.radeox.filter.context.FilterContext;
import org.radeox.macro.code.CodeMacroContext;
import org.radeox.macro.code.SourceCodeFormatter;
import org.radeox.macro.parameter.MacroParameter;
import org.radeox.util.Service;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * Macro for displaying programming language source code.
 * <p>
 *   CodeMacro knows about different source code formatters which can be plugged
 *   into radeox to display more languages. CodeMacro displays Java, Ruby or SQL
 *   code.
 * </p>
 *
 * @author stephan
 * @team sonicteam
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id: CodeMacro.java,v 1.16 2004/04/27 19:30:38 leo Exp $
 * @see CodeMacroContext
 */
public class CodeMacro extends Preserved
{
    private static final Log LOG = LogFactory.getLog(CodeMacro.class);

    private final Map<String, SourceCodeFormatter> formatters;
    private final FilterContext nullContext = new BaseFilterContext();

    private String start;
    private String end;

    @Override
    public String getLocaleKey()
    {
        return "macro.code";
    }

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        super.setInitialContext(context);
        final Locale outputLocale = context.getOutputLocale();
        final String outputName = context.getOutputBundleName();
        final BaseResourceBundle outputMessages = context.getBundle(outputLocale, outputName);
        start = outputMessages.get(getLocaleKey() + ".start");
        end = outputMessages.get(getLocaleKey() + ".end");
    }

    public CodeMacro()
    {
        formatters = new HashMap<>();

        final Iterator<SourceCodeFormatter> formatterIt = Service
            .providers(SourceCodeFormatter.class);
        while(formatterIt.hasNext())
        {
            try
            {
                final SourceCodeFormatter formatter = formatterIt.next();
                final String name = formatter.getName();
                if(formatters.containsKey(name))
                {
                    final SourceCodeFormatter existing = formatters.get(name);
                    if(existing.getPriority() < formatter.getPriority())
                    {
                        formatters.put(name, formatter);
                        LOG.debug("Replacing formatter: " +
                            formatter.getClass() + " (" + name + ")");
                    }
                }
                else
                {
                    formatters.put(name, formatter);
                    LOG.debug("Loaded formatter: " + formatter.getClass() +
                        " (" + name + ")");
                }
            }
            catch(final Exception e)
            {
                LOG.warn("CodeMacro: unable to load code formatter", e);
            }
        }

        addSpecial('[');
        addSpecial(']');
        addSpecial('{');
        addSpecial('}');
        addSpecial('*');
        addSpecial('-');
        addSpecial('#');
        addSpecial('\\');
    }

    @Override
    public void execute(final Writer writer, final MacroParameter params)
    throws IllegalArgumentException, IOException
    {
        SourceCodeFormatter formatter = null;

        final String formatterName = params.get("0");
        if(formatterName != null)
        {
            formatter = formatters.get(formatterName);
            LOG.warn("Formatter not found: " + formatterName);
        }
        if(formatter == null)
        {
            final CodeMacroContext macroContext = CodeMacroContext.getOrCreate(params.getContext());
            formatter = formatters.get(macroContext.getDefaultFormatter());
            if(formatter == null)
            {
                LOG.error("Formatter not found: " + macroContext.getDefaultFormatter());
            }
        }
        writer.write(start);
        if(formatter != null)
        {
            final String result = formatter.filter(params.getContent(), nullContext);
            writer.write(replace(result.trim()));
        }
        else
        {
            writer.write(params.getContent());
        }
        writer.write(end);
    }

}
