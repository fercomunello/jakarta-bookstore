package io.github.jakarta.entity;

import java.util.Objects;

public final class UUID {

    private final java.util.UUID uuid;
    private final boolean random;

    public UUID(final java.util.UUID uuid) {
        this.uuid = uuid;
        this.random = false;
    }

    public UUID(final String uuid) {
        this.uuid = java.util.UUID.fromString(uuid);
        this.random = false;
    }

    public UUID() {
        this.uuid = java.util.UUID.randomUUID();
        this.random = true;
    }

    public java.util.UUID value() {
        return this.uuid;
    }

    public boolean isNew() {
        return this.random;
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UUID uuid = (UUID) o;
        return Objects.equals(this.uuid, uuid.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }
}
