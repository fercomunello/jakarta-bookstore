package io.github.jakarta.i18n;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mvc.MvcContext;

@ApplicationScoped
@Named(value = "msg")
public class Messages implements MessageBundle {

    @Inject
    private MvcContext context;

    @Override
    public String bundleName() {
        return "i18n/messages";
    }

    @Override
    public String get(final String key) {
        return bundle(this.context.getLocale()).getString(key);
    }

    public static MessageBundle instance() {
        return CDI.current().select(Messages.class).get();
    }
}