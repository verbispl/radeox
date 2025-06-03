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

package org.radeox.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

/**
 * Interactive example how to use EngineManager.
 *
 * @author Stephan J. Schmidt
 * @version $Id: InteractiveExample.java,v 1.8 2004/05/26 08:56:20 stephan Exp $
 */
public class InteractiveExample
{
    private static DateFormat perfFormat = new SimpleDateFormat("m'm's's'S'ms'");

    public static void main(final String[] args)
    {
        System.err.println("Radeox");
        System.err.println("Copyright 2001-2004 Fraunhofer FIRST.");
        System.err.println("See License Agreement for terms and conditions of use.");

        final RenderEngine engine = new BaseRenderEngine();
        final RenderContext context = new BaseRenderContext();

        if(args.length > 0)
        {
            final File inputFile = new File(args[0]);
            if(inputFile.exists())
            {
                batch(engine, context, inputFile);
            }
            else
            {
                System.err.println("The file '" + args[0] + "' does not exist.");
            }
        }
        else
        {
            interactive(engine, context);
        }

    }

    private static void batch(final RenderEngine engine,
        final RenderContext context, final File inputFile)
    {
        final StringBuilder input = new StringBuilder();
        try(final BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
        {
            final char buffer[] = new char[4096];
            int length = 0;
            while((length = reader.read(buffer)) != -1)
            {
                input.append(buffer, 0, length);
            }
        }
        catch(final IOException e)
        {
            e.printStackTrace();
            return;
        }
        render(engine, context, input.toString());
    }

    private static void interactive(final RenderEngine engine,
        final RenderContext context)
    {
        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String line;
        try
        {
            System.out.print("> ");
            System.out.flush();
            while((line = reader.readLine()) != null)
            {
                render(engine, context, line);
                System.out.print("> ");
                System.out.flush();
            }
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void render(final RenderEngine engine,
        final RenderContext context, final String input)
    {
        final long start = System.currentTimeMillis();
        final String result = engine.render(input, context);
        final long length = System.currentTimeMillis() - start;
        System.out.println(result);
        System.out.println("rendered in " + perfFormat.format(new Date(length)));
    }

}
