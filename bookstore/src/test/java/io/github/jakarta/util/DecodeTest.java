package io.github.jakarta.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class DecodeTest {

    @Test
    @DisplayName("""
        If the decode expression is true then return the object
        else return an empty optional""")
    void testDecodeExpression() {
        var duke = new Decode<>(true, "Duke").compute();
        assertTrue(duke.isPresent());
        assertEquals("Duke", duke.get());

        duke = new Decode<>(false, "Duke").compute();
        assertTrue(duke.isEmpty());
    }
}
