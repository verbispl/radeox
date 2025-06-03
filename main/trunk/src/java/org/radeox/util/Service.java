package org.radeox.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * After the Service class from Sun and the Apache project. With help from
 * Frédéric Miserey.
 * <p/>
 * credits Frédéric Miserey, Joseph Oettinger
 *
 * @author Matthias L. Jugel
 * @version $id$
 */
public class Service
{
    private static final Log LOG = LogFactory.getLog(Service.class);

    public static final int RESOURCE = 0;
    public static final int CLASS = 1;
    public static final int INSTANCE = 2;

    static HashMap services = new HashMap();

    public static synchronized <C> Iterator<C> providerNames(final Class<C> cls)
    {
        return providers(cls, Service.RESOURCE);
    }

    public static synchronized <C> Iterator<C>
        providerClasses(final Class<C> cls)
    {
        return providers(cls, false);
    }

    public static synchronized <C> Iterator<C> providers(final Class<C> cls)
    {
        return providers(cls, true);
    }

    public static synchronized <C> Iterator<C> providers(final Class<C> cls,
        final boolean instantiate)
    {
        return providers(cls, instantiate ? Service.INSTANCE : Service.CLASS);
    }

    public static synchronized <C> Iterator<C> providers(final Class<C> cls,
        final int providerKind)
    {
        final ClassLoader cl = cls.getClassLoader();
        String providerFile = "META-INF/services/" + cls.getName();

        // check whether we already loaded the provider classes
        List<C> providers = (List<C>) services.get(providerFile);
        if(providers != null)
        {
            return providers.iterator();
        }

        // create new list of providers
        providers = new ArrayList<>();
        services.put(providerFile, providers);

        try
        {
            // get all files with the name providerFile out of all jar files
            final Enumeration<URL> providerFiles = cl
                .getResources(providerFile);

            if(providerFiles.hasMoreElements())
            {
                // cycle through the provider files and load classes
                while(providerFiles.hasMoreElements())
                {
                    final URL url = providerFiles.nextElement();
                    try(final Reader reader = new InputStreamReader(
                        url.openStream(), UTF_8))
                    {
                        switch (providerKind)
                        {
                            case Service.RESOURCE:
                                loadResources(reader, cl, (List) providers);
                                break;
                            case Service.CLASS:
                                loadClasses(reader, cl, (List) providers);
                                break;
                            case Service.INSTANCE:
                                loadInstances(reader, cl, providers);
                                break;
                        }
                    }
                    catch(final Exception ex)
                    {
                        LOG.error("Error reading file: " + providerFile, ex);
                        // ex.printStackTrace();
                        // Just try the next file...
                    }
                }
            }
            else
            {
                // Workaround for broken classloaders, e.g. Orion
                InputStream is = cl.getResourceAsStream(providerFile);
                if(is == null)
                {
                    providerFile = providerFile
                        .substring(providerFile.lastIndexOf('.') + 1);
                    is = cl.getResourceAsStream(providerFile);
                }
                if(is != null)
                {
                    final Reader reader = new InputStreamReader(is, UTF_8);
                    loadInstances(reader, cl, providers);
                }
            }
        }
        catch(final IOException ioe)
        {
            // ioe.printStackTrace();
            // ignore exception
        }
        return providers.iterator();
    }

    private static void loadResources(final Reader input,
        final ClassLoader classLoader, final List<String> providers)
        throws IOException
    {
        final BufferedReader reader = new BufferedReader(input);
        String line = reader.readLine();
        while(line != null)
        {
            providers.add(line);
            line = reader.readLine();
        }
    }

    private static void loadClasses(final Reader input, final ClassLoader cl,
        final List<Class<?>> classes) throws IOException
    {
        for(final String className : readLines(input))
        {
            try
            {
                classes.add(cl.loadClass(className));
            }
            catch(final ClassNotFoundException e)
            {
                // ignore silently
            }
        }
    }

    private static <C> void loadInstances(final Reader input,
        final ClassLoader cl, final List<C> providers) throws IOException
    {
        for(String className : readLines(input))
        {
            final int modifierIndex = className.indexOf('_');
            String modifier = null;
            if(modifierIndex != -1)
            {
                modifier = className.substring(modifierIndex + 1);
                className = className.substring(0, modifierIndex);
            }
            try
            {
                final Class<?> klass = cl.loadClass(className);
                final C obj = (C) klass.newInstance();
                if(null != modifier)
                {
                    final Method setModifierMethod = klass
                        .getMethod("setModifier", String.class);
                    setModifierMethod.invoke(obj, modifier);
                }
                // stick it into our vector...
                providers.add(obj);
            }
            catch(final NoSuchMethodException e)
            {
                // ignore silently
            }
            catch(final InvocationTargetException e)
            {
                // ignore silently
            }
            catch(final ClassNotFoundException e)
            {
                // ignore silently
            }
            catch(final InstantiationException e)
            {
                e.printStackTrace();
            }
            catch(final IllegalAccessException e)
            {
                e.printStackTrace();
            }
            // Logger.debug("Service: loaded "+ obj.getClass().getName());
        }
    }

    /**
     * Read all lines from the reader and filter out comment lines starting with
     * a hash mark (#)
     *
     * @param input the stream reader to contain lines
     * @return a list containing all significant lines
     * @throws IOException if the reader was interrupted
     */
    private static List<String> readLines(final Reader input) throws IOException
    {
        final List<String> linesList = new ArrayList<>();

        final BufferedReader reader = new BufferedReader(input);
        String line;
        while((line = reader.readLine()) != null)
        {
            try
            {
                // strip comments out of the stream
                final int idx = line.indexOf('#');
                if(idx != -1)
                {
                    line = line.substring(0, idx);
                }

                // Trim whitespace
                line = line.trim();

                // store line if there is still more than spaces on it
                if(line.length() > 0)
                {
                    linesList.add(line);
                }
            }
            catch(final Exception ex)
            {
                // ex.printStackTrace();
                // Just try the next line
            }
        }
        return linesList;
    }

}
