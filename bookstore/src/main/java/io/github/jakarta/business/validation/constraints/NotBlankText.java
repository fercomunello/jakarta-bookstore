package io.github.jakarta.business.validation.constraints;

import io.github.jakarta.business.Text;
import io.github.jakarta.business.validation.InvalidReason;
import io.github.jakarta.business.validation.Validation;
import io.github.jakarta.business.validation.ValidationResult;
import io.github.jakarta.i18n.MessageBundle;

public class NotBlankText implements Validation {

    private final Text text;
    private final Metadata metadata;

    public NotBlankText(final CharSequence chars) {
        this(new Text(chars), Metadata.OF_TEXT);
    }

    public NotBlankText(final CharSequence chars, final Metadata metadata) {
        this(new Text(chars), metadata);
    }

    public NotBlankText(final Text text, final Metadata metadata) {
        this.text = text;
        this.metadata = metadata;
    }

    @Override
    public ValidationResult validate(final MessageBundle messages) {
        var reason = InvalidReason.NONE;
        final String str = this.text.toString();
        if (str.isBlank()) {
            reason = () -> messages.get("$.cannot.be.empty")
                .formatted(this.metadata.label());
        }
        return new ValidationResult(this.metadata, reason);
    }
}