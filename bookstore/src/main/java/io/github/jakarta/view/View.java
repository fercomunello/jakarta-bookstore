package io.github.jakarta.view;

import io.github.jakarta.JakartaApp;
import jakarta.inject.Inject;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;

public abstract class View extends JakartaApp {

    @Inject
    protected Models models;

    protected abstract ViewLocation view();

    public Response ok() {
        return Response.status(Response.Status.OK)
                .entity(this.view().get())
                .build();
    }
}
