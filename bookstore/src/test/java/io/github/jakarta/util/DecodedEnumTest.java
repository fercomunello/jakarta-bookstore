package io.github.jakarta.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

final class DecodedEnumTest {

    @Test
    @DisplayName("Create an enum set with only successfully decoded entries")
    void testEnumSetCreation() {
        final var sennins = new DecodedEnum<>(Shinobi.class).compute(
            new Decode<>(true, Shinobi.TSUNADE),
            new Decode<>(true, Shinobi.JIRAYA),
            new Decode<>(true, Shinobi.OROCHIMARU),
            new Decode<>(false, Shinobi.SARUTOBI)
        );
        Assertions.assertEquals(3, sennins.size());
    }

    @Test
    @DisplayName("Create the enum set but skipping empty entries")
    void testEnumSetCreationSkippingEmptyEntries() {
        Assertions.assertEquals(1,
            new DecodedEnum<>(Shinobi.class).compute(
                new Decode<>(true, Shinobi.JIRAYA),
                Optional::empty, Optional::empty
            ).size()
        );
    }

    private enum Shinobi {
        JIRAYA, TSUNADE, OROCHIMARU, SARUTOBI
    }
}
