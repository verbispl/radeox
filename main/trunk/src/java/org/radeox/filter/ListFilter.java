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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
 * Listfilter checks for lists in in its input. These are
 * transformed to output lists, e.g. in HTML. Recognices
 * different lists like numbered lists, unnumbered lists,
 * greek lists, alpha lists etc.
 *
 * @credits nested list support by Davor Cubranic
 * @author stephan
 * @team sonicteam
 * @version $Id: ListFilter.java,v 1.17 2004/04/15 13:56:14 stephan Exp $
 */

public class ListFilter extends LocaleRegexTokenFilter implements CacheFilter {
  private static Log log = LogFactory.getLog(ListFilter.class);

  private final static Map openList = new HashMap();
  private final static Map closeList = new HashMap();

  private static final String NEWLINE = "\n";
  private static final String LI_OPEN = "<li>";
  private static final String LI_CLOSE = "</li>";
  private static final String UL_CLOSE = "</ul>";
  private static final String OL_CLOSE = "</ol>";

  protected String getLocaleKey() {
    return "filter.list";
  }

  protected boolean isSingleLine() {
    return false;
  }

  public ListFilter() {
    super();
    openList.put(new Character('-'), "<ul class=\"minus\">");
    openList.put(new Character('*'), "<ul class=\"star\">");
    openList.put(new Character('#'), "<ol>");
    openList.put(new Character('i'), "<ol class=\"roman\">");
    openList.put(new Character('I'), "<ol class=\"ROMAN\">");
    openList.put(new Character('a'), "<ol class=\"alpha\">");
    openList.put(new Character('A'), "<ol class=\"ALPHA\">");
    openList.put(new Character('g'), "<ol class=\"greek\">");
    openList.put(new Character('h'), "<ol class=\"hiragana\">");
    openList.put(new Character('H'), "<ol class=\"HIRAGANA\">");
    openList.put(new Character('k'), "<ol class=\"katakana\">");
    openList.put(new Character('K'), "<ol class=\"KATAKANA\">");
    openList.put(new Character('j'), "<ol class=\"HEBREW\">");
    openList.put(new Character('1'), "<ol>");
    closeList.put(new Character('-'), UL_CLOSE);
    closeList.put(new Character('*'), UL_CLOSE);
    closeList.put(new Character('#'), OL_CLOSE);
    closeList.put(new Character('i'), OL_CLOSE);
    closeList.put(new Character('I'), OL_CLOSE);
    closeList.put(new Character('a'), OL_CLOSE);
    closeList.put(new Character('A'), OL_CLOSE);
    closeList.put(new Character('1'), OL_CLOSE);
    closeList.put(new Character('g'), OL_CLOSE);
    closeList.put(new Character('G'), OL_CLOSE);
    closeList.put(new Character('h'), OL_CLOSE);
    closeList.put(new Character('H'), OL_CLOSE);
    closeList.put(new Character('k'), OL_CLOSE);
    closeList.put(new Character('K'), OL_CLOSE);
    closeList.put(new Character('j'), OL_CLOSE);
  };

  public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
    try {
      BufferedReader reader = new BufferedReader(new StringReader(result.group(0)));
      //System.err.println("match="+result.group(0));
      addList(buffer, reader);
    } catch (Exception e) {
      log.warn("ListFilter: unable get list content", e);
    }
  }

  /**
   * Adds a list to a buffer
   *
   * @param buffer The buffer to write to
   * @param reader Input is read from this Reader
   */
  private void addList(StringBuffer buffer, BufferedReader reader) throws IOException {
    char[] lastBullet = new char[0];
    String line = null;
    while ((line = reader.readLine()) != null) {
      // no nested list handling, trim lines:
      line = line.trim();
      if (line.length() == 0) {
        continue;
      }

      int bulletEnd = line.indexOf(' ');
      if (bulletEnd < 1) {
        continue;
      }
      if ( line.charAt(bulletEnd - 1) == '.') {
        bulletEnd--;
      }
      char[] bullet = line.substring(0, bulletEnd).toCharArray();
      // Logger.log("found bullet: ('" + new String(lastBullet) + "') '" + new String(bullet) + "'");
      // check whether we find a new list
      int sharedPrefixEnd;
      for (sharedPrefixEnd = 0; ; sharedPrefixEnd++) {
        if (bullet.length <= sharedPrefixEnd || lastBullet.length <= sharedPrefixEnd ||
          +bullet[sharedPrefixEnd] != lastBullet[sharedPrefixEnd]) {
          break;
        }
      }

      for (int i = sharedPrefixEnd; i < lastBullet.length; i++) {
        //Logger.log("closing " + lastBullet[i]);
        buffer.append(LI_CLOSE + NEWLINE);
        buffer.append(closeList.get(new Character(lastBullet[i]))).append("\n");
      }

      for (int i = sharedPrefixEnd; i < bullet.length; i++) {
        //Logger.log("opening " + bullet[i]);
        if(i > 0)
        {
          buffer.append(NEWLINE);
        }
        buffer.append(openList.get(new Character(bullet[i]))).append("\n");
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

    for (int i = lastBullet.length - 1; i >= 0; i--) {
      //Logger.log("closing " + lastBullet[i]);
      buffer.append(LI_CLOSE + NEWLINE);
      buffer.append(closeList.get(new Character(lastBullet[i])));
      if(i > 0)
      {
          buffer.append(NEWLINE);
      }
    }
  }
}
