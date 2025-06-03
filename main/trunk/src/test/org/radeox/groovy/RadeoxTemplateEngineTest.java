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

package org.radeox.groovy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.radeox.example.RadeoxTemplateEngine;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import junit.framework.TestCase;

public class RadeoxTemplateEngineTest extends TestCase
{

    public RadeoxTemplateEngineTest(final String name)
    {
        super(name);
    }

    public void testRadeoxTemplate() throws CompilationFailedException,
        FileNotFoundException, ClassNotFoundException, IOException
    {
        final String text = "__Dear__ ${firstname}";

        final Map<String, String> binding = new HashMap<>();
        binding.put("firstname", "stephan");

        final TemplateEngine engine = new RadeoxTemplateEngine();
        final Template template = engine.createTemplate(text);
        final Writable make = template.make(binding);

        final String result = "<b class=\"bold\">Dear</b> stephan";
        assertEquals(result, make.toString());
    }

}
