package io.github.bookstore.jakarta.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;

import java.io.IOException;

public abstract class JakartaServerPage {

    private static final Logger LOG = Logger.getLogger(JakartaServerPage.class);

    @Inject
    protected HttpServletRequest request;

    @Inject
    protected Model model;

    public String path() {
        final var className = this.getClass().getSimpleName();
        if (className.endsWith("Page")) {
            return className.substring(0, className.length() - 4).toLowerCase();
        }
        return className.toLowerCase();
    }

    public BrowserAction checkBrowserAction() {
        boolean htmxBoostHeader = Boolean.parseBoolean(this.request.getHeader("HX-Boosted"));
        final var browserAction = htmxBoostHeader ? BrowserAction.XHR_REQUEST : BrowserAction.REQUEST;
        this.model.set("browserAction", browserAction);
        return browserAction;
    }

    public void process(final HttpServletResponse response) {
        final RequestDispatcher dispatcher = this.request.getRequestDispatcher(
                "/WEB-INF/" + this.context() + '/' + this.path() + ".jsp");
        try {
            dispatcher.forward(this.request, response);
        } catch (ServletException | IOException exception) {
            // TODO: Create a error page to avoid showing technical details to the end user.
            LOG.error(exception.getMessage(), exception);
        }
    }

    public abstract String context();

    public abstract void processRequest();

    public abstract void processXhrRequest();

    public abstract void addModels();

    @FunctionalInterface
    public interface Router {
        JakartaServerPage view(String route);
    }
}
