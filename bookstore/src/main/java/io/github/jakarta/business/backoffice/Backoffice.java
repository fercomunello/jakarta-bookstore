package io.github.jakarta.business.backoffice;

import io.github.jakarta.business.Text;
import io.github.jakarta.view.ViewLocation;

import java.io.File;

public enum Backoffice implements ViewLocation {

    DASHBOARD_JSP(Backoffice.HOME),

    BOOK_AUTHOR_LIST_JSP(Backoffice.AUTHORS),
    BOOK_AUTHOR_FORM_JSP(Backoffice.AUTHORS + Backoffice.EMPTY_FORM) ;

    public static final String ENTRYPOINT = "/backoffice";
    public static final String DASHBOARD = "/dashboard";
    public static final String HOME = ENTRYPOINT + DASHBOARD;

    public static final String EMPTY_FORM = "/new";

    public static final String AUTHORS = ENTRYPOINT + "/authors";

    private final Text jspPath, uriPath;

    Backoffice(final String uriPath) {
        final var constant = this.name();
        final var suffix = "_JSP";
        if (!constant.endsWith(suffix)) {
            throw new IllegalStateException(
                "Invalid suffix for enum constant %s, replace it with '%s'"
                    .formatted(constant, suffix)
            );
        }
        this.jspPath = new Text(
            ENTRYPOINT.replace('/', File.separatorChar)
            + File.separatorChar
            + constant
                .replace(suffix, suffix.replace('_', '.'))
                .toLowerCase()
                .replace('_', File.separatorChar)
        );
        this.uriPath = new Text(uriPath);
    }

    @Override
    public String resolveViewPath() {
        return this.jspPath.toString()
            .substring(1);
    }

    public String uriPath() {
        return this.uriPath.toString();
    }
}