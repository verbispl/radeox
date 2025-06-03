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

import java.util.Locale;
import java.util.MissingResourceException;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * Class that implements base functionality to write macros.
 *
 * <p>Created on 2004-11-033</p>
 *
 * @author stephan
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id: BaseMacro.java,v 1.3 2004/04/27 19:30:38 leo Exp $
 */
public abstract class BaseMacro implements Macro, LocaleMacro
{
    protected InitialRenderContext initialContext;

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        this.initialContext = context;
    }

    @Override
    public String getDescription(final Locale locale)
    {
        final String bundleName = initialContext.getLanguageBuldleName();
        final BaseResourceBundle bundle = initialContext.getBundle(locale,
            bundleName);
        return bundle.getString(getLocaleKey() + ".description");
    }

    @Override
    public String[] getParamDescription(final Locale locale)
    {
        final String buldleName = initialContext.getLanguageBuldleName();
        final BaseResourceBundle bundle = initialContext.getBundle(locale,
            buldleName);
        try
        {
            return bundle.get(getLocaleKey() + ".params").split(";");
        }
        catch(final MissingResourceException e)
        {
            return new String[0];
        }
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public int compareTo(final Object object)
    {
        final Macro macro = (Macro) object;
        return getName().compareTo(macro.getName());
    }

}
