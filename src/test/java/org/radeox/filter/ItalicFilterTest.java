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

public class ItalicFilterTest extends FilterTestSupport
{
    @Override
    protected void setUp() throws Exception
    {
        filter = new ItalicFilter();
        super.setUp();
    }

    public void testItalic()
    {
        assertEquals("<i class=\"italic\">Text</i>",
            filter.filter("~~Text~~", context));
    }

    public void testItalicCreole()
    {
        assertEquals("<i class=\"italic\">Text</i>",
            filterCreole.filter("//Text//", context));
    }

}
