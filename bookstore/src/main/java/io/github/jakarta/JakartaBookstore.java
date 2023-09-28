package io.github.jakarta;

import io.github.jakarta.view.JakartaMvc;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashMap;
import java.util.Map;

@ApplicationPath(value = "/bookstore")
public class JakartaBookstore extends Application {

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<>(JakartaMvc.Configuration.PROPERTIES);
    }
}
