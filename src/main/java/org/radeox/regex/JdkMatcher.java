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
 * Matcher matches regular expressions (Pattern) to input Implementation for
 * regex package in JDK 1.4
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: JdkMatcher.java,v 1.5 2004/04/16 09:10:38 stephan Exp $
 */
public class JdkMatcher extends Matcher
{
    private final JdkPattern pattern;
    private final String input;
    private final java.util.regex.Matcher internalMatcher;

    @Override
    public String substitute(final Substitution substitution)
    {
        final MatchResult matchResult = new JdkMatchResult(internalMatcher);
        final StringBuffer buffer = new StringBuffer();
        while(internalMatcher.find())
        {
            internalMatcher.appendReplacement(buffer, "");
            substitution.handleMatch(buffer, matchResult);
        }
        internalMatcher.appendTail(buffer);
        return buffer.toString();
    }

    @Override
    public String substitute(final String substitution)
    {
        return internalMatcher.replaceAll(substitution);
    }

    protected java.util.regex.Matcher getMatcher()
    {
        return internalMatcher;
    }

    public JdkMatcher(final String input, final Pattern pattern)
    {
        this.input = input;
        this.pattern = (JdkPattern) pattern;
        internalMatcher = this.pattern.getPattern().matcher(this.input);

    }

    @Override
    public boolean contains()
    {
        internalMatcher.reset();
        return internalMatcher.find();
    }

    @Override
    public boolean matches()
    {
        return internalMatcher.matches();
    }

}
