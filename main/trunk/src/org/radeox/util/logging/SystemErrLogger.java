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


package org.radeox.util.logging;


/**
 * Concrete Logger that logs to System Err
 *
 * @author stephan
 * @version $Id: SystemErrLogger.java,v 1.3 2003/03/25 10:37:54 leo Exp $
 */

public class SystemErrLogger implements LogHandler {
  public void log(String output) {
    System.err.println(output);
  }
  public void log(String output, Throwable e) {
    System.err.println(output);
    if (Logger.PRINT_STACKTRACE) e.printStackTrace(System.err);
  }
}
