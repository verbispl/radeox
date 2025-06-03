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

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.FilterPipe;
import org.radeox.util.i18n.BaseResourceBundle;
import org.radeox.util.i18n.ResourceManager;

/**
 * Base impementation for InitialRenderContext.
 *
 * @author Stephan J. Schmidt
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id: BaseInitialRenderContext.java,v 1.6 2004/04/27 19:30:38 leo Exp $
 */
public class BaseInitialRenderContext extends BaseRenderContext implements InitialRenderContext
{
    private final ConcurrentHashMap<Locale, ResourceManager> resourceManagers;

    private final String languageBuldleName;
    private final Locale inputLocale;
    private final Locale outputLocale;
    private final String inputBundleName;
    private final String outputBundleName;

    private FilterPipe fp;

    BaseInitialRenderContext(final String languageBuldleName,
        final Locale inputLocale, final Locale outputLocale,
        final String inputBundleName, final String outputBundleName)
    {
        this.resourceManagers = new ConcurrentHashMap<>();
        this.languageBuldleName = languageBuldleName;
        this.inputLocale = inputLocale;
        this.outputLocale = outputLocale;
        this.inputBundleName = inputBundleName;
        this.outputBundleName = outputBundleName;
    }

    @Override
    public void setFilterPipe(final FilterPipe fp)
    {
        this.fp = fp;
    }

    @Override
    public FilterPipe getFilterPipe()
    {
        return fp;
    }

    @Override
    public String getLanguageBuldleName()
    {
        return languageBuldleName;
    }

    @Override
    public Locale getInputLocale()
    {
        return inputLocale;
    }

    @Override
    public Locale getOutputLocale()
    {
        return outputLocale;
    }

    @Override
    public String getInputBundleName()
    {
        return inputBundleName;
    }

    @Override
    public String getOutputBundleName()
    {
        return outputBundleName;
    }

    /**
     * Get a new thread-local instance of the ResourceManager If you are having
     * problems with bundles beeing the same for different threads and locales,
     * try forceGet()
     *
     * @return the thread-local ResourceManager
     */
    private ResourceManager get(final Locale locale)
    {
        return resourceManagers.computeIfAbsent(locale, ResourceManager::new);
    }

    /**
     * Get ResourceBundle using the base name provided. The bundle is located
     * using previously given locale settings.
     *
     * @param baseName the bundle base name
     * @return the bundle
     */
    @Override
    public BaseResourceBundle getBundle(final Locale locale, final String baseName)
    {
        return get(locale).getResourceBundle(baseName);
    }

}
