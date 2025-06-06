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

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.StringSubstitution;

/**
 * Called with a MatchResult which is substituted This implementation is needed
 * by Jakarta ORO.
 *
 * @author stephan
 * @team sonicteam
 * @version $Id: OroActionSubstitution.java,v 1.1 2004/04/20 13:45:36 stephan Exp $
 */
public class OroActionSubstitution extends StringSubstitution
{
    private final org.radeox.regex.Substitution substitution;

    public OroActionSubstitution(final Substitution substitution)
    {
        this.substitution = substitution;
    }

    @Override
    public void appendSubstitution(final StringBuffer stringBuffer,
        final MatchResult matchResult, final int i,
        final PatternMatcherInput patternMatcherInput,
        final PatternMatcher patternMatcher,
        final org.apache.oro.text.regex.Pattern pattern)
    {
        substitution.handleMatch(stringBuffer, new OroMatchResult(matchResult));
    }

}
