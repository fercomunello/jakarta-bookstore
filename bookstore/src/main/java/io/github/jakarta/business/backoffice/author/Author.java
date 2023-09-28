package io.github.jakarta.business.backoffice.author;

import io.github.jakarta.Bean;
import io.github.jakarta.business.Text;
import io.github.jakarta.business.backoffice.author.AuthorForm.AuthorBean;
import io.github.jakarta.business.validation.ValidatedGroup;
import io.github.jakarta.business.validation.Validation;
import io.github.jakarta.business.validation.constraints.NameLengthOf;
import io.github.jakarta.business.validation.ValidationGroup;
import io.github.jakarta.business.validation.constraints.NotBlankText;
import io.github.jakarta.entity.Result;
import io.github.jakarta.business.Name;
import io.github.jakarta.business.PersonName;
import io.github.jakarta.entity.Entity;
import io.github.jakarta.entity.UUID;
import io.github.jakarta.sql.Database;

public final class Author implements Entity, Bean<AuthorBean> {

    private final UUID uuid;
    private final Name name;
    private final Text notableWork;

    public Author(final AuthorForm.Payload payload) {
        this(new UUID(), payload);
    }

    public Author(final UUID uuid, final AuthorForm.Payload payload) {
        this(uuid,
            new PersonName(
                payload.firstName,
                payload.lastName
            ),
            payload.notableWork
        );
    }

    public Author(final UUID uuid,
                  final Name name,
                  final Text notableWork) {
        this.uuid = uuid;
        this.name = name;
        this.notableWork = notableWork;
    }

    @Override
    public ValidatedGroup validate() {
        return new ValidationGroup(
            new NameLengthOf(this.name, 3, 48, Metadata.NAME),
            new NotBlankText(this.notableWork, Metadata.NOTABLE_WORK)
        ).result();
    }

    @Override
    public Result<Entity> save(final Database database) {
        if (this.uuid.isNew()) {
            // INSERT
            return new Result<>(this);
        }
        // UPDATE
        return new Result<>(this);
    }

    @Override
    public void unpack(final AuthorBean authorBean) {
        authorBean.setFirstName(new Text(this.name.first()));
        authorBean.setLastName(new Text(this.name.last()));
        authorBean.setNotableWork(this.notableWork);
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }

    public enum Metadata implements Validation.Metadata {
        NAME, NOTABLE_WORK
    }
}
