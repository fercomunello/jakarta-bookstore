package io.github.jakarta.validation.constraints;

import io.github.jakarta.business.person.Name;
import io.github.jakarta.validation.InvalidReason;
import io.github.jakarta.validation.Validation;
import io.github.jakarta.validation.ValidationResult;
import io.github.jakarta.i18n.MessageBundle;

public final class NameLengthOf implements Validation {

    private final Name name;
    private final int min, max;
    private final Metadata metadata;

    public NameLengthOf(final Name name, final int min, final int max) {
        this(name, min, max, new Metadata() {
            @Override
            public String label() {
                return "Name";
            }
            @Override
            public String target() {
                return "name";
            }
        });
    }

    public NameLengthOf(final Name name,
                        final int min, final int max,
                        final Metadata metadata) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.metadata = metadata;
    }

    @Override
    public ValidationResult validate(final MessageBundle messages) {
        final var label = this.metadata.label();
        var reason = InvalidReason.NONE;
        if (this.name.first().isBlank() || this.name.last().isBlank()) {
            reason = () -> messages.get("$.cannot.be.empty")
                .formatted(label);
        } else {
            final var fullName = this.name.toString();
            if (fullName.length() < this.min) {
                reason = () ->
                    messages.get("$.must.have.at.least.$.characters")
                        .formatted(label, this.min);
            }
            else if (fullName.length() > this.max) {
                reason = () ->
                    messages.get("$.cannot.have.more.than.$.characters")
                        .formatted(label, this.max);
            }
        }
        return new ValidationResult(this.metadata, reason);
    }
}
