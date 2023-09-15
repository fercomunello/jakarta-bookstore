package io.github.bookstore.jakarta.view;

import io.github.bookstore.jakarta.view.model.BeanModel;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class Form<FormPayload, Entity, Bean extends BeanModel> extends View {

    @Inject
    protected Instance<Bean> beanInstance;

    @Inject
    protected BindingResult validationResult;

    @Context
    protected UriInfo uriInfo;

    protected abstract Entity entityFrom(UUID uuid, FormPayload payload);

    protected abstract Consumer<Bean> beanConsumer(Entity entity);

    public Response ok(final NotCachedFormBean<Bean> formBean) {
        final var bean = this.beanInstance.get();
        if (!bean.isCached()) {
            formBean.get(bean);
        }
        return ok();
    }

    public Response seeOther(final FormPayload payload,
                             final Consumer<Entity> consumer) {
        return this.seeOther(UUID.randomUUID(), payload, consumer);
    }

    public Response seeOther(final UUID uuid,
                             final FormPayload payload,
                             final Consumer<Entity> next) {

        final URI uri = this.uriInfo.getAbsolutePathBuilder()
                .path(uuid.toString())
                .build();

        final var entity = this.entityFrom(uuid, payload);
        next.accept(entity);

        final var bean = this.beanInstance.get();
        this.beanConsumer(entity).accept(bean);

        return Response.status(Status.SEE_OTHER)
                .location(uri)
                .build();
    }

    public Response invalid() {
        this.models.put("errors", String.join("; ", this.validationResult.getAllMessages()));
        return Response.status(Status.BAD_REQUEST)
                .entity(this.view().get())
                .build();
    }

    @FunctionalInterface
    public interface NotCachedFormBean<T> {
        void get(T bean);
    }

}
