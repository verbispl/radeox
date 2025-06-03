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

package org.radeox.api.engine.context;

import java.util.Locale;
import java.util.function.Function;

import org.radeox.api.engine.RenderEngine;

/**
 * RenderContext stores basic data for the context the RenderEngine is called in.
 * <p>
 *   RenderContext can be used by the Engine in whatever way it likes to. The
 *   Radeox RenderEngine uses RenderContext to construct FilterContext.
 * </p>
 *
 * @author Stephan J. Schmidt
 * @version $Id: RenderContext.java,v 1.2 2004/01/30 08:42:56 stephan Exp $
 */
public interface RenderContext
{
    /**
     * Returns the RenderEngine handling this request.
     *
     * @return engine RenderEngine handling the request within this context
     */
    RenderEngine getRenderEngine();

    /**
     * Stores the current RenderEngine of the request.
     *
     * @param engine Current RenderEnginge
     */
    void setRenderEngine(RenderEngine engine);

    Object get(String key);

    Object computeIfAbsent(String key, Function<String, ?> mappingFunction);

    void set(String key, Object value);

    void setCacheable(boolean cacheable);

    void commitCache();

    boolean isCacheable();

    Locale getLocale();

    void setLocale(Locale locale);
}
