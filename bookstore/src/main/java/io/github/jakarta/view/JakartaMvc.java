package io.github.jakarta.view;

import io.github.jakarta.view.htmx.HxFlag;
import io.github.jakarta.view.jsp.Jspc;
import io.github.jakarta.view.model.template.BackofficeTemplate;
import io.github.jakarta.view.model.template.BookstoreTemplate;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.mvc.engine.ViewEngine;
import jakarta.mvc.engine.ViewEngineContext;
import jakarta.mvc.engine.ViewEngineException;
import jakarta.mvc.security.Csrf;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.krazo.Properties;
import org.eclipse.krazo.engine.JspViewEngine;

import java.util.Map;
import java.util.Optional;

@ApplicationScoped
@Priority(ViewEngine.PRIORITY_APPLICATION)
public class JakartaMvc extends JspViewEngine {

    @Inject
    private Instance<BookstoreTemplate> bookstoreTemplate;

    @Inject
    private Instance<BackofficeTemplate> backofficeTemplate;

    @Override
    public void processView(final ViewEngineContext context) throws ViewEngineException {
        Entrypoint.resolve(context.getView()).ifPresent(entrypoint -> {
            final HttpServletRequest request = context.getRequest(HttpServletRequest.class);

            context.getModels().put("invokeJspc", Jspc.checkRequestToken(request));

            context.getModels().put("hxBoosted",
                new HxFlag(HxFlag.Header.HX_BOOSTED, request)
                    .activeOrElse(() ->
                        context.getModels().put("template",
                            switch (entrypoint) {
                                case BOOKSTORE -> bookstoreTemplate.get();
                                case BACKOFFICE -> backofficeTemplate.get();
                            }
                    ))
            );
        });

        final HttpServletResponse response = context.getResponse(HttpServletResponse.class);
        response.setHeader("Vary", "HX-Request");

        super.processView(context);
    }

    public interface Configuration {
        Map<String, Object> PROPERTIES = Map.of(
                Csrf.CSRF_PROTECTION, Csrf.CsrfOptions.EXPLICIT,
                Properties.REDIRECT_SCOPE_COOKIES, true
        );
    }

    private enum Entrypoint {
        BOOKSTORE("bookstore"), BACKOFFICE("backoffice");

        final String value;

        Entrypoint(String value) {
            this.value = value;
        }

        static Optional<Entrypoint> resolve(final String pattern) {
            if (pattern.startsWith(Entrypoint.BOOKSTORE.value)) {
                return Optional.of(Entrypoint.BOOKSTORE);
            } else if (pattern.startsWith(Entrypoint.BACKOFFICE.value)) {
                return Optional.of(Entrypoint.BACKOFFICE);
            }
            return Optional.empty();
        }
    }
}