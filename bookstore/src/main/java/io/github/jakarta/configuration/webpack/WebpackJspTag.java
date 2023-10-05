package io.github.jakarta.configuration.webpack;

public interface WebpackJspTag {

    WebpackManifest WEBPACK_MANIFEST = new WebpackManifest();

    static String css(String fileName) {
        fileName = fileName + ".css";
        return WEBPACK_MANIFEST.getResource(fileName);
    }

    static String javascript(String fileName) {
        fileName = fileName + ".js";
        return WEBPACK_MANIFEST.getResource(fileName);
    }

    static String png(String fileName) {
        fileName = fileName + ".png";
        return WEBPACK_MANIFEST.getResource(fileName);
    }
}