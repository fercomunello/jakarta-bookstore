package io.github.bookstore.jakarta.configuration.webpack;

import io.github.bookstore.jakarta.configuration.JakartaApplication;
import io.github.bookstore.jakarta.configuration.JakartaProfile;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WebpackManifest {

    private static final Logger LOG = Logger.getLogger(WebpackManifest.class);

    private static final String WEBPACK_BUNDLE_DIR = "/dist";
    private static final String WEBPACK_MANIFEST = "/META-INF/webpack.manifest.json";

    private static FileTime WEBPACK_BUNDLES_LMT;
    private static final Map<String, String> WEBPACK_BUNDLES = new HashMap<>();

    public static void load() {
        if (JakartaApplication.PROFILE == JakartaProfile.PRODUCTION && !WEBPACK_BUNDLES.isEmpty()) {
            LOG.warnf("The attempt to reload webpack entries in production was ignored.");
            return;
        }
        final URL resource = WebpackManifest.class.getResource(WEBPACK_MANIFEST);
        if (resource == null) {
            LOG.errorf("%s file not found in the classloader.", WEBPACK_MANIFEST);
            return;
        }
        if (JakartaApplication.PROFILE == JakartaProfile.DEVELOPMENT) {
            try {
                final FileTime lastModifiedTime = Files.getLastModifiedTime(
                        Path.of(resource.getPath()), LinkOption.NOFOLLOW_LINKS
                );
                if (lastModifiedTime.equals(WEBPACK_BUNDLES_LMT)) {
                    return;
                }
                WEBPACK_BUNDLES_LMT = lastModifiedTime;
                LOG.infof("%s file has been changed, updating webpack bundles hash map.", WEBPACK_MANIFEST);
            } catch (IOException ignored) {
            }
        }
        try (InputStream inputStream = resource.openStream()) {
            final JsonObject entries = Json.createReader(inputStream).readObject();
            if (entries == null || entries.isEmpty()) {
                LOG.errorf("No webpack entries found in %s file.", WEBPACK_MANIFEST);
                return;
            }
            for (final Map.Entry<String, JsonValue> jsonEntry : entries.entrySet()) {
                final String key = jsonEntry.getKey();
                final JsonValue value = jsonEntry.getValue();
                if (key != null && value != null) {
                    final var hashedFilePath = new Supplier<String>() {
                        @Override
                        public String get() {
                            final String hashedFileName = ((JsonString) value).getString();
                            return WEBPACK_BUNDLE_DIR + '/' + hashedFileName;
                        }
                    };
                    if (JakartaApplication.PROFILE == JakartaProfile.DEVELOPMENT) {
                        WEBPACK_BUNDLES.entrySet().removeIf(entry -> !entries.containsKey(entry.getKey()));
                        if (WEBPACK_BUNDLES.containsKey(key)) {
                            WEBPACK_BUNDLES.replace(key, hashedFilePath.get());
                            continue;
                        }
                    }
                    WEBPACK_BUNDLES.put(key, hashedFilePath.get());
                }
            }
        } catch (IOException exception) {
            LOG.error(exception.getMessage(), exception);
        }
    }

    static String getResource(final String fileName) {
        if (JakartaApplication.PROFILE == JakartaProfile.DEVELOPMENT) {
            WebpackManifest.load();
        }
        final String resource = WEBPACK_BUNDLES.get(fileName);
        if (resource != null) {
            return resource;
        }
        LOG.warnf("No webpack entry for %s", fileName);
        return "/no/manifest/entry/for/" + fileName;
    }
}