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

package org.radeox.macro.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.context.ParamContext;

/**
 *
 * @author
 * @version $Id: BaseMacroParameter.java,v 1.12 2004/05/03 11:12:37 stephan Exp $
 */
public class BaseMacroParameter implements MacroParameter
{
    private String content;
    protected Map<String,String> params;
    private int size;
    protected RenderContext context;
    private int start;
    private int end;
    private int contentStart;
    private int contentEnd;

    public BaseMacroParameter()
    {
    }

    public BaseMacroParameter(final RenderContext context)
    {
        this.context = context;
    }

    @Override
    public void setParams(final String stringParams)
    {
        params = split(stringParams, "|");
        size = params.size();
    }

    @Override
    public RenderContext getContext()
    {
        return context;
    }

    @Override
    public Map<String,String> getParams()
    {
        return params;
    }

    @Override
    public String getContent()
    {
        return content;
    }

    @Override
    public void setContent(final String content)
    {
        this.content = content;
    }

    @Override
    public int getLength()
    {
        return size;
    }

    @Override
    public String get(final String index, final int idx)
    {
        String result = get(index);
        if(result == null)
        {
            result = get(idx);
        }
        return result;
    }

    @Override
    public String get(final String key1, final String key2)
    {
        String result = get(key1);
        if(result == null)
        {
            result = get(key2);
        }
        return result;
    }

    @Override
    public String get(final String index)
    {
        return params.get(index);
    }

    @Override
    public String get(final int index)
    {
        return get(Integer.toString(index));
    }

    /**
     * Splits a String on a delimiter to a List. The function works like the
     * perl-function split.
     *
     * @param aString a String to split
     * @param delimiter a delimiter dividing the entries
     * @return a Array of splittet Strings
     */
    public Map<String,String> split(final String aString, final String delimiter)
    {
        final Map<String,String> result = new HashMap<>();

        if(null != aString)
        {
            final StringTokenizer st = new StringTokenizer(aString, delimiter);
            int i = 0;

            while(st.hasMoreTokens())
            {
                String value = st.nextToken();
                String key = "" + i;
                if(value.indexOf("=") != -1)
                {
                    result.put(key, insertValue(value));

                    final int index = value.indexOf("=");
                    key = value.substring(0, index);
                    value = value.substring(index + 1);

                    result.put(key, insertValue(value));
                }
                else
                {
                    result.put(key, insertValue(value));
                }
                i++;
            }
        }
        return result;
    }

    private String insertValue(final String s)
    {
        final int idx = s.indexOf('$');
        if(idx != -1)
        {
            final StringBuilder tmp = new StringBuilder();
            final String varName = s.substring(idx + 1);
            if(idx > 0)
            {
                tmp.append(s.substring(0, idx));
            }
            final ParamContext globals = ParamContext.get(context);
            if(globals != null)
            {
                final Object value = globals.get(varName);
                if(value != null)
                {
                    tmp.append(value);
                }
            }
            return tmp.toString();
        }
        return s;
    }

    @Override
    public void setStart(final int start)
    {
        this.start = start;
    }

    @Override
    public void setEnd(final int end)
    {
        this.end = end;
    }

    @Override
    public int getStart()
    {
        return this.start;
    }

    @Override
    public int getEnd()
    {
        return this.end;
    }

    @Override
    public int getContentStart()
    {
        return contentStart;
    }

    @Override
    public void setContentStart(final int contentStart)
    {
        this.contentStart = contentStart;
    }

    @Override
    public int getContentEnd()
    {
        return contentEnd;
    }

    @Override
    public void setContentEnd(final int contentEnd)
    {
        this.contentEnd = contentEnd;
    }

}
