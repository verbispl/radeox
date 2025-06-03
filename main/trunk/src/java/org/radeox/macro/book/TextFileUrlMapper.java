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

package org.radeox.macro.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.util.Encoder;

/**
 * Manages links to keys, mapping is read from a text file.
 *
 * @author Stephan J. Schmidt
 * @version $Id: TextFileUrlMapper.java,v 1.6 2003/06/11 10:04:27 stephan Exp $
 */
public abstract class TextFileUrlMapper implements UrlMapper
{
    private static Log log = LogFactory.getLog(TextFileUrlMapper.class);

    private final Map<String, String> services;

    public abstract String getFileName();

    public abstract String getKeyName();

    protected TextFileUrlMapper()
    {
        services = new HashMap<>();

        try
        {
            final BufferedReader br = new BufferedReader(
                new InputStreamReader(TextFileUrlMapper.class.getClassLoader()
                    .getResourceAsStream(getFileName())));
            addMapping(br);
        }
        catch(final IOException e)
        {
            log.warn("Unable to read " + getFileName());
        }

    }

    public void addMapping(final BufferedReader reader) throws IOException
    {
        String line;
        while((line = reader.readLine()) != null)
        {
            if(!line.startsWith("#"))
            {
                final int index = line.indexOf(" ");
                services.put(line.substring(0, index),
                    Encoder.escape(line.substring(index + 1)));
            }
        }
    }

    @Override
    public Writer appendTo(final Writer writer) throws IOException
    {
        final Iterator<Entry<String, String>> iterator = services.entrySet().iterator();
        writer.write("{table}\n");
        writer.write("Service|Url\n");
        while(iterator.hasNext())
        {
            final Entry<String, String> entry = iterator.next();
            writer.write(entry.getKey());
            writer.write("|");
            writer.write(entry.getValue());
            writer.write("\n");
        }
        writer.write("{table}");
        return writer;
    }

    public boolean contains(final String external)
    {
        return services.containsKey(external);
    }

    @Override
    public Writer appendUrl(final Writer writer, final String key)
        throws IOException
    {
        if(services.size() == 0)
        {
            writer.write(getKeyName());
            writer.write(":");
            writer.write(key);
        }
        else
        {
            // SnipLink.appendImage(writer, "external-link", "&gt;&gt;");
            writer.write("(");
            final Iterator<Entry<String, String>> iterator = services.entrySet()
                .iterator();
            while(iterator.hasNext())
            {
                final Entry<String, String> entry = iterator.next();
                writer.write("<a href=\"");
                writer.write(entry.getValue());
                writer.write(key);
                writer.write("\">");
                writer.write(entry.getKey());
                writer.write("</a>");
                if(iterator.hasNext())
                {
                    writer.write(" &#x7c; ");
                }
            }
            writer.write(")");
        }
        return writer;
    }

}
