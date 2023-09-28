package io.github.jakarta.business.validation;

@FunctionalInterface
public interface InvalidReason{

    InvalidReason NONE = () -> "";

    String reason();
}