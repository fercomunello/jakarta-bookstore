package io.github.jakarta.view.model;

import java.time.Year;

public abstract class Template {

    private final int year;

    {
        this.year = Year.now().getValue();
    }

    public int getYear() {
        return year;
    }

}
