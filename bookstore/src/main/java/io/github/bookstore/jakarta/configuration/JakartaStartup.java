package io.github.bookstore.jakarta.configuration;

import io.github.bookstore.jakarta.configuration.webpack.WebpackManifest;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Startup
@Singleton
public class JakartaStartup {

    public static final JakartaProfile PROFILE;

    static {
        final String profile = System.getProperty("profile");
        if ("development".equalsIgnoreCase(profile)) {
            PROFILE = JakartaProfile.DEVELOPMENT;
        } else {
            PROFILE = JakartaProfile.PRODUCTION;
        }
    }

    @PostConstruct
    void init() {
        WebpackManifest.load();
    }

}