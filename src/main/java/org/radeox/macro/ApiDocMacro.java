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

import java.io.IOException;
import java.io.Writer;

import org.radeox.macro.api.ApiDoc;
import org.radeox.macro.parameter.MacroParameter;

/**
 * Lists all known API documentation repositories and mappings.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: ApiDocMacro.java,v 1.7 2004/04/27 19:30:38 leo Exp $
 */
public class ApiDocMacro extends BaseLocaleMacro
{
    @Override
    public String getLocaleKey()
    {
        return "macro.apidocs";
    }

    @Override
    public void execute(final Writer writer, final MacroParameter params)
        throws IllegalArgumentException, IOException
    {
        final ApiDoc apiDoc = ApiDoc.getInstance();
        apiDoc.appendTo(writer);
    }

}
