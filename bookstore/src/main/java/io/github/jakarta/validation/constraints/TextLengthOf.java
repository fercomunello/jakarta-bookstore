package io.github.jakarta.validation.constraints;

import io.github.jakarta.common.Text;
import io.github.jakarta.validation.InvalidReason;
import io.github.jakarta.validation.Validation;
import io.github.jakarta.validation.ValidationResult;
import io.github.jakarta.i18n.MessageBundle;

public final class TextLengthOf implements Validation {

    private final Text text;
    private final int min, max;
    private final Metadata metadata;

    public TextLengthOf(final CharSequence chars, final int min, final int max) {
        this(new Text(chars), min, max, Metadata.OF_TEXT);
    }

    public TextLengthOf(final Text text,
                        final int min, final int max,
                        final Metadata metadata) {
        this.text = text;
        this.min = min;
        this.max = max;
        this.metadata = metadata;
    }

    @Override
    public ValidationResult validate(final MessageBundle messages) {
        final var label = this.metadata.label();
        final var str = this.text.toString();
        var reason = InvalidReason.NONE;
        if (str.isBlank()) {
            reason = () -> messages.get("$.cannot.be.empty")
                .formatted(label);
        } else {
            if (str.length() < this.min) {
                reason = () ->
                    messages.get("$.must.have.at.least.$.characters")
                        .formatted(label, this.min);
            }
            else if (str.length() > this.max) {
                reason = () ->
                    messages.get("$.cannot.have.more.than.$.characters")
                        .formatted(label, this.max);
            }
        }
        return new ValidationResult(this.metadata, reason);
    }
}
