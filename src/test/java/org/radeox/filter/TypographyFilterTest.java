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

public class TypographyFilterTest extends FilterTestSupport
{

    @Override
    protected void setUp() throws Exception
    {
        filter = new TypographyFilter();
        super.setUp();
    }

    public void testElipsis()
    {
        assertEquals("Test &#8230; Text",
            filter.filter("Test ... Text", context));
    }

    public void testNotAfter()
    {
        assertEquals("...Text", filter.filter("...Text", context));
    }

    public void testEndOfLine()
    {
        assertEquals("Text&#8230;", filter.filter("Text...", context));
    }

    public void test4Dots()
    {
        assertEquals("Test .... Text",
            filter.filter("Test .... Text", context));
    }

    public void testLineStart()
    {
        assertEquals("&#8230; Text", filter.filter("... Text", context));
    }

    public void testLineEnd()
    {
        assertEquals("Test &#8230;", filter.filter("Test ...", context));
    }

}
