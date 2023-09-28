package io.github.jakarta.i18n;

import java.util.Locale;

public final class MockMessageBundle implements MessageBundle {

    private final Locale locale;
    private final String resource;

    public MockMessageBundle(final Locale locale, final String resource) {
        this.locale = locale;
        this.resource = resource;
    }

    @Override
    public String bundleName() {
        return this.resource;
    }

    @Override
    public String get(final String key) {
        return bundle(this.locale).getString(key);
    }
}
