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

package org.radeox.macro.xref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Stores information and links to xref Java source code e.g.
 * http://nanning.sourceforge.net/xref/com/tirsen/nanning/MixinInstance.html#83
 *
 * @author Stephan J. Schmidt
 * @version $Id: XrefMapper.java,v 1.3 2003/06/11 10:04:27 stephan Exp $
 */
public class XrefMapper
{
    private static final Log LOG = LogFactory.getLog(XrefMapper.class);

    private static XrefMapper instance;
    private final Map<String, String> xrefMap;

    public static synchronized XrefMapper getInstance()
    {
        if(null == instance)
        {
            instance = new XrefMapper();
        }
        return instance;
    }

    public XrefMapper()
    {
        xrefMap = new HashMap<>();
        try
        {
            final BufferedReader br = new BufferedReader(
                new InputStreamReader(XrefMapper.class.getClassLoader()
                    .getResourceAsStream("xref.txt")));
            addXref(br);
        }
        catch(final IOException e)
        {
            LOG.warn("Unable to read xref.txt");
        }
    }

    public void addXref(final BufferedReader reader) throws IOException
    {
        String line;
        while((line = reader.readLine()) != null)
        {
            final StringTokenizer tokenizer = new StringTokenizer(line, " ");
            final String site = tokenizer.nextToken();
            final String baseUrl = tokenizer.nextToken();
            xrefMap.put(site.toLowerCase(), baseUrl);
        }
    }

    public boolean contains(final String external)
    {
        return xrefMap.containsKey(external);
    }

    public Writer expand(final Writer writer, final String className,
        String site, final int lineNumber) throws IOException
    {
        site = site.toLowerCase();
        if(xrefMap.containsKey(site))
        {
            writer.write("<a href=\"");
            writer.write(xrefMap.get(site));
            writer.write("/");
            writer.write(className.replace('.', '/'));
            writer.write(".html");
            if(lineNumber > 0)
            {
                writer.write("#");
                writer.write("" + lineNumber);
            }
            writer.write("\">");
            writer.write(className);
            writer.write("</a>");
        }
        else
        {
            LOG.debug("Xrefs : " + xrefMap);
            LOG.warn(site + " not found");
        }
        return writer;
    }

    public Writer appendTo(final Writer writer) throws IOException
    {
        writer.write("{table}\n");
        writer.write("Binding|Site\n");
        final Iterator<Entry<String, String>> iterator = xrefMap.entrySet()
            .iterator();
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

}
