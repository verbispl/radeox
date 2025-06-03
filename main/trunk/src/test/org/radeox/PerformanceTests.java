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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.radeox.api.engine.RenderEngine;
import org.radeox.engine.BaseRenderEngine;
import org.radeox.engine.context.BaseRenderContext;

import com.clarkware.junitperf.TimedTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PerformanceTests
{
    public static Test suite() throws IOException
    {
        // get test markup from text file
        final InputStream wiki = PerformanceTests.class.getClassLoader()
            .getResourceAsStream("wiki.txt");
        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(wiki));
        final StringBuilder input = new StringBuilder();
        String tmp;
        while((tmp = reader.readLine()) != null)
        {
            input.append(tmp);
        }
        final RenderEngine engine = new BaseRenderEngine();
        System.err
            .println(engine.render("__initialized__", new BaseRenderContext()));

        final TestSuite s = new TestSuite();
        final long maxElapsedTime = 30 * 1000; // 30s
        final StringBuilder testString = new StringBuilder();
        for(int i = 0; i < 10; i++)
        {
            testString.append(input);
            final Test renderEngineTest = new RenderEnginePerformanceTest(
                testString.toString());
            final Test timedTest = new TimedTest(renderEngineTest,
                maxElapsedTime, false);
            s.addTest(timedTest);
        }
        return s;
    }

}
