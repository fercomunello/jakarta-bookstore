package io.github.bookstore.jakarta.servlet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

@ApplicationScoped
public class Model {

    @Inject
    private HttpServletRequest request;

    public Model data(final String name, final Object data) {
        this.set(name, data);
        return this;
    }

    public void set(final String name, final Object data) {
        this.request.setAttribute(name, data);
    }

    public Object get(final String name, final Object data) {
        return this.request.getAttribute(name);
    }

    public void remove(final String name, final Object data) {
       this.request.removeAttribute(name);
    }
}
