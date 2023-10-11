package io.github.jakarta.validation.constraints;

import io.github.jakarta.business.person.PersonName;
import io.github.jakarta.common.Text;
import io.github.jakarta.validation.Validation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class TextValidationTest extends ValidationAssertions {

    @Test
    @DisplayName("Any nullable text should always produce an empty value")
    void testTextDefaultValue() {
        assertEquals("", new Text(null).toString());
    }

    @Test
    @DisplayName("Not blank texts should be valid")
    void testTextIsNotBlank() {
        withMessagesMock(() -> {
            assertValid(new NotBlankText("Duke"));
            assertValid(new NotBlankText("  Duke  "));
        });
    }

    @Test
    @DisplayName("Blank texts should be invalid")
    void testTextIsBlankOrEmpty() {
        withMessagesMock(() -> {
            assertInvalid(new NotBlankText(null));
            assertInvalid(new NotBlankText(""));
            assertInvalid(new NotBlankText(" "));
            assertInvalid(new NotBlankText("  "));
        });
    }

    @Test
    @DisplayName("Text length should be on the given range")
    void testTextLength() {
        withMessagesMock(() -> {
            assertValid(new TextLengthOf("Duke", 1, 4));
            assertInvalid(new TextLengthOf("Duke mascot", 1, 4));

            assertInvalid(new TextLengthOf("Duke, the mascot of Java", 0, 23));
            assertValid(new TextLengthOf("Duke, the Java's mascot", 0, 23));

            assertInvalid(new TextLengthOf(null, 0, 1));
            assertInvalid(new TextLengthOf("", 0, 1));
            assertInvalid(new TextLengthOf(" ", 1, 1));
        });
    }

    @Test
    @DisplayName("Length of people's names should be within the given range as well")
    void testPersonNameLength() {
        withMessagesMock(() -> {
            assertValid(new NameLengthOf(
                new PersonName("Masashi", "Kishimoto"), 1, 17
            ));

            assertInvalid(new NameLengthOf(new PersonName("", ""), 0, 1));
            assertInvalid(new NameLengthOf(new PersonName("  ", "     "), 0, 1));
        });
    }

    @Test
    @DisplayName("Each validation result should have label and target")
    void testValidationMetadata() {
        withMessagesMock(() -> {
            var result = assertInvalid(new NotBlankText("  ", TestMetadata.TEXT));

            assertTrue(result.toString().contains(TestMetadata.TEXT.label()));
            assertEquals(TestMetadata.TEXT.target(), result.getTarget());

            result = assertInvalid(
                new TextLengthOf("a sample text ...", 0, 16)
            );

            assertTrue(result.toString().contains(TestMetadata.TEXT.label()));
            assertEquals(TestMetadata.TEXT.target(), result.getTarget());

            result = assertInvalid(
                new NameLengthOf(
                    new PersonName("Masashi", "Kishimoto"), 0, 16,
                    TestMetadata.NAME
                )
            );

            assertTrue(result.toString().contains(TestMetadata.NAME.label()));
            assertEquals(TestMetadata.NAME.target(), result.getTarget());
        });
    }

    enum TestMetadata implements Validation.Metadata {
        TEXT, NAME
    }
}