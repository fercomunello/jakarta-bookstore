package io.github.bookstore.jakarta.backoffice;

import io.github.bookstore.jakarta.servlet.JakartaServerPage;

import java.time.Year;

abstract class BackofficePage extends JakartaServerPage {

    @Override
    public String context() {
        return "backoffice";
    }

    @Override
    public void processRequest() {
        this.model.set("year", Year.now().getValue());
    }

    @Override
    public void processXhrRequest() {

    }
}
