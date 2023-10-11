package io.github.jakarta.validation;

@FunctionalInterface
public interface InvalidReason{

    InvalidReason NONE = () -> "";

    String reason();
}