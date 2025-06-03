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

package org.radeox.util.i18n;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Resource Manager implementation to aid the use of locales. It works similar
 * to what the bundle tag in jstl fmt taglibs does but adapted for use withing a
 * threaded java environment.
 *
 * @author Matthias L. Jugel
 * @version $Id: ResourceManager.java,v 1.5 2004/06/08 07:54:37 leo Exp $
 */
public class ResourceManager
{
    private static final Log LOG = LogFactory.getLog(ResourceManager.class);
    private static final List<Locale> FALLBACK = Collections.singletonList(new Locale("en_US"));

    private final Locale locale;
    private final ConcurrentHashMap<String, BaseResourceBundle> resourceBundles;

    public ResourceManager(final Locale locale)
    {
        this.locale = locale;
        this.resourceBundles = new ConcurrentHashMap<>();
    }

    /**
     * Get the bundle that is active for this thread. This is done by loading
     * either the specified locale based resource bundle and, if that fails, by
     * looping through the fallback locales to locate a usable bundle.
     *
     * @return the resource bundle
     */
    public BaseResourceBundle getResourceBundle(final String baseName)
    {
        return resourceBundles.computeIfAbsent(baseName, this::findBundle);
    }

    /**
     * Find a resource bundle by looking up using the locales. This is done by
     * loading either the specified locale based resource bundle and, if that
     * fails, by looping through the fallback locales to locate a usable bundle.
     */
    private BaseResourceBundle findBundle(final String baseName)
    {
        ResourceBundle resourceBundle = null;

        // first try to load the resource bundle with the specified locale
        final ClassLoader cl = getClass().getClassLoader();
        try
        {
            resourceBundle = ResourceBundle.getBundle(baseName, locale, cl);
            // check that the requested main locale matches the resource bundle's,
            // since we get the system fallback locale if no match is found
            if(!resourceBundle.getLocale().equals(locale))
            {
                LOG.warn("loaded bundle locale differs requested locale. Requestet locale: " +
                    locale + ", bundle locale: " + resourceBundle.getLocale());
            }
        }
        catch(final Exception e)
        {
            LOG.warn("unable to load a default bundle: " + baseName + "_" + locale, e);
        }

        // loop through the fall back locales until a bundle is found
        if(resourceBundle == null)
        {
            resourceBundle = getFallbackBundle(baseName, cl);

            // make sure the resource bundle is loaded (should not happen)
            if(resourceBundle == null)
            {
                resourceBundle = ResourceBundle.getBundle(baseName);
                if(null != resourceBundle)
                {
                    LOG.debug("system locale bundle taken: " + baseName + "_" +
                        resourceBundle.getLocale());
                }
            }
        }
        return new BaseResourceBundle(baseName, resourceBundle);
    }

    private ResourceBundle getFallbackBundle(final String baseName, final ClassLoader cl)
    {
        ResourceBundle resourceBundle = null;
        for(final Locale testLocale : FALLBACK)
        {
            LOG.debug("looking up locale " + testLocale);
            try
            {
                final ResourceBundle testBundle = ResourceBundle.getBundle(baseName, testLocale, cl);
                final Locale foundLocale = testBundle.getLocale();
                final String language = foundLocale.getLanguage();
                final String country = foundLocale.getCountry();

                if(foundLocale.equals(testLocale))
                {
                    resourceBundle = testBundle;
                    LOG.debug("found bundle for locale " + baseName + "_" + foundLocale);
                    break;
                }
                else if(testLocale.getLanguage().equals(language))
                {
                    if(testLocale.getCountry().equals(country))
                    {
                        // language and country match which is good, keep
                        // looking for variant too
                        resourceBundle = testBundle;
                        LOG.debug("potential bundle: " + baseName + "_" + foundLocale);
                    }
                    else
                    {
                        // only accept this if there is no better previous lookup
                        if(resourceBundle == null)
                        {
                            resourceBundle = testBundle;
                            LOG.debug("potential bundle: " + baseName + "_" + foundLocale);
                        }
                    }
                }
            }
            catch(final MissingResourceException e)
            {
                LOG.debug("not found bundle for locale " + baseName + "_" + testLocale);
            }
        }
        return resourceBundle;
    }

}
