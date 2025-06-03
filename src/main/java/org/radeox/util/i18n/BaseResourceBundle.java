package org.radeox.util.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * String resource bundle.
 *
 * <p>Created on 2025-06-02</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class BaseResourceBundle
{
    private static final Log LOG = LogFactory.getLog(BaseResourceBundle.class);

    private final String baseName;
    private final ResourceBundle bundle;

    public BaseResourceBundle(final String baseName, final ResourceBundle bundle)
    {
        this.baseName = baseName;
        this.bundle = bundle;
    }

    /**
     * Always returns string value of given key.
     *
     * @param key searched key
     * @return value of the key or "???" + key + "???" if not found
     */
    public String getString(final String key)
    {
        try
        {
            return bundle.getString(key);
        }
        catch(final Exception e)
        {
            LOG.warn("missing resource for bundle '" + baseName + "', key '" +
                key + "'", e);
            return "???" + key + "???";
        }
    }

    /**
     * Returns known value of given key or throws an expception.
     *
     * @param key searched key
     * @return found value fo the searched key
     * @throws MissingResourceException when value not found
     */
    public String get(final String key) throws MissingResourceException
    {
        return bundle.getString(key);
    }

}
