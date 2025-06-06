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

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.filter.context.FilterContext;

/**
 * Filter interface. Concrete Filters should implement Filter. Filters transform
 * a String (usually snip content) to another String (usually HTML).
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: Filter.java,v 1.10 2003/10/07 08:20:24 stephan Exp $
 */
public interface Filter
{
    public String filter(String input, FilterContext context);

    /**
     * Returns classes which this filter should be run insted.
     *
     * @return names of the classes ({@link java.lang.Class#getName()})
     */
    public String[] replaces();

    /**
     * Returns array of filter class names which this filter shoud precede.
     *
     * @return array of class names or special arrays
     * @see FilterPipe#FIRST_IN_PIPE
     * @see FilterPipe#EMPTY_BEFORE
     * @see FilterPipe#NO_REPLACES
     */
    public String[] before();

    public void setInitialContext(InitialRenderContext context);

    public String getDescription();

}
