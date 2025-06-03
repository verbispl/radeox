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

package org.radeox.util;

import java.io.IOException;
import java.io.Writer;

/**
 * The same as StringWriter, but takes an existing StringBuilder in its
 * constructor.
 *
 * @author Stephan J. Schmidt
 * @version $Id: StringBufferWriter.java,v 1.2 2003/02/06 13:41:42 leo Exp $
 */
public class StringBufferWriter extends Writer
{

    private final StringBuffer buffer;

    public StringBufferWriter(final StringBuffer buffer)
    {
        this.buffer = buffer;
        this.lock = buffer;
    }

    public StringBufferWriter()
    {
        this.buffer = new StringBuffer();
        this.lock = buffer;
    }

    public StringBufferWriter(final int initialSize)
    {
        if(initialSize < 0)
        {
            throw new IllegalArgumentException("Negative buffer size");
        }
        buffer = new StringBuffer(initialSize);
        lock = buffer;
    }

    @Override
    public void write(final int c)
    {
        buffer.append((char) c);
    }

    @Override
    public void write(final char cbuf[], final int off, final int len)
    {
        if((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0))
        {
            throw new IndexOutOfBoundsException();
        }
        else if(len == 0)
        {
            return;
        }
        buffer.append(cbuf, off, len);
    }

    @Override
    public void write(final String str)
    {
        buffer.append(str);
    }

    @Override
    public void write(final String str, final int off, final int len)
    {
        buffer.append(str.substring(off, off + len));
    }

    @Override
    public String toString()
    {
        return buffer.toString();
    }

    public StringBuffer getBuffer()
    {
        return buffer;
    }

    @Override
    public void flush()
    {
    }

    @Override
    public void close() throws IOException
    {
    }

}
