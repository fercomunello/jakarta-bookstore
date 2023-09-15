package io.github.bookstore.jakarta.view;

import jakarta.ws.rs.core.Response;

import java.util.List;

public abstract class DataTable<T> extends View {

    public abstract String name();

    public Response ok(final Rows<T> rows) {
        this.models.put(this.name(), rows.fetch());
        return Response.ok(this.view().get()).build();
    }

    @FunctionalInterface
    public interface Rows<T> {
        List<T> fetch();
    }
}
