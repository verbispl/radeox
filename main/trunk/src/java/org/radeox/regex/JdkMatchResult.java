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

package org.radeox.regex;

/**
 * The result when a Matcher object finds matches in some input Implementation
 * for regex package in JDK 1.4.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: JdkMatchResult.java,v 1.1 2004/04/16 07:37:52 stephan Exp $
 */
public class JdkMatchResult extends MatchResult
{
    private final java.util.regex.Matcher matcher;

    public JdkMatchResult(final java.util.regex.Matcher matcher)
    {
        this.matcher = matcher;
    }

    public JdkMatchResult(final Matcher matcher)
    {
        this.matcher = ((JdkMatcher) matcher).getMatcher();
    }

    @Override
    public int groups()
    {
        return matcher.groupCount();
    }

    @Override
    public String group(final int i)
    {
        return matcher.group(i);
    }

    @Override
    public int beginOffset(final int i)
    {
        return matcher.start(i);
    }

    @Override
    public int endOffset(final int i)
    {
        return matcher.end(i);
    }

}
