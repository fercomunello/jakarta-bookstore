package io.github.jakarta.business.backoffice.author;

import io.github.jakarta.business.Text;
import io.github.jakarta.business.backoffice.Backoffice;
import io.github.jakarta.entity.UUID;
import io.github.jakarta.view.Form;
import io.github.jakarta.view.ViewLocation;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mvc.Controller;
import jakarta.mvc.RedirectScoped;
import jakarta.mvc.binding.MvcBinding;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serial;
import java.io.Serializable;

import static io.github.jakarta.business.backoffice.author.AuthorForm.AuthorBean;

@Controller
@Path(Backoffice.AUTHORS)
@Produces(MediaType.TEXT_HTML)
public class AuthorForm extends Form<Author, AuthorBean> {

    @Inject
    private AuthorCatalog authors;

    @Override
    protected ViewLocation view() {
        return Backoffice.BOOK_AUTHOR_FORM_JSP;
    }

    @GET
    @Path(Backoffice.EMPTY_FORM)
    public Response authorForm() {
        return ok();
    }

    @GET
    @Path("/{uuid}")
    public Response author(@PathParam("uuid") final UUID uuid) {
        return ok(() -> this.authors.get(uuid));
    }

    @POST
    public Response postAuthor(@BeanParam final Payload payload) {
        return postRedirect(new Author(payload));
    }

    @PUT
    @Path("/{uuid}")
    public Response putAuthor(@PathParam("uuid") final UUID uuid,
                              @BeanParam final Payload payload) {
        return postRedirect(new Author(uuid, payload));
    }

    public static class Payload {
        @MvcBinding @FormParam("firstName") Text firstName;
        @MvcBinding @FormParam("lastName") Text lastName;
        @MvcBinding @FormParam("notableWork") Text notableWork;
    }

    @Named
    @RedirectScoped
    public static class AuthorBean implements Serializable {

        @Serial
        private static final long serialVersionUID = 4125270991366950024L;

        private Text firstName, lastName, notableWork;

        public Text getFirstName() {
            return firstName;
        }

        public void setFirstName(Text firstName) {
            this.firstName = firstName;
        }

        public Text getLastName() {
            return lastName;
        }

        public void setLastName(Text lastName) {
            this.lastName = lastName;
        }

        public Text getNotableWork() {
            return notableWork;
        }

        public void setNotableWork(Text notableWork) {
            this.notableWork = notableWork;
        }
    }
}