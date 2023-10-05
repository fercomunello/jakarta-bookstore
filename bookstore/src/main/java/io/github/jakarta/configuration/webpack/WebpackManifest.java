package io.github.jakarta.configuration.webpack;

import io.github.jakarta.configuration.JakartaProfile;
import io.github.jakarta.configuration.JakartaStartup;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class WebpackManifest implements Runnable {

    private static final String WEBPACK_BUNDLE_DIR = "/dist";
    private static final String WEBPACK_MANIFEST = "/META-INF/webpack.manifest.json";

    private final Logger logger;

    private final ConcurrentMap<String, String> webpackBundles;
    private final AtomicReference<FileTime> webpackBundlesLMT;

    {
        this.logger = Logger.getLogger(WebpackManifest.class);
        this.webpackBundles = new ConcurrentHashMap<>();
        this.webpackBundlesLMT = new AtomicReference<>();
    }

    @Override
    public void run() {
        reloadWebpackBundles();
    }

    private void reloadWebpackBundles() {
        if (!this.webpackBundles.isEmpty() &&
            JakartaStartup.PROFILE == JakartaProfile.PRODUCTION) {
            this.logger.warnf(
                "The attempt to reload webpack entries in production was ignored."
            );
            return;
        }
        final URL resource = WebpackManifest.class.getResource(WEBPACK_MANIFEST);
        if (resource == null) {
            this.logger.errorf("%s file not found in the classloader.", WEBPACK_MANIFEST);
            return;
        }
        if (JakartaStartup.PROFILE == JakartaProfile.DEVELOPMENT) {
            try {
                final FileTime lastModifiedTime = Files.getLastModifiedTime(
                    Path.of(resource.getPath()), LinkOption.NOFOLLOW_LINKS
                );
                if (lastModifiedTime.equals(webpackBundlesLMT.get())) {
                    return;
                }
                this.webpackBundlesLMT.set(lastModifiedTime);
                this.logger.infof("%s file has been changed, updating webpack " +
                                  "bundles hash map.", WEBPACK_MANIFEST);
            } catch (IOException ignored) {}
        }
        try (InputStream inputStream = resource.openStream()) {
            final JsonObject entries = Json.createReader(inputStream).readObject();
            if (entries == null || entries.isEmpty()) {
                this.logger.errorf("No webpack entries found in %s file.", WEBPACK_MANIFEST);
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
                    if (JakartaStartup.PROFILE == JakartaProfile.DEVELOPMENT) {
                        this.webpackBundles.entrySet()
                            .removeIf(entry -> !entries.containsKey(entry.getKey()));
                        if (this.webpackBundles.containsKey(key)) {
                            this.webpackBundles.replace(key, hashedFilePath.get());
                            continue;
                        }
                    }
                    webpackBundles.put(key, hashedFilePath.get());
                }
            }
        } catch (IOException exception) {
            this.logger.error(exception.getMessage(), exception);
        }
    }

    public String getResource(final String fileName) {
        if (JakartaStartup.PROFILE == JakartaProfile.DEVELOPMENT) {
            reloadWebpackBundles();
        }
        final String resource = this.webpackBundles.get(fileName);
        if (resource != null) {
            return resource;
        }
        this.logger.warnf("No webpack entry for %s", fileName);
        return "/no/manifest/entry/for/" + fileName;
    }
}