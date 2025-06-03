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

import java.util.Locale;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * Class that extends RegexReplaceFilter but reads patterns from a locale file
 * instead of hardwired regex.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: LocaleRegexReplaceFilter.java,v 1.6 2003/10/07 08:20:24 stephan Exp $
 */
public abstract class LocaleRegexReplaceFilter extends RegexReplaceFilter
{
    private String modifier = null;

    protected abstract String getLocaleKey();

    public void setModifier(final String modifier)
    {
        this.modifier = modifier;
    }

    public String getModifier()
    {
        return modifier;
    }

    protected boolean isSingleLine()
    {
        return false;
    }

    protected BaseResourceBundle getInputBundle()
    {
        final Locale inputLocale = initialContext.getInputLocale();
        final String inputName = initialContext.getInputBundleName();
        return initialContext.getBundle(inputLocale, inputName);
    }

    protected BaseResourceBundle getOutputBundle()
    {
        final String outputName = initialContext.getOutputBundleName();
        final Locale outputLocale = initialContext.getOutputLocale();
        return initialContext.getBundle(outputLocale, outputName);
    }

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        super.setInitialContext(context);
        clearRegex();

        final BaseResourceBundle outputMessages = getOutputBundle();
        final BaseResourceBundle inputMessages = getInputBundle();

        final String match;
        final String print;
        if(modifier != null)
        {
            match = inputMessages.getString(getLocaleKey() + "." + modifier + ".match");
            print = outputMessages.getString(getLocaleKey() + "." + modifier + ".print");
            addRegex(match, print, isSingleLine() ? RegexReplaceFilter.SINGLELINE
                : RegexReplaceFilter.MULTILINE);
        }
        else
        {
            match = inputMessages.getString(getLocaleKey() + ".match");
            print = outputMessages.getString(getLocaleKey() + ".print");
        }
        addRegex(match, print, isSingleLine() ? RegexReplaceFilter.SINGLELINE
            : RegexReplaceFilter.MULTILINE);
    }

}
