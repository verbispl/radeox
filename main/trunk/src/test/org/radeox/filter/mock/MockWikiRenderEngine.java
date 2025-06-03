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

package org.radeox.filter.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.WikiRenderEngine;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.util.Encoder;

public class MockWikiRenderEngine implements RenderEngine, WikiRenderEngine
{

    @Override
    public InitialRenderContext getInitialRenderContext()
    {
        return null;
    }

    @Override
    public boolean exists(final String name)
    {
        return name.equals("SnipSnap") || name.equals("stephan");
    }

    @Override
    public boolean showCreate()
    {
        return true;
    }

    @Override
    public void appendLink(final StringBuffer buffer, final String name,
        final String view, final String anchor)
    {
        buffer.append("link:" + name + "|" + view + "#" + anchor);
    }

    @Override
    public void appendLink(final StringBuffer buffer, final String name,
        final String view)
    {
        buffer.append("link:" + name + "|" + view);
    }

    @Override
    public void appendCreateLink(final StringBuffer buffer, final String name,
        final String view)
    {
        buffer.append("'").append(name).append("' - ");
        buffer.append("'").append(Encoder.escape(name)).append("'");
    }

    @Override
    public String getName()
    {
        return "mock-wiki";
    }

    @Override
    public String render(final String content, final RenderContext context)
    {
        return null;
    }

    @Override
    public void render(final Writer out, final String content,
        final RenderContext context) throws IOException
    {
    }

    @Override
    public String render(final Reader in, final RenderContext context)
        throws IOException
    {
        return null;
    }

}
