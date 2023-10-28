package io.github.jakarta.util;

import java.util.Set;

public interface Expressions<T> {

    Set<T> compute(Expression<T>... expressions);
}
