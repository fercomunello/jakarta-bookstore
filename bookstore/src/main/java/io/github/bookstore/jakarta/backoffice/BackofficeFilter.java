package io.github.bookstore.jakarta.backoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/backoffice/*", "/admin/*"}, dispatcherTypes = DispatcherType.REQUEST)
final class BackofficeFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (true) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/");
        }
    }
}
