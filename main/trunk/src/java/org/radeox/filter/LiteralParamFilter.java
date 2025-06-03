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

import org.radeox.filter.context.ParamContext;

/**
 * LiteralParamFilter replaces parametes as a last stage of markup transformation.
 * <p>
 *   These parameters could be read from an HTTP request and put in MacroFilter.
 *   A parameter is replaced in {$$paramName}.
 * </p>
 *
 * <p>Created on 2025-06-02</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @see ParamContext
 */
public class LiteralParamFilter extends AbstractParamFilter
{
    @Override
    protected String getLocaleKey()
    {
        return "filter.lparam";
    }

    @Override
    void appendEmptyValue(final StringBuffer buffer, final String name)
    {
        buffer.append("{$$");
        buffer.append(name);
        buffer.append("}");
    }

}
