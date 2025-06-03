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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.filter.context.FilterContext;

/**
 * FilterPipe is a collection of Filters which are applied one by one to an
 * input to generate output.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: FilterPipe.java,v 1.21 2004/06/21 13:08:43 leo Exp $
 */
public class FilterPipe
{
    private static Log log = LogFactory.getLog(FilterPipe.class);

    public static final String FIRST_IN_PIPE = "all";
    public static final String LAST_IN_PIPE = "none";
    public static final String[] EMPTY_BEFORE = new String[] {};
    public static final String[] NO_REPLACES = new String[] {};
    public static final String[] FIRST_BEFORE = new String[] {FIRST_IN_PIPE};

    private final InitialRenderContext initialContext;

    private List<Filter> activeFilters = null;
    private Set<Filter> inactiveFilters = null;

    public FilterPipe()
    {
        this(InitialRenderContext.defaultContext());
    }

    public FilterPipe(final InitialRenderContext context)
    {
        activeFilters = new ArrayList<>();
        inactiveFilters = new HashSet<>();
        initialContext = context;
    }

    public void init()
    {
        // loop over copy of activeFilters becouse of #removeFilter()
        for(final Filter filter : new ArrayList<>(activeFilters))
        {
            final String[] replaces = filter.replaces();
            for(int i = 0; i < replaces.length; i++)
            {
                final String replace = replaces[i];
                removeFilter(replace);
            }
        }
    }

    public void removeFilter(final String filterClass)
    {
        final Iterator<Filter> iterator = activeFilters.iterator();
        while(iterator.hasNext())
        {
            final Filter filter = iterator.next();
            if(filter.getClass().getName().equals(filterClass))
            {
                iterator.remove();
            }
        }
    }

    public void activateFilter(final String name)
    {
        synchronized(inactiveFilters)
        {
            final Iterator<Filter> iterator = inactiveFilters.iterator();
            while(iterator.hasNext())
            {
                final Filter filter = iterator.next();
                if(filter.getClass().getName().startsWith(name))
                {
                    inactiveFilters.remove(filter);
                }
            }
        }
    }

    public void deactivateFilter(final String name)
    {
        final Iterator<Filter> iterator = activeFilters.iterator();
        while(iterator.hasNext())
        {
            final Filter filter = iterator.next();
            if(filter.getClass().getName().startsWith(name))
            {
                inactiveFilters.add(filter);
            }
        }
    }

    public List<Filter> getAllFilters()
    {
        return new ArrayList<>(activeFilters);
    }

    public List<Filter> getInactiveFilters()
    {
        return new ArrayList<>(inactiveFilters);
    }

    /**
     * Add a filter to the active pipe.
     *
     * @param filter filter to add
     */
    public void addFilter(final Filter filter)
    {
        filter.setInitialContext(initialContext);

        int minIndex = Integer.MAX_VALUE;
        final String[] before = filter.before();
        for(int i = 0; i < before.length; i++)
        {
            final String s = before[i];
            final int index = index(activeFilters, s);
            if(index < minIndex)
            {
                minIndex = index;
            }
        }
        if(minIndex == Integer.MAX_VALUE)
        {
            // -1 is more usable for not-found than MAX_VALUE
            minIndex = -1;
        }

        if(contains(filter.before(), FIRST_IN_PIPE))
        {
            activeFilters.add(0, filter);
        }
        else if(minIndex != -1)
        {
            activeFilters.add(minIndex, filter);
        }
        else
        {
            activeFilters.add(filter);
        }
    }

    public int index(final String filterName)
    {
        return FilterPipe.index(activeFilters, filterName);
    }

    public static int index(final List<?> list, final String filterName)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(filterName.equals(list.get(i).getClass().getName()))
            {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(final Object[] array, final Object value)
    {
        return (Arrays.binarySearch(array, value) != -1);
    }

    /**
     * Filter some input and generate ouput. FilterPipe pipes the string input
     * through every filter in the pipe and returns the resulting string.
     *
     * @param input Input string which should be transformed
     * @param context FilterContext with information about the enviroment
     * @return result Filtered output
     */
    public String filter(final String input, final FilterContext context)
    {
        String output = input;
        final Iterator<Filter> filterIterator = activeFilters.iterator();
        final RenderContext renderContext = context.getRenderContext();

        // Apply every filter in activeFilters to input string
        while(filterIterator.hasNext())
        {
            final Filter f = filterIterator.next();
            if(!inactiveFilters.contains(f))
            {
                try
                {
                    // assume all filters non cacheable
                    if(f instanceof CacheFilter)
                    {
                        renderContext.setCacheable(true);
                    }
                    else
                    {
                        renderContext.setCacheable(false);
                    }
                    final String tmp = f.filter(output, context);
                    if(output.equals(tmp))
                    {
                        renderContext.setCacheable(true);
                    }
                    if(null == tmp)
                    {
                        log.warn("FilterPipe.filter: error while filtering: " + f);
                    }
                    else
                    {
                        output = tmp;
                    }
                    renderContext.commitCache();
                }
                catch(final Exception e)
                {
                    log.warn("Filtering exception: " + f, e);
                }
            }
        }
        return output;
    }

    public Filter getFilter(final int index)
    {
        return activeFilters.get(index);
    }

}
