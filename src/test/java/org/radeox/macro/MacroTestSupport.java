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

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

import junit.framework.TestCase;

/**
 * Base macro JUnit test.
 *
 * @author stephan <stephan@fe3c5836-e6e7-0310-9b8e-ac26c1e95e32>
 * @version $Id: MacroTestSupport.java,v 1.3 2004/05/26 08:56:20 stephan Exp $
 */
public abstract class MacroTestSupport extends TestCase
{
    protected RenderContext context;
    protected RenderEngine engine;

    protected MacroTestSupport(final String s)
    {
        super(s);
    }

    @Override
    protected void setUp() throws Exception
    {
        engine = new BaseRenderEngine();
        context = new BaseRenderContext();
    }

}
