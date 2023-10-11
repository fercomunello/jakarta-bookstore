package io.github.jakarta.business.book.author;

import io.github.jakarta.common.Text;
import io.github.jakarta.business.Backoffice;
import io.github.jakarta.entity.UUID;
import io.github.jakarta.view.DataTable;
import io.github.jakarta.view.ViewLocation;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Controller
@Path(Backoffice.AUTHORS)
@Produces(MediaType.TEXT_HTML)
public class AuthorDataTable extends DataTable {

    @Inject
    private AuthorCatalog authors;

    @Override
    protected ViewLocation view() {
        return Backoffice.BOOK_AUTHOR_LIST_JSP;
    }

    @Override
    public String name() {
        return "authors";
    }

    @GET
    public Response authors() {
        return ok(() -> this.authors.paginate());
    }

    public record AuthorInfo(UUID getUuid, Text getFullName) { }

    public static final class Builder {
        private UUID uuid;
        private Text fullName;

        Builder() { }

        public Builder uuid(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder fullName(final String fullName) {
            return fullName(new Text(fullName));
        }

        public Builder fullName(final Text fullName) {
            this.fullName = fullName;
            return this;
        }

        public AuthorInfo build() {
            return new AuthorInfo(this.uuid, this.fullName);
        }
    }
}