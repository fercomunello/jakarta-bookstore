package io.github.jakarta.util;

import java.util.EnumSet;
import java.util.Set;

public final class DecodedEnum<E extends Enum<E>> implements Expressions<E> {

    private final Class<E> enumClass;

    public DecodedEnum(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @SafeVarargs
    public final Set<E> compute(final Expression<E>... expressions) {
        final var enumSet = EnumSet.noneOf(this.enumClass);
        for (final var exp : expressions) {
            exp.compute().ifPresent(enumSet::add);
        }
        return enumSet;
    }
}