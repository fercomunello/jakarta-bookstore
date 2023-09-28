package io.github.jakarta.view.htmx;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class HxFlag {

    private final Header header;
    private final boolean active;

    public HxFlag(final Header header, final HttpServletRequest request) {
        this.header = header;
        this.active = Boolean.parseBoolean(request.getHeader(header.name));
    }

    public HxFlag(final Header header, final boolean active) {
        this.header = header;
        this.active = active;
    }

    public void addHeader(final HttpServletResponse response) {
        response.setHeader(
            this.header.name, Boolean.toString(this.active)
        );
    }

    public boolean activeOrElse(final Runnable runnable) {
        if (!this.active) {
            runnable.run();
            return false;
        }
        return true;
    }

    public enum Header {
        HX_REQUEST("HX-Request"),
        HX_BOOSTED("HX-Boosted"),
        HX_REPLACE_URL("HX-Replace-Url");

        final String name;

        Header(final String name) {
            this.name = name;
        }
    }
}
