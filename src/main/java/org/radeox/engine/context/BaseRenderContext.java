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

package org.radeox.engine.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;

/**
 * Base impementation for RenderContext.
 *
 * @author Stephan J. Schmidt
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id: BaseRenderContext.java,v 1.8 2003/10/07 08:20:24 stephan Exp $
 */
public class BaseRenderContext implements RenderContext
{
    private Locale locale;

    private boolean cacheable = true;
    private boolean tempCacheable = false;

    private RenderEngine engine;
    private final Map<String, Object> values;

    /**
     * Single render context constructor with default locale.
     *
     * @see Locale#getDefault()
     */
    public BaseRenderContext()
    {
        this(Locale.getDefault());
    }

    /**
     * Single render context constructor.
     *
     * @param locale render locale
     */
    public BaseRenderContext(final Locale locale)
    {
        this.locale = locale;
        this.values = new HashMap<>();
    }

    @Override
    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(final Locale locale)
    {
        this.locale = locale;
    }

    @Override
    public Object get(final String key)
    {
        return values.get(key);
    }

    @Override
    public Object computeIfAbsent(final String key, final Function<String, ?> mappingFunction)
    {
        return values.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public void set(final String key, final Object value)
    {
        values.put(key, value);
    }

    @Override
    public RenderEngine getRenderEngine()
    {
        return engine;
    }

    @Override
    public void setRenderEngine(final RenderEngine engine)
    {
        this.engine = engine;
    }

    @Override
    public void setCacheable(final boolean cacheable)
    {
        tempCacheable = cacheable;
    }

    @Override
    public void commitCache()
    {
        cacheable = cacheable && tempCacheable;
        tempCacheable = false;
    }

    @Override
    public boolean isCacheable()
    {
        return cacheable;
    }

}
