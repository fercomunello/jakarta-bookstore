package io.github.jakarta.view.jsp;

import io.github.jakarta.business.Backoffice;
import jakarta.servlet.ServletContext;
import org.jboss.logging.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class JspcRunnable implements Runnable {

    private static final boolean JSPC_ENABLED = true,
                                 LOG_INFO = true;

    private final Logger logger;
    private final ExecutorService executorService;
    private final JspLocationFunction jspLocation;

    public JspcRunnable(final ExecutorService executorService,
                        final ServletContext servletContext) {
        this.logger = Logger.getLogger(JspcRunnable.class);
        this.executorService = executorService;
        this.jspLocation = (location) -> new File(
            servletContext.getRealPath("WEB-INF/views/" + location)
        ).exists();
    }

    @Override
    public void run() {
        if (!JSPC_ENABLED) {
            return;
        }

        final Instant start = Instant.now();

        record CompilationTask(String jspPath, String jspName, boolean success) {}
        final List<CompletableFuture<CompilationTask>> completableFutures = new ArrayList<>();

        try (var httpClient = HttpClient.newBuilder()
                .executor(this.executorService)
                .build()) {

            final Backoffice[] pages = Backoffice.values();

            short waiting = 0;
            boolean serverAlive = false;
            final short waitMax = 3500, waitInterval = 5;

            for (short index = 0; index < pages.length; ) {
                final var jspPath = pages[index].resolveViewPath();
                final var jspName = jspPath.substring(jspPath.indexOf('/') + 1);

                final URI uri;
                try {
                    uri = new URI("http://localhost:8080/bookstore"
                                  + pages[index].uriPath() + Jspc.asURLParameter(true)
                    );
                } catch (final URISyntaxException ex) {
                    this.logger.error(ex.getMessage(), ex);
                    continue;
                }

                final var completableFuture = httpClient
                    .sendAsync(
                        HttpRequest.newBuilder()
                            .uri(uri)
                            .GET()
                            .build(),
                        HttpResponse.BodyHandlers.ofString()
                    )
                    .thenApply(response ->
                        new CompilationTask(
                            jspPath, jspName, response.statusCode() == 200
                        )
                    );

                completeLater: {
                    if (!serverAlive && waiting < waitMax) { // 🚬
                        if (completableFuture.join().success()) {
                            serverAlive = true;
                        } else {
                            waiting += waitInterval;
                            try {
                                TimeUnit.MILLISECONDS.sleep(waitInterval);
                            } catch (InterruptedException ex) {
                                this.logger.error(ex.getMessage(), ex);
                            }
                            break completeLater;
                        }
                    }
                    completableFutures.add(completableFuture);
                    index++;
                }
            }

            if (LOG_INFO) {
                byte i = 0, j = 1;
                final StringBuilder log = new StringBuilder();
                for (final var future : completableFutures) {
                    appendLog: {
                        final CompilationTask compilationTask;
                        try {
                            compilationTask = future.get();
                        } catch (InterruptedException | ExecutionException ex) {
                            this.logger.error(ex.getMessage(), ex);
                            break appendLog;
                        }

                        final boolean greenJsp = compilationTask.success()
                                                 && this.jspLocation.exists(compilationTask.jspPath());

                        log.append("\u001B[").append(greenJsp ? "32m" : "91m")
                            .append(compilationTask.jspName())
                            .append("\u001B[0m");
                        if (i < completableFutures.size() - 1) {
                            log.append(", ");
                        }
                        if (j == 3 && i < completableFutures.size() - 1) {
                            log.append("\n    ");
                            j = 0;
                        } else {
                            j++;
                        }
                        i++;
                    }
                }
                this.logger.infof("%n\u001B[37m Pre-compiled JSPs:" +
                                  "\u001B[0m \u001B[90m[\u001B[0m %s \u001B[90m] ~ %dms\u001B[0m %n%n%n",
                    log.toString(), Duration.between(start, Instant.now()).toMillis()
                );
            }
        }
    }

    @FunctionalInterface
    private interface JspLocationFunction {
        boolean exists(String jspLocation);
    }
}