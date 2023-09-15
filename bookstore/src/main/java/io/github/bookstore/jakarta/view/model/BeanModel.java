package io.github.bookstore.jakarta.view.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class BeanModel implements Serializable {

    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isCached() {
        return this.uuid != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanModel bean = (BeanModel) o;
        return Objects.equals(uuid, bean.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
