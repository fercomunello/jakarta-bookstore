package io.github.jakarta.business.backoffice;

import io.github.jakarta.view.ViewLocation;

public enum Backoffice implements ViewLocation {

    DASHBOARD,
    BOOK_AUTHOR_FORM,
    BOOK_AUTHOR_LIST;

    private final String path;

    Backoffice() {
        final var relativePath = this.name().toLowerCase().replace("_", "/");
        this.path = "backoffice/" + relativePath + ".jsp";
    }

    @Override
    public String get() {
        return this.path;
    }

}
