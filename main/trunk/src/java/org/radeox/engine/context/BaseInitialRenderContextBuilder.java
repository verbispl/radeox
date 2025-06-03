package org.radeox.engine.context;

import java.util.Locale;

/**
 * Builder for {@link BaseInitialRenderContext}.
 *
 * <p>Created on 2025-06-02</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class BaseInitialRenderContextBuilder
{
    private static final Locale BASIC_LOCALE = new Locale("Basic", "basic");

    private String languageBuldleName = "radeox_messages";
    private Locale inputLocale;
    private Locale outputLocale;
    private String inputBundleName;
    private String outputBundleName;

    public BaseInitialRenderContextBuilder()
    {
        this.languageBuldleName = "radeox_messages";
        this.inputLocale = BASIC_LOCALE;
        this.outputLocale = BASIC_LOCALE;
        this.inputBundleName = "radeox_markup";
        this.outputBundleName = "radeox_markup";
    }

    public BaseInitialRenderContextBuilder setLanguageBuldleName(final String languageBuldleName)
    {
        this.languageBuldleName = languageBuldleName;
        return this;
    }

    public BaseInitialRenderContextBuilder setInputLocale(final Locale inputLocale)
    {
        this.inputLocale = inputLocale;
        return this;
    }

    public BaseInitialRenderContextBuilder setOutputLocale(final Locale outputLocale)
    {
        this.outputLocale = outputLocale;
        return this;
    }

    public BaseInitialRenderContextBuilder setInputBundleName(final String inputBundleName)
    {
        this.inputBundleName = inputBundleName;
        return this;
    }

    public BaseInitialRenderContextBuilder setOutputBundleName(final String outputBundleName)
    {
        this.outputBundleName = outputBundleName;
        return this;
    }

    public BaseInitialRenderContext build()
    {
        return new BaseInitialRenderContext(languageBuldleName, inputLocale,
            outputLocale, inputBundleName, outputBundleName);
    }

}
