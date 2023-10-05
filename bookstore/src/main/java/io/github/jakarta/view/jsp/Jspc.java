package io.github.jakarta.view.jsp;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public final class Jspc {

    private static final UUID JSPC_TOKEN = UUID.randomUUID();

    public static boolean checkRequestToken(final HttpServletRequest request) {
        return JSPC_TOKEN.toString().equals(
            request.getParameter("jspc")
        );
    }

    public static String asURLParameter(final boolean first) {
        return (first ? '?' : '&') + "jspc=" + JSPC_TOKEN;
    }
}
