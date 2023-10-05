package io.github.jakarta.view;

import io.github.jakarta.JakartaApp;
import io.github.jakarta.view.jsp.Jspc;
import jakarta.inject.Inject;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;

public abstract class View extends JakartaApp {

    @Inject
    protected Models models;

    @Inject
    protected HttpServletRequest request;

    protected abstract ViewLocation view();

    public Response ok() {
        if (view() == ViewLocation.NONE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK)
                .entity(view().resolveViewPath())
                .build();
    }

    public Response ok(Runnable runnable) {
        if (view() == ViewLocation.NONE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!Jspc.checkRequestToken(this.request)) {
            runnable.run();
        }
        return Response
            .ok(view().resolveViewPath())
            .build();
    }
}