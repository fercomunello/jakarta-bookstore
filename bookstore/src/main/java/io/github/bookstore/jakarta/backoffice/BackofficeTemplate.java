package io.github.bookstore.jakarta.backoffice;

import io.github.bookstore.jakarta.view.model.Template;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named("template")
public class BackofficeTemplate extends Template {
}
