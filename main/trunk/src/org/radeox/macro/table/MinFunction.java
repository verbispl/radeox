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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A function that finds the max of table cells
 *
 * @author stephan
 * @version $Id: MinFunction.java,v 1.2 2003/06/11 10:04:27 stephan Exp $
 */

public class MinFunction implements Function {
  private static Log log = LogFactory.getLog(MinFunction.class);


  public String getName() {
    return "MIN";
  }

  public void execute(Table table, int posx, int posy, int startX, int startY, int endX, int endY) {
    float min = Float.MAX_VALUE;
    boolean floating = false;
    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        //Logger.debug("x="+x+" y="+y+" >"+getXY(x,y));
        float value = 0;
        try {
          value += Integer.parseInt((String) table.getXY(x, y));
        } catch (Exception e) {
          try {
            value += Float.parseFloat((String) table.getXY(x, y));
            floating = true;
          } catch (NumberFormatException e1) {
            log.debug("SumFunction: unable to parse " + table.getXY(x, y));
          }
        }
        if (min > value) {
          min = value;
        }
      }
    }
    //Logger.debug("Sum="+sum);
    if (floating) {
      table.setXY(posx, posy, "" + min);
    } else {
      table.setXY(posx, posy, "" + (int) min);
    }
  }

}
