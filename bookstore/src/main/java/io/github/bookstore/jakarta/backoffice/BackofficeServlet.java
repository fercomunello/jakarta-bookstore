package io.github.bookstore.jakarta.backoffice;

import io.github.bookstore.jakarta.servlet.JakartaServlet;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;

@WebServlet(urlPatterns = "/backoffice/*", loadOnStartup = 1,
        description = "The web routes of the bookstore backoffice system.")
final class BackofficeServlet extends JakartaServlet {

    private static final Logger LOG = Logger.getLogger(BackofficeServlet.class);

    @Inject
    private Dashboard dashboard;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        this.dispatchRequest(response, (route) -> switch (route) {
            case "/", "/dashboard" -> this.dashboard;
            default -> {
                // TODO: Create a 404 page to give the proper feedback to the user.
                LOG.warnf("User attempted to access an unknown route: %s", route);
                yield this.dashboard;
            }
        });
    }

}