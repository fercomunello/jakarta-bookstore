package io.github.bookstore.jakarta;

import io.github.bookstore.jakarta.webpack.WebpackManifest;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@Startup
@Singleton
@ApplicationPath(value = "/api")
public class JakartaApplication extends Application {

    @Inject
    private WebpackManifest webpack;

    @PostConstruct
    void init() {
        this.webpack.load();
    }

}