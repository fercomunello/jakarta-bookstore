package io.github.jakarta.business.backoffice;

import io.github.jakarta.view.ViewLocation;
import io.github.jakarta.view.View;
import jakarta.mvc.Controller;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Controller
@Path("/backoffice")
@Produces(MediaType.TEXT_HTML)
public class Dashboard extends View {

    @Override
    protected ViewLocation view() {
        return Backoffice.DASHBOARD;
    }

    @GET
    public Response index() {
        return dashboard();
    }

    @GET
    @Path("/dashboard")
    public Response dashboard() {
        return ok();
    }

}
