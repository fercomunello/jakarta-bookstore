package io.github.jakarta.entity;

import io.github.jakarta.business.validation.ValidatedGroup;
import io.github.jakarta.sql.Database;

public interface Entity {

    UUID uuid();

    ValidatedGroup validate();

    Result<Entity> save(Database database);

}
