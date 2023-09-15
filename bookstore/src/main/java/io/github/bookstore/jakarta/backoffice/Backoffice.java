package io.github.bookstore.jakarta.backoffice;

import io.github.bookstore.jakarta.view.ViewLocation;

public enum Backoffice implements ViewLocation {

    DASHBOARD;

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
