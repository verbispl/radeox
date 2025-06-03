package org.radeox.macro.img;

import java.util.Base64;
import java.util.function.Supplier;

import org.radeox.macro.ImgMacro;

/**
 * File to be rendered by {@link ImgMacro}.
 *
 * <p>Created on 2025-05-28</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ImgMacroFile
{
    private final String filename;
    private final String mime;
    private final Supplier<byte[]> content;
    private final String contentId;

    /**
     * Constructs file container, which should be rendered inline, using
     * {@link Base64} encoding.
     * <p>
     *   Example:
     *   <pre>
     *     <img src="data:;charset=utf-8;base64,/9j/4AAQSkZJRgABAgAAA...">
     *   </pre>
     * </p>
     * <p>
     *   This should be used, when template is displayed in html page.
     * </p>
     *
     * @param filename the name of the file
     * @param mime mime type
     * @param content lazy inicialized file content - not every attachment will
     *      be displayed in rendered output
     */
    public ImgMacroFile(final String filename, final String mime, final Supplier<byte[]> content)
    {
        this.filename = filename;
        this.mime = mime;
        this.content = content;
        this.contentId = null;
    }

    /**
     * Constructs file container, which should be rendered as reference to the
     * MIME part.
     * <p>
     *   Example:
     *   <pre>
     *      <img src="cid:AbcXyz123"/>
     *      [...]
     *      ------=_NextPart_000_0070_01DBA7D0.0BCF1B00
     *      Content-Type: image/jpeg; name="image001.jpg"
     *      Content-Transfer-Encoding: base64
     *      Content-ID: <image001.jpg@01DBA7CF.C2615870>
     *      [...]
     *   </pre>
     * </p>
     * <p>
     *   This should be used, when template is displayed in e-mail body with
     *   attachments.
     * </p>
     *
     * @param filename the name of the file
     * @param mime mime type
     * @param contentId content identifier
     */
    public ImgMacroFile(final String filename, final String mime, final String contentId)
    {
        this.filename = filename;
        this.mime = mime;
        this.content = null;
        this.contentId = contentId;
    }

    public String getMime()
    {
        return mime;
    }

    public String getFilename()
    {
        return filename;
    }

    /**
     * @return content or <code>null</code> if file is not avaliable or
     *      {@link #getContentId()} is set
     */
    public byte[] getContent()
    {
        if(content != null)
        {
            return content.get();
        }
        return null;
    }

    /**
     * Returns MIME content id used to render refenence to the MIME part.
     * <p>
     *   Example:
     *   <pre>
     *      <img src="cid:AbcXyz123"/>
     *      [...]
     *      ------=_NextPart_000_0070_01DBA7D0.0BCF1B00
     *      Content-Type: image/jpeg; name="image001.jpg"
     *      Content-Transfer-Encoding: base64
     *      Content-ID: <image001.jpg@01DBA7CF.C2615870>
     *      [...]
     *   </pre>
     * </p>
     *
     * @return MIME content id, or <code>null</code> when {@link #getContent()}
     *      is set
     */
    public String getContentId()
    {
        return contentId;
    }

    /**
     * Check if fileContainer has no conntent attached.
     *
     * @return <code>true</code> when no content is attached
     */
    public boolean isNoContent()
    {
        return content==null && contentId==null;
    }

    @Override
    public String toString()
    {
        return filename + ":" + mime;
    }

}
