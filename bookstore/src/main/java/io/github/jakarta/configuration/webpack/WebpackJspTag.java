package io.github.jakarta.configuration.webpack;

public interface WebpackJspTag {

    WebpackManifest WEBPACK_MANIFEST = new WebpackManifest();

    static String css(String name) {
        name = name + ".css";
        return WEBPACK_MANIFEST.getResource(name);
    }

    static String javascript(String name) {
        name = name + ".js";
        return WEBPACK_MANIFEST.getResource(name);
    }

    static String png(String name) {
        name = "assets/" + name + ".png";
        return WEBPACK_MANIFEST.getResource(name);
    }

    static String icon(String name) {
        name = "assets/" + name + ".ico";
        return WEBPACK_MANIFEST.getResource(name);
    }
}