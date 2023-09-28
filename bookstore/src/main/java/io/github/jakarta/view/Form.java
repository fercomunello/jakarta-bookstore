package io.github.jakarta.view;

import io.github.jakarta.Bean;
import io.github.jakarta.business.validation.ValidatedGroup;
import io.github.jakarta.business.validation.ValidationResult;
import io.github.jakarta.entity.Entity;
import io.github.jakarta.entity.Result;
import io.github.jakarta.view.htmx.HxFlag;
import io.github.jakarta.view.model.FormBean;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Form<T extends Entity, BeanInstance> extends View {

    @Inject
    private Instance<BeanInstance> beanInstance;

    @Inject
    private Instance<FormBean> formBeanInstance;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletResponse response;

    public Response ok(final Supplier<Result<T>> resultSupplier) {
        if (!this.formBeanInstance.get().isCached()) {
            final Result<T> result = resultSupplier.get();
            updateForm(result.unwrap(), form -> {
                form.setCreatedAt(result.createdAt());
                form.setUpdatedAt(result.updatedAt());
            });
        }
        return ok();
    }

    public Response postRedirect(final T entity) {
        final ValidatedGroup validations = entity.validate();

        for (final ValidationResult validation : validations) {
            if (validation.isValid()) {
                continue;
            }

            updateForm(entity, form -> {
                form.setCached(true);
                form.setValidations(validations);
            });

            new HxFlag(HxFlag.Header.HX_REPLACE_URL, false)
                .addHeader(this.response);

            return Response.status(Status.BAD_REQUEST)
                .entity(view().get())
                .build();
        }

        final Result<Entity> result = entity.save(this.database);

        updateForm(result.unwrap(), form -> {
            form.setCached(true);
            form.setValidations(validations);
            form.setCreatedAt(result.createdAt());
            form.setUpdatedAt(result.updatedAt());
        });

        final URI uri = this.uriInfo.getAbsolutePathBuilder()
            .path(entity.uuid().toString())
            .build();

        return Response.status(Status.SEE_OTHER)
            .location(uri)
            .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void updateForm(final Entity entity,
                            final Consumer<FormBean> formBeanConsumer) {
        formBeanConsumer.accept(this.formBeanInstance.get());
        final T type = ((T) entity);
        if (type instanceof Bean unpackable) {
           final BeanInstance bean = this.beanInstance.get();
           unpackable.unpack(bean);
       }
    }
}