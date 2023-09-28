package io.github.jakarta.configuration.webpack;

public interface WebpackJspTag {

    static String css(String fileName) {
        fileName = fileName + ".css";
        return WebpackManifest.getResource(fileName);
    }

    static String javascript(String fileName) {
        fileName = fileName + ".js";
        return WebpackManifest.getResource(fileName);
    }

    static String png(String fileName) {
        fileName = fileName + ".png";
        return WebpackManifest.getResource(fileName);
    }

}
