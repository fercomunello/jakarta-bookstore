package io.github.bookstore.jakarta.view;

import jakarta.inject.Inject;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;

public abstract class View {

    @Inject
    protected Models models;

    protected abstract ViewLocation view();

    public Response ok() {
        return Response.status(Response.Status.OK)
                .entity(this.view().get())
                .build();
    }
}
