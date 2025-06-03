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

package org.radeox.macro.table;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.radeox.macro.Repository;

/**
 * A Table implementation primarly for the table macro.
 *
 * @author stephan
 * @version $Id: Table.java,v 1.6 2003/05/23 10:47:25 stephan Exp $
 */
public class Table
{
    // current number of rows
    private int indexRow = 0;
    // current number of cols
    private int indexCol = 0;
    private final List rows;
    private List currentRow;
    private List functionOccurences;

    private final Repository<Function> functions;

    public Table()
    {
        rows = new ArrayList(10);
        currentRow = new ArrayList(10);
        functions = FunctionRepository.getInstance();
    }

    public Object getXY(final int x, final int y)
    {
        // perhaps move everything to a twodim array first
        return ((List) rows.get(y)).get(x);
    }

    public void setXY(final int x, final int y, final Object content)
    {
        ((List) rows.get(y)).set(x, content);
    }

    /**
     * Add a cell to the current row of the table
     *
     * @param content Content of the cell
     */
    public void addCell(String content)
    {
        content = content.trim();
        if(content.startsWith("="))
        {
            // Logger.debug("Table.addCell: function found.");
            if(null == functionOccurences)
            {
                functionOccurences = new ArrayList();
            }
            functionOccurences.add(new int[] {indexCol, indexRow});
            // function
        }
        currentRow.add(content);
        indexCol++;
    }

    /**
     * Finishes current row and starts a new one
     */
    public void newRow()
    {
        rows.add(currentRow);
        indexRow++;
        // create new row with number of cells of
        // the last row, this is a good guess
        currentRow = new ArrayList(indexCol);
        indexCol = 0;
    }

    /**
     * Recalculate all cells. Currently does nothing.
     */
    public void calc()
    {
        if(null != functionOccurences)
        {
            final Iterator iterator = functionOccurences.iterator();
            while(iterator.hasNext())
            {
                final int[] position = (int[]) iterator.next();
                final String functionString = ((String) getXY(position[0],
                    position[1])).trim();
                // better use RegEx
                final String name = functionString
                    .substring(1, functionString.indexOf("(")).trim()
                    .toLowerCase();

                final String range = functionString.substring(
                    functionString.indexOf("(") + 1,
                    functionString.indexOf(")"));
                final int colon = range.indexOf(":");
                final String start = range.substring(0, colon).trim();
                final String end = range.substring(colon + 1).trim();

                int startX = start.charAt(0) - 'A';
                int startY = Integer.parseInt(start.substring(1)) - 1;
                int endX = end.charAt(0) - 'A';
                int endY = Integer.parseInt(end.substring(1)) - 1;

                // normalize range, start is left top, end is bottom right
                if(startX > endX)
                {
                    final int tmp = startX;
                    startX = endX;
                    endX = tmp;
                }
                if(startY > endY)
                {
                    final int tmp = startY;
                    startY = endY;
                    endY = tmp;
                }
                if(functions.containsKey(name))
                {
                    final Function function = functions.get(name);
                    function.execute(this, position[0], position[1], startX,
                        startY, endX, endY);
                }
            }
        }
    }

    /**
     * Serialize table by appending it to a writer. The output format is HTML.
     *
     * @param writer Writer to append the table object to
     * @return writer Writer the table object appended itself to
     */
    public Writer appendTo(final Writer writer) throws IOException
    {
        writer.write("<table class=\"wiki-table\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        final List[] outputRows = (List[]) rows.toArray(new List[0]);
        final int rowSize = outputRows.length;
        boolean odd = true;
        for(int i = 0; i < rowSize; i++)
        {
            writer.write("<tr");
            if(i == 0)
            {
                writer.write(">");
            }
            else if(odd)
            {
                writer.write(" class=\"table-odd\">");
                odd = false;
            }
            else
            {
                writer.write(" class=\"table-even\">");
                odd = true;
            }
            final String[] outputCols = (String[]) outputRows[i].toArray(new String[0]);
            final int colSize = outputCols.length;
            for(int j = 0; j < colSize; j++)
            {
                writer.write(i == 0 ? "<th>" : "<td>");
                if(outputCols[j] == null || outputCols[j].trim().length() == 0)
                {
                    writer.write("&#160;");
                }
                else
                {
                    writer.write(outputCols[j]);
                }
                writer.write(i == 0 ? "</th>" : "</td>");
            }
            writer.write("</tr>");
        }
        writer.write("</table>");
        return writer;
    }

}
