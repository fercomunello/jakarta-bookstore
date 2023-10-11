package io.github.jakarta.validation;

public final class ValidationResult {

    private final Validation.Metadata metadata;
    private final InvalidReason invalidReason;

    public ValidationResult(final Validation.Metadata metadata,
                            final InvalidReason invalidReason) {
        this.metadata = metadata;
        this.invalidReason = invalidReason;
    }

    @Override
    public String toString() {
        return this.invalidReason.reason();
    }

    public boolean isValid() {
        return this.invalidReason == InvalidReason.NONE;
    }

    public String getTarget() {
        return this.metadata.target();
    }
}