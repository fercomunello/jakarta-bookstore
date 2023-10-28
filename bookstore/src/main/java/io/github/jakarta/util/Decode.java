package io.github.jakarta.util;

import java.util.Optional;

public final class Decode<T> implements Expression<T> {

    private final T value;
    private final boolean expression;

    public Decode(final boolean expression, final T value) {
        this.expression = expression;
        this.value = value;
    }

    @Override
    public Optional<T> compute() {
        return this.expression ? Optional.of(this.value) : Optional.empty();
    }
}