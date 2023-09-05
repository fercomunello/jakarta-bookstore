package io.github.bookstore.jakarta.servlet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
public class JakartaServlet extends HttpServlet {

    @Inject
    private JakartaRoute route;

    protected void dispatchRequest(final HttpServletResponse response,
                                   final JakartaServerPage.Router router) {

        final String route = this.route.get();
        final JakartaServerPage jsp = router.view(route);

        final BrowserAction browserAction = jsp.checkBrowserAction();

        if (browserAction == BrowserAction.REQUEST) {
            jsp.processRequest();
            jsp.processXhrRequest();
        } else if (browserAction == BrowserAction.XHR_REQUEST) {
            jsp.processXhrRequest();
        }

        jsp.addModels();
        jsp.process(response);
    }
}
