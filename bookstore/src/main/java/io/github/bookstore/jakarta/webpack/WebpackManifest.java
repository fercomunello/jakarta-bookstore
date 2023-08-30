package io.github.bookstore.jakarta.webpack;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class WebpackManifest {

    private static final Logger LOG = Logger.getLogger(WebpackManifest.class);

    private static final String WEBPACK_BUNDLE_DIR = "/dist";
    private static final String WEBPACK_MANIFEST = "/META-INF/webpack.manifest.json";

    private static final Map<String, String> MANIFEST_RESOURCES = new HashMap<>();

    public void load() {
        try (InputStream inputStream = WebpackManifest.class.getResourceAsStream(WEBPACK_MANIFEST)) {
            if (inputStream == null) {
                LOG.errorf("%s file not found in the classloader.", WEBPACK_MANIFEST);
            } else {
                final JsonObject entries = Json.createReader(inputStream).readObject();
                if (entries != null) {
                    MANIFEST_RESOURCES.clear();
                    entries.forEach((key, value) -> {
                        if (key != null && value != null) {
                            final String hashedFileName = ((JsonString) value).getString();
                            MANIFEST_RESOURCES.put(key, WEBPACK_BUNDLE_DIR + '/' + hashedFileName);
                        }
                    });
                } else {
                    LOG.errorf("No webpack entries found in %s file.", WEBPACK_MANIFEST);
                }
            }
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
        }
    }

    static String getResource(final String fileName) {
        final String resource = MANIFEST_RESOURCES.get(fileName);
        if (resource != null) return resource;
        LOG.warnf("No webpack entry for %s", fileName);
        return "/no/manifest/entry/for/" + fileName;
    }

}