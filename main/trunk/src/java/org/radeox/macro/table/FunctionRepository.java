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

package org.radeox.macro.table;

import java.util.ArrayList;
import java.util.List;

import org.radeox.macro.PluginRepository;
import org.radeox.macro.Repository;

/**
 * Repository for functions.
 *
 * @author Stephan J. Schmidt
 * @version $Id: FunctionRepository.java,v 1.2 2003/05/23 10:47:25 stephan Exp $
 */
public class FunctionRepository extends PluginRepository<Function>
{
    protected static Repository<Function> instance;
    protected List<FunctionLoader> loaders;

    public static synchronized Repository<Function> getInstance()
    {
        if(null == instance)
        {
            instance = new FunctionRepository();
        }
        return instance;
    }

    private void load()
    {
        for(final FunctionLoader loader : loaders)
        {
            loader.loadPlugins(this);
        }
    }

    private FunctionRepository()
    {
        loaders = new ArrayList<>();
        loaders.add(new FunctionLoader());

        load();
    }

}
