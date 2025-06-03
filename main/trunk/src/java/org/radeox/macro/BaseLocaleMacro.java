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

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * Class that implements base functionality to write macros and reads it's name
 * from a locale file
 *
 * @author stephan
 * @version $Id: BaseLocaleMacro.java,v 1.6 2004/04/27 19:30:38 leo Exp $
 */
public abstract class BaseLocaleMacro extends BaseMacro implements LocaleMacro
{
    private String name;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setInitialContext(final InitialRenderContext context)
    {
        super.setInitialContext(context);
        final Locale inputLocale = context.getInputLocale();
        final String inputName = context.getInputBundleName();
        final BaseResourceBundle inputMessages = context.getBundle(inputLocale, inputName);
        name = inputMessages.getString(getLocaleKey() + ".name");
    }

}
