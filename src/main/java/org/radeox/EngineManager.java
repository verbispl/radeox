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

package org.radeox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.RenderEngine;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.util.Service;

/**
 * Acess point to dock several different rendering engines into e.g. SnipSnap.
 * <p>
 *   SHOULD BE REMOVED. If this functionality is needed, it will be replaced by
 *   PicoContainer, Spring, ... (but kept for compatibility)
 * </p>
 *
 * @author Stephan J. Schmidt
 * @version $Id: EngineManager.java,v 1.15 2004/05/26 08:56:20 stephan Exp $
 */
public class EngineManager
{
    private static final Log LOG = LogFactory.getLog(EngineManager.class);
    private static final Map<String, RenderEngine> AVAILABLE_ENGINES = new HashMap<>();

    private EngineManager()
    {
        // utility class
    }

    static
    {
        final Iterator<RenderEngine> iterator = Service.providers(RenderEngine.class);
        while(iterator.hasNext())
        {
            try
            {
                final RenderEngine engine = iterator.next();
                registerEngine(engine);
                LOG.debug(
                    "Loaded RenderEngine: " + engine.getClass().getName());
            }
            catch(final Exception e)
            {
                LOG.warn("EngineManager: unable to load RenderEngine", e);
            }
        }
    }

    /**
     * Different RenderEngines can register themselves with the EngineManager
     * factory to be available with EngineManager.getInstance();
     *
     * @param engine RenderEngine instance, e.g. SnipRenderEngine
     */
    public static synchronized void registerEngine(final RenderEngine engine)
    {
        AVAILABLE_ENGINES.put(engine.getName(), engine);
    }

    /**
     * Get an instance of a RenderEngine. This is a factory method.
     *
     * @param name Name of the RenderEngine to get
     * @return engine RenderEngine for the requested name
     */
    public static synchronized RenderEngine getInstance(final String name)
    {
        return AVAILABLE_ENGINES.get(name);
    }

    /**
     * Get an instance of a RenderEngine. This is a factory method. Defaults to
     * a default RenderEngine. Currently this is a basic EngineManager with no
     * additional features that is distributed with Radeox.
     *
     * @return engine default RenderEngine
     */
    public static synchronized RenderEngine getInstance()
    {
        return AVAILABLE_ENGINES.computeIfAbsent(BaseRenderEngine.NAME,
            f -> new BaseRenderEngine());
    }

    public static String getVersion()
    {
        return "0.5.1";
    }

}
