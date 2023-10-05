package io.github.jakarta.view;

import io.github.jakarta.view.jsp.Jspc;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.function.Supplier;

public abstract class DataTable extends View {

    public abstract String name();

    public Response ok(final Supplier<Iterable<?>> iterable) {
        if (view() == ViewLocation.NONE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        this.models.put(this.name(), !Jspc.checkRequestToken(this.request)
            ? iterable.get() : List.of());

        return Response
            .ok(view().resolveViewPath())
            .build();
    }
}
