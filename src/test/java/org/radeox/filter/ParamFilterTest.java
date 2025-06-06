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
 * JUnit test for {@link ParamFilter}.
 *
 * <p>Created on 2004-11-03</p>
 *
 * @author stephan
 * @version $Id$
 */
public class ParamFilterTest extends FilterTestSupport
{
    @Override
    protected void setUp() throws Exception
    {
        filter = new ParamFilter();
        super.setUp();
    }

    public void testParam()
    {
        final ParamContext params = ParamContext.getOrCreate(context.getRenderContext());
        params.put("var1", "test");
        assertEquals("test", filter.filter("{$var1}", context));
    }

}
