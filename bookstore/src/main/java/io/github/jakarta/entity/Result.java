package io.github.jakarta.entity;

import java.time.LocalDateTime;

public final class Result<T> {

    private final T value;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Result(final T value) {
        this(value, null, null);
    }

    public Result(final T value, final LocalDateTime createdAt) {
        this(value, createdAt, null);
    }

    public Result(final T value,
                  final LocalDateTime createdAt,
                  final LocalDateTime updatedAt) {
        this.value = value;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public T unwrap() {
        return this.value;
    }

    public LocalDateTime createdAt() {
        return this.createdAt;
    }

    public LocalDateTime updatedAt() {
        return this.updatedAt;
    }
}
