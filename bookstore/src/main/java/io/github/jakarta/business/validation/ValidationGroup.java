package io.github.jakarta.business.validation;

import io.github.jakarta.i18n.ValidationMessages;

public final class ValidationGroup {

    private final Validation[] validations;

    public ValidationGroup(final Validation ... validations) {
        this.validations = validations;
    }

    public ValidatedGroup result() {
        final int length = this.validations.length;
        final ValidationResult[] results = new ValidationResult[length];

        final var bundle = ValidationMessages.instance();

        for (int i = 0; i < length; i++) {
            results[i] = this.validations[i].validate(bundle);
        }

        return new ValidatedGroup(results);
    }
}
