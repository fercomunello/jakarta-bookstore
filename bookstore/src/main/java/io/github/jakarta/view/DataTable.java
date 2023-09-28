package io.github.jakarta.view;

import jakarta.ws.rs.core.Response;

import java.util.function.Supplier;

public abstract class DataTable extends View {

    public abstract String name();

    public Response ok(final Supplier<Iterable<?>> iterable) {
        this.models.put(this.name(), iterable.get());

        return Response.ok(this.view().get()).build();
    }

}
