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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.util.Encoder;
import org.radeox.util.i18n.BaseResourceBundle;

/**
 * A specialized macro that allows to preserve certain special characters by
 * creating character entities.
 * <p>
 *   The subclassing macro may decide whether to call {@link #replace(String)}
 *   before or after executing the actual macro substitution.
 * </p>
 *
 * @author Matthias L. Jugel
 * @version $Id: Preserved.java,v 1.7 2004/06/08 08:46:18 leo Exp $
 */
public abstract class Preserved extends BaseMacro
{
    private final Map<String,String> special = new HashMap<>();
    private String specialString = "";

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
        name = inputMessages.get(getLocaleKey() + ".name");
    }

    /**
     * Encode special character c by replacing with it's hex character entity
     * code.
     */
    protected void addSpecial(final char c)
    {
        addSpecial("" + c, Encoder.toEntity(c));
    }

    /**
     * Add a replacement for the special character c which may be a string
     *
     * @param c the character to replace
     * @param replacement the new string
     */
    protected void addSpecial(final String c, final String replacement)
    {
        specialString += c;
        special.put(c, replacement);
    }

    /**
     * Actually replace specials in source. This method can be used by
     * subclassing macros.
     *
     * @param source String to encode
     *
     * @return encoded Encoded string
     */
    protected String replace(final String source)
    {
        final StringBuilder tmp = new StringBuilder();
        final StringTokenizer st = new StringTokenizer(source, specialString, true);
        String previous = "";
        while(st.hasMoreTokens())
        {
            String current = st.nextToken();
            if(special.containsKey(current) && !previous.endsWith("&"))
            {
                current = special.get(current);
            }
            tmp.append(current);
            previous = current;
        }
        return tmp.toString();
    }

}
