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

package org.radeox.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.Filter;
import org.radeox.filter.FilterPipe;
import org.radeox.filter.context.BaseFilterContext;
import org.radeox.filter.context.FilterContext;
import org.radeox.util.Service;

/**
 * Base implementation of RenderEngine.
 *
 * @author Stephan J. Schmidt
 * @version $Id: BaseRenderEngine.java,v 1.18 2004/05/26 08:56:20 stephan Exp $
 */
public class BaseRenderEngine implements RenderEngine
{
    public static final String NAME = "radeox";

    private static final Log LOG = LogFactory.getLog(BaseRenderEngine.class);

    protected InitialRenderContext initialContext;
    protected FilterPipe fp;

    public BaseRenderEngine(final InitialRenderContext context)
    {
        this.initialContext = context;
        init();
        initialContext.setFilterPipe(fp);
    }

    public BaseRenderEngine()
    {
        this(InitialRenderContext.defaultContext());
    }

    @Override
    public InitialRenderContext getInitialRenderContext()
    {
        return initialContext;
    }

    private void init()
    {
        fp = new FilterPipe(initialContext);
        final Iterator<Filter> iterator = Service.providers(Filter.class);
        while(iterator.hasNext())
        {
            try
            {
                final Filter filter = iterator.next();
                fp.addFilter(filter);
                LOG.debug("Loaded filter: " + filter.getClass().getName());
            }
            catch(final Exception e)
            {
                LOG.warn("BaseRenderEngine: unable to load filter", e);
            }
        }
        fp.init();
    }

    /**
     * Name of the RenderEngine. This is used to get a RenderEngine instance
     * with EngineManager.getInstance(name);
     *
     * @return name Name of the engine
     */
    @Override
    public String getName()
    {
        return NAME;
    }

    /**
     * Render an input with text markup and return a String with e.g. HTML
     *
     * @param content String with the input to render
     * @param context Special context for the filter engine, e.g. with
     *      configuration information
     * @return result Output with rendered content
     */
    @Override
    public String render(final String content, final RenderContext context)
    {
        final FilterContext filterContext = new BaseFilterContext();
        filterContext.setRenderContext(context);
        return fp.filter(content, filterContext);
    }

    /**
     * Render an input with text markup from a Reader and write the result to a
     * writer.
     *
     * @param in Reader to read the input from
     * @param context Special context for the render engine, e.g. with
     *      configuration information
     */
    @Override
    public String render(final Reader in, final RenderContext context)
        throws IOException
    {
        final StringBuilder buffer = new StringBuilder();
        final BufferedReader inputReader = new BufferedReader(in);
        String line;
        while((line = inputReader.readLine()) != null)
        {
            buffer.append(line);
        }
        return render(buffer.toString(), context);
    }

    @Override
    public void render(final Writer out, final String content,
        final RenderContext context) throws IOException
    {
        out.write(render(content, context));
    }

}
