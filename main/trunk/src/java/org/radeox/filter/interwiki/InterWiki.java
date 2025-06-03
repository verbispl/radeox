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

package org.radeox.filter.interwiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
 * Stores information and links to other wikis forming a InterWiki.
 *
 * @author Stephan J. Schmidt
 * @version $Id: InterWiki.java,v 1.6 2004/01/19 11:45:24 stephan Exp $
 */

public class InterWiki
{
    private static final Log LOG = LogFactory.getLog(InterWiki.class);

    private static InterWiki instance;
    private Map<String, String> interWiki;

    public static synchronized InterWiki getInstance()
    {
        if(null == instance)
        {
            instance = new InterWiki();
        }
        return instance;
    }

    public InterWiki(final InputStream in)
    {
        try
        {
            init(in);
        }
        catch(final IOException e)
        {
            LOG.warn("Unable to initialize from stream.", e);
        }
    }

    public InterWiki()
    {
        try
        {
            init(InterWiki.class.getClassLoader().getResourceAsStream("intermap.txt"));
        }
        catch(final IOException e)
        {
            LOG.warn("Unable to read intermap.txt", e);
        }
    }

    public void init(final InputStream in) throws IOException
    {
        interWiki = new HashMap<>();
        interWiki.put("LCOM", "http://www.langreiter.com/space/");
        interWiki.put("ESA", "http://earl.strain.at/space/");
        interWiki.put("C2", "http://www.c2.com/cgi/wiki?");
        interWiki.put("WeblogKitchen",
            "http://www.weblogkitchen.com/wiki.cgi?");
        interWiki.put("Meatball", "http://www.usemod.com/cgi-bin/mb.pl?");
        interWiki.put("SnipSnap", "http://snipsnap.org/space/");

        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(in));
        String line;
        while((line = reader.readLine()) != null)
        {
            final int index = line.indexOf(" ");
            interWiki.put(line.substring(0, index),
                Encoder.escape(line.substring(index + 1)));
        }
    }

    public Writer appendTo(final Writer writer) throws IOException
    {
        final Iterator<Entry<String, String>> iterator = interWiki.entrySet()
            .iterator();
        writer.write("{table}\n");
        writer.write("Wiki|Url\n");
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
        return interWiki.containsKey(external);
    }

    public String getWikiUrl(final String wiki, final String name)
    {
        return (interWiki.get(wiki)) + name;
    }

    public Writer expand(final Writer writer, final String wiki,
        final String name, final String view, final String anchor)
        throws IOException
    {
        writer.write("<a href=\"");
        writer.write(interWiki.get(wiki));
        writer.write(name);
        if(!"".equals(anchor))
        {
            writer.write("#");
            writer.write(anchor);
        }
        writer.write("\">");
        writer.write(view);
        writer.write("</a>");
        return writer;
    }

    public Writer expand(final Writer writer, final String wiki,
        final String name, final String view) throws IOException
    {
        return expand(writer, wiki, name, view, "");
    }

}
