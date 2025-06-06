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

import java.lang.reflect.Method;

import org.jmock.MockObjectTestCase;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.engine.context.BaseRenderContext;
import org.radeox.filter.context.BaseFilterContext;
import org.radeox.filter.context.FilterContext;

/**
 * Support class for defning JUnit FilterTests.
 *
 * @author Stephan J. Schmidt
 * @version $Id: FilterTestSupport.java,v 1.7 2003/08/14 07:46:04 stephan Exp $
 */
abstract class FilterTestSupport extends MockObjectTestCase
{
    protected Filter filter;
    protected Filter filterCreole;
    protected FilterContext context;

    protected FilterTestSupport()
    {
        context = new BaseFilterContext();
        context.setRenderContext(new BaseRenderContext());
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        final InitialRenderContext context = InitialRenderContext.defaultContext();
        if(filter != null)
        {
            filter.setInitialContext(context);
        }
        try
        {
            filterCreole = filter.getClass().newInstance();
            final Method setModifierMethod = filterCreole.getClass()
                .getMethod("setModifier", String.class);
            setModifierMethod.invoke(filterCreole, "Creole");
            filterCreole.setInitialContext(context);
        }
        catch(final Exception e)
        {
            // ignore errors ...
        }
    }

}
