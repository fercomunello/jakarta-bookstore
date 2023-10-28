package io.github.jakarta.util;

import java.util.Optional;

public interface Expression<T> {

    Optional<T> compute();
}
