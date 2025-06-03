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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.radeox.macro.parameter.MacroParameter;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * MacroListMacro displays a list of all known macros of the EngineManager with
 * their name, parameters and a description.
 *
 * @author Matthias L. Jugel
 * @version $Id: MacroListMacro.java,v 1.11 2004/05/11 12:17:21 leo Exp $
 */
public class MacroListMacro extends BaseLocaleMacro
{
    @Override
    public String getLocaleKey()
    {
        return "macro.macrolist";
    }

    @Override
    public void execute(final Writer writer, final MacroParameter params)
        throws IllegalArgumentException, IOException
    {
        final Locale locale = params.getContext().getLocale();
        final List<Macro> macros = MacroRepository.getInstance().getPlugins();
        if(params.getLength() == 0)
        {
            appendTo(writer, locale, macros);
        }
        else
        {
            final Set<String> allowedMacros = params.getParams().values().stream()
                .collect(Collectors.toSet());
            final List<Macro> filtered = macros.stream()
                .filter(m -> allowedMacros.contains(m.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
            appendTo(writer, locale, filtered);
        }
    }

    private Writer appendTo(final Writer writer, final Locale locale, final List<Macro> macroList)
        throws IOException
    {
        final String bundleName = initialContext.getLanguageBuldleName();
        Collections.sort(macroList);
        final BaseResourceBundle bundle = initialContext.getBundle(locale, bundleName);
        writer.write("<table class=\"wiki-table\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
        writer.write("<tr><th>");
        writer.write(bundle.getString(getLocaleKey() + ".title.macro"));
        writer.write("</th>");
        writer.write("<th>");
        writer.write(bundle.getString(getLocaleKey() + ".title.parameters"));
        writer.write("</th>");
        writer.write("<th>");
        writer.write(bundle.getString(getLocaleKey() + ".title.description"));
        writer.write("</th></tr>\n");
        for(final Macro macro : macroList)
        {
            writer.write("<tr>");
            writer.write("<td>");
            writer.write(macro.getName());
            writer.write("</td><td>");
            final String[] params = macro.getParamDescription(locale);
            if(params.length == 0)
            {
                writer.write("<i>");
                writer.write(bundle.getString(getLocaleKey() + ".noparams"));
                writer.write("</i>");
            }
            else
            {
                for(int i = 0; i < params.length; i++)
                {
                    final String parameter = params[i].trim();
                    // display missing resources in RED
                    if(parameter.startsWith("???") && parameter.endsWith("???"))
                    {
                        writer.write("<div class=\"error\">");
                        writer.write(parameter);
                        writer.write("</div>");
                    }
                    else if(parameter.startsWith("?"))
                    {
                        writer.write(parameter.substring(1));
                        writer.write(" <i>");
                        writer.write(bundle.getString(getLocaleKey() + ".optional"));
                        writer.write("</i>");
                    }
                    else
                    {
                        writer.write(params[i]);
                    }
                    writer.write("<br/>");
                }
            }
            writer.write("</td><td>");
            writer.write(macro.getDescription(locale));
            if(macro instanceof LocaleMacro)
            {
                final String example = bundle.getString(((LocaleMacro) macro).getLocaleKey() + ".example");
                if(!example.startsWith("???"))
                {
                    writer.write("<p class='paragraph'><div>");
                    writer.write(bundle.getString(getLocaleKey() + ".label.example"));
                    writer.write(": <pre>");
                    writer.write(example.replace("{", "&#123;"));
                    writer.write("</pre></div></p>");
                }
            }
            writer.write("</td>\n");
        }
        writer.write("</table>");
        return writer;
    }
}
