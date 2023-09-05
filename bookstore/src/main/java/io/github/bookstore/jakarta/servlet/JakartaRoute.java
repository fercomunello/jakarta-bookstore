package io.github.bookstore.jakarta.servlet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@ApplicationScoped
public class JakartaRoute {

    @Inject
    private HttpServletRequest request;

    public String get() {
        if (this.request.getAttribute("route") instanceof String route) {
            return route;
        }
        String route;
        if ((route = this.request.getPathInfo()) == null) {
            return "/";
        }
        route = route.replaceAll("\\s", "").replaceAll("(?<!^)/+$", "");
        this.request.setAttribute("route", route);
        return route;
    }

}
