package io.github.jakarta.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public interface MessageBundle {

    Locale LOCALE_PT_BR = new Locale("pt", "BR");
    Locale LOCALE_EN_US = new Locale("en", "US");

    String bundleName();

    String get(final String key);

    default ResourceBundle bundle(final Locale locale) {
        return LOCALE_PT_BR.getLanguage().equals(locale.getLanguage())
            ? ResourceBundle.getBundle(bundleName(), LOCALE_PT_BR)
            : ResourceBundle.getBundle(bundleName(), LOCALE_EN_US);
    }
}
