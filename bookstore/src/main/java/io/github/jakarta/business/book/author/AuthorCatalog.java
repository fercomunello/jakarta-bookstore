package io.github.jakarta.business.book.author;

import io.github.jakarta.business.book.author.AuthorDataTable.AuthorInfo;
import io.github.jakarta.business.Catalog;
import io.github.jakarta.entity.Result;
import io.github.jakarta.entity.UUID;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AuthorCatalog implements Catalog<Author, AuthorInfo> {

    @Override
    public Result<Author> get(final UUID uuid) {
        return null;
    }

    @Override
    public void remove(final UUID uuid) {

    }

    @Override
    public Iterable<AuthorInfo> paginate() {
        return List.of(
            new AuthorDataTable.Builder()
                .uuid(new UUID())
                .fullName("Masashi Kishimoto")
                .build()
        );
    }
}
