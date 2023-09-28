package io.github.jakarta.business.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class ValidatedGroup implements Iterable<ValidationResult> {

    public static final ValidatedGroup EMPTY = new ValidatedGroup(new ValidationResult[]{});

    private final ValidationResult[] validationResults;

    public ValidatedGroup(final ValidationResult[] results) {
        this.validationResults = results;
    }

    @Override
    public Iterator<ValidationResult> iterator() {
        return Arrays.stream(this.validationResults).iterator();
    }
}