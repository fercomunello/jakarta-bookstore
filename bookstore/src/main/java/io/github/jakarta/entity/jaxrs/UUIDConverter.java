package io.github.jakarta.entity.jaxrs;

import io.github.jakarta.entity.UUID;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.Provider;

@Provider
final class UUIDConverter implements ParamConverter<UUID> {

    @Override
    public UUID fromString(final String value) {
        return new UUID(value);
    }

    @Override
    public String toString(final UUID uuid) {
        return uuid.toString();
    }
}
