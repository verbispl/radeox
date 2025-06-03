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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.context.InitialRenderContext;

/**
 * Repository for plugins
 *
 * @author Stephan J. Schmidt
 * @version $Id: MacroRepository.java,v 1.9 2003/12/17 13:35:36 stephan Exp $
 */
public class MacroRepository extends PluginRepository<Macro>
{
    private static final Log LOG = LogFactory.getLog(MacroRepository.class);

    protected static MacroRepository instance;
    protected List<MacroLoader> loaders;

    public static synchronized MacroRepository getInstance()
    {
        if(null == instance)
        {
            instance = new MacroRepository();
        }
        return instance;
    }

    private void initialize(final InitialRenderContext context)
    {
        final Iterator<Macro> iterator = list.iterator();
        while(iterator.hasNext())
        {
            final Macro macro = iterator.next();
            macro.setInitialContext(context);
        }
        init();
    }

    public void setInitialContext(final InitialRenderContext context)
    {
        initialize(context);
    }

    private void init()
    {
        final Map<String, Macro> newPlugins = new HashMap<>();

        final Iterator<Macro> iterator = list.iterator();
        while(iterator.hasNext())
        {
            final Macro macro = iterator.next();
            newPlugins.put(macro.getName(), macro);
        }
        plugins = newPlugins;
    }

    /**
     * Loads macros from all loaders into plugins.
     */
    private void load()
    {
        final Iterator<MacroLoader> iterator = loaders.iterator();
        while(iterator.hasNext())
        {
            final MacroLoader loader = iterator.next();
            loader.setRepository(this);
            LOG.debug("Loading from: " + loader.getClass());
            loader.loadPlugins(this);
        }
    }

    public void addLoader(final MacroLoader loader)
    {
        loader.setRepository(this);
        loaders.add(loader);
        plugins = new HashMap<>();
        list = new ArrayList<>();
        load();
    }

    private MacroRepository()
    {
        loaders = new ArrayList<>();
        loaders.add(new MacroLoader());
        load();
    }

}
