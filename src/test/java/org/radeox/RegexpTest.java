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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

public class RegexpTest
{
    public static void main(final String[] args)
    {
        final String file = args.length > 0 ? args[0] : "conf/wiki.txt";
        try
        {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        }
        catch(final UnsupportedEncodingException e)
        {
            // this should never happen
        }

        final StringBuilder tmp = new StringBuilder();
        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
        {
            final char[] buffer = new char[1024];
            int n = 0;
            while((n = reader.read(buffer)) != -1)
            {
                tmp.append(buffer, 0, n);
            }
        }
        catch(final Exception e)
        {
            System.err.println("File not found: " + e.getMessage());
        }

        final String content = tmp.toString();

        try
        {
            final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(System.out, "UTF-8"));
            out.println(content);
            out.flush();

            final RenderContext context = new BaseRenderContext();
            final RenderEngine engine = new BaseRenderEngine();

            final long now = System.currentTimeMillis();
            final String output = engine.render(content, context);
            final long dif = System.currentTimeMillis() - now;

            System.err.println("time spent: " + dif + "ms");
            out.println(output);
            out.flush();
            out.close();
        }
        catch(final UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

}
