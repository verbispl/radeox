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

package org.radeox.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;

/**
 * Listfilter checks for lists in in its input. These are transformed to output
 * lists, e.g. in HTML.
 * <p>
 *   Recognices different lists like numbered lists, unnumbered lists, greek
 *   lists, alpha lists etc.
 * </p>
 *
 * @credits nested list support by Davor Cubranic
 * @author stephan
 * @team sonicteam
 * @version $Id: ListFilter.java,v 1.17 2004/04/15 13:56:14 stephan Exp $
 */
public class ListFilter extends LocaleRegexTokenFilter implements CacheFilter
{
    private static final Log LOG = LogFactory.getLog(ListFilter.class);

    private static final String NEWLINE = "\n";
    private static final String LI_OPEN = "<li>";
    private static final String LI_CLOSE = "</li>";
    private static final String UL_CLOSE = "</ul>";
    private static final String OL_CLOSE = "</ol>";

    private static final Map<Character,String> openList = new HashMap<>();
    private static final Map<Character,String> closeList = new HashMap<>();

    static
    {
        openList.put(Character.valueOf('-'), "<ul class=\"minus\">");
        openList.put(Character.valueOf('*'), "<ul class=\"star\">");
        openList.put(Character.valueOf('#'), "<ol>");
        openList.put(Character.valueOf('i'), "<ol class=\"roman\">");
        openList.put(Character.valueOf('I'), "<ol class=\"ROMAN\">");
        openList.put(Character.valueOf('a'), "<ol class=\"alpha\">");
        openList.put(Character.valueOf('A'), "<ol class=\"ALPHA\">");
        openList.put(Character.valueOf('g'), "<ol class=\"greek\">");
        openList.put(Character.valueOf('h'), "<ol class=\"hiragana\">");
        openList.put(Character.valueOf('H'), "<ol class=\"HIRAGANA\">");
        openList.put(Character.valueOf('k'), "<ol class=\"katakana\">");
        openList.put(Character.valueOf('K'), "<ol class=\"KATAKANA\">");
        openList.put(Character.valueOf('j'), "<ol class=\"HEBREW\">");
        openList.put(Character.valueOf('1'), "<ol>");
        closeList.put(Character.valueOf('-'), UL_CLOSE);
        closeList.put(Character.valueOf('*'), UL_CLOSE);
        closeList.put(Character.valueOf('#'), OL_CLOSE);
        closeList.put(Character.valueOf('i'), OL_CLOSE);
        closeList.put(Character.valueOf('I'), OL_CLOSE);
        closeList.put(Character.valueOf('a'), OL_CLOSE);
        closeList.put(Character.valueOf('A'), OL_CLOSE);
        closeList.put(Character.valueOf('1'), OL_CLOSE);
        closeList.put(Character.valueOf('g'), OL_CLOSE);
        closeList.put(Character.valueOf('G'), OL_CLOSE);
        closeList.put(Character.valueOf('h'), OL_CLOSE);
        closeList.put(Character.valueOf('H'), OL_CLOSE);
        closeList.put(Character.valueOf('k'), OL_CLOSE);
        closeList.put(Character.valueOf('K'), OL_CLOSE);
        closeList.put(Character.valueOf('j'), OL_CLOSE);
    }

    @Override
    protected String getLocaleKey()
    {
        return "filter.list";
    }

    @Override
    protected boolean isSingleLine()
    {
        return false;
    }

    @Override
    public void handleMatch(final StringBuffer buffer, final MatchResult result,
        final FilterContext context)
    {
        try
        {
            final BufferedReader reader = new BufferedReader(new StringReader(result.group(0)));
            addList(buffer, reader);
        }
        catch(final Exception e)
        {
            LOG.warn("ListFilter: unable get list content", e);
        }
    }

    /**
     * Adds a list to a buffer
     *
     * @param buffer The buffer to write to
     * @param reader Input is read from this Reader
     */
    private void addList(final StringBuffer buffer, final BufferedReader reader)
        throws IOException
    {
        char[] lastBullet = new char[0];
        String line = null;
        while((line = reader.readLine()) != null)
        {
            // no nested list handling, trim lines:
            line = line.trim();
            if(line.length() == 0)
            {
                continue;
            }

            int bulletEnd = line.indexOf(' ');
            if(bulletEnd < 1)
            {
                continue;
            }
            if(line.charAt(bulletEnd - 1) == '.')
            {
                bulletEnd--;
            }
            final char[] bullet = line.substring(0, bulletEnd).toCharArray();
            // check whether we find a new list
            int sharedPrefixEnd;
            for(sharedPrefixEnd = 0;; sharedPrefixEnd++)
            {
                if(bullet.length <= sharedPrefixEnd ||
                    lastBullet.length <= sharedPrefixEnd ||
                    +bullet[sharedPrefixEnd] != lastBullet[sharedPrefixEnd])
                {
                    break;
                }
            }

            for(int i = sharedPrefixEnd; i < lastBullet.length; i++)
            {
                buffer.append(LI_CLOSE + NEWLINE);
                buffer.append(closeList.get(Character.valueOf(lastBullet[i])))
                    .append("\n");
            }

            for(int i = sharedPrefixEnd; i < bullet.length; i++)
            {
                if(i > 0)
                {
                    buffer.append(NEWLINE);
                }
                buffer.append(openList.get(Character.valueOf(bullet[i])))
                    .append("\n");
                buffer.append(LI_OPEN);
            }

            if(lastBullet.length >= bullet.length)
            {
                buffer.append(LI_CLOSE + NEWLINE);
                buffer.append(LI_OPEN);
            }

            buffer.append(line.substring(line.indexOf(' ') + 1));
            lastBullet = bullet;
        }

        for(int i = lastBullet.length - 1; i >= 0; i--)
        {
            buffer.append(LI_CLOSE + NEWLINE);
            buffer.append(closeList.get(Character.valueOf(lastBullet[i])));
            if(i > 0)
            {
                buffer.append(NEWLINE);
            }
        }
    }

}
