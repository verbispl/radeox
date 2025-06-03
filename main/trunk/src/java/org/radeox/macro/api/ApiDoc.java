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

package org.radeox.macro.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Stores information and links to api documentation, e.g. for Java, Ruby, JBoss
 *
 * @author Stephan J. Schmidt
 * @version $Id: ApiDoc.java,v 1.6 2003/06/11 10:04:27 stephan Exp $
 */
public class ApiDoc
{
    private static final Log LOG = LogFactory.getLog(ApiDoc.class);

    private static ApiDoc instance;
    private final Map<String, ApiConverter> apiDocs;

    public static synchronized ApiDoc getInstance()
    {
        if(null == instance)
        {
            instance = new ApiDoc();
        }
        return instance;
    }

    public ApiDoc()
    {
        apiDocs = new HashMap<>();
        try
        {
            final BufferedReader br = new BufferedReader(
                new InputStreamReader(ApiDoc.class.getClassLoader()
                    .getResourceAsStream("apidocs.txt")));
            addApiDoc(br);
        }
        catch(final IOException e)
        {
            LOG.warn("Unable to read apidocs.txt");
        }

    }

    public void addApiDoc(final BufferedReader reader) throws IOException
    {
        String line;
        while((line = reader.readLine()) != null)
        {
            final StringTokenizer tokenizer = new StringTokenizer(line, " ");
            final String mode = tokenizer.nextToken();
            final String baseUrl = tokenizer.nextToken();
            final String converterName = tokenizer.nextToken();
            ApiConverter converter = null;
            try
            {
                converter = (ApiConverter) Class.forName(
                    "org.radeox.macro.api." + converterName + "ApiConverter")
                    .newInstance();
            }
            catch(final Exception e)
            {
                LOG.warn("Unable to load converter: " + converterName +
                    "ApiConverter", e);
            }
            converter.setBaseUrl(baseUrl);
            apiDocs.put(mode.toLowerCase(), converter);
        }
    }

    public boolean contains(final String external)
    {
        return apiDocs.containsKey(external);
    }

    public Writer expand(final Writer writer, final String className,
        String mode) throws IOException
    {
        mode = mode.toLowerCase();
        if(apiDocs.containsKey(mode))
        {
            writer.write("<a href=\"");
            apiDocs.get(mode).appendUrl(writer, className);
            writer.write("\">");
            writer.write(className);
            writer.write("</a>");
        }
        else
        {
            LOG.warn(mode + " not found");
        }
        return writer;
    }

    public Writer appendTo(final Writer writer) throws IOException
    {
        writer.write("{table}\n");
        writer.write("Binding|BaseUrl|Converter Name\n");
        for(final Entry<String, ApiConverter> entry : apiDocs.entrySet())
        {
            writer.write(entry.getKey());
            final ApiConverter converter = entry.getValue();
            writer.write("|");
            writer.write(converter.getBaseUrl());
            writer.write("|");
            writer.write(converter.getName());
            writer.write("\n");
        }
        writer.write("{table}");
        return writer;
    }

}
