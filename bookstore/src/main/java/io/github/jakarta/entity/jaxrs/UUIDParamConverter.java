package io.github.jakarta.entity.jaxrs;

import io.github.jakarta.entity.UUID;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@SuppressWarnings({"unused", "unchecked"})
public final class UUIDParamConverter implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType,
                                              final Type genericType,
                                              final Annotation[] annotations) {
        if (rawType.equals(UUID.class)) {
            return (ParamConverter<T>) new UUIDConverter();
        }
        return null;
    }
}
