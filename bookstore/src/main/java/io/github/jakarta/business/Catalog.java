package io.github.jakarta.business;

import io.github.jakarta.entity.UUID;
import io.github.jakarta.entity.Result;

public interface Catalog<Item, Info> {

    Result<Item> get(final UUID uuid);

    void remove(final UUID uuid);

    Iterable<Info> paginate();
}