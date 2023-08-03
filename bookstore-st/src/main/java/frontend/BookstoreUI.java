package frontend;

import com.codeborne.selenide.*;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;
import org.openqa.selenium.logging.LogType;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.getWebDriverLogs;

public final class BookstoreUI {

    private static final Logger LOG = Logger.getLogger(BookstoreUI.class);

    public BookstoreUI() {
        final Config config = ConfigProvider.getConfig();

        Configuration.browser = config.getOptionalValue(
                "bookstore.test.ui.browser", String.class).orElse("chrome");
        Configuration.headless = config.getOptionalValue(
                "bookstore.test.ui.headless", Boolean.class).orElse(true);

        Configuration.screenshots = config.getOptionalValue(
                "bookstore.test.ui.screenshot-on-failure", Boolean.class).orElse(true);
        Configuration.savePageSource = config.getOptionalValue(
                "bookstore.test.ui.save-page-on-failure", Boolean.class).orElse(true);

        Configuration.pageLoadTimeout = config.getOptionalValue(
                "bookstore.test.ui.page-load-timeout", Short.class)
                .map(TimeUnit.SECONDS::toMillis).orElse(20_000L);

        Configuration.assertionMode = AssertionMode.STRICT;
        Configuration.selectorMode = SelectorMode.CSS;
        Configuration.pageLoadStrategy = "normal";
    }

    private String indexUrl() {
        return this.uriBuilder().toTemplate();
    }

    private UriBuilder uriBuilder() {
        final Config config = ConfigProvider.getConfig();
        final var host = config.getValue("bookstore.test.host", String.class);
        final var port = config.getValue("bookstore.test.port", String.class);

        return UriBuilder.fromUri("http://{host}:{port}/")
                .resolveTemplate("host", host)
                .resolveTemplate("port", port);
    }

    public IndexView indexView() {
        Selenide.open(indexUrl());
        return new IndexView();
    }

    public static void close() {
        final List<String> logs = getWebDriverLogs(LogType.BROWSER, Level.SEVERE);
        logs.forEach(LOG::error);
        closeWindow();
    }
}
