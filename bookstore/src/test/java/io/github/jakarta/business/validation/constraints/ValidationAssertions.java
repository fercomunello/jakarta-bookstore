package io.github.jakarta.business.validation.constraints;

import io.github.jakarta.business.validation.Validation;
import io.github.jakarta.business.validation.ValidationResult;
import io.github.jakarta.i18n.Messages;
import io.github.jakarta.i18n.MockMessageBundle;
import io.github.jakarta.i18n.MessageBundle;
import org.mockito.Mockito;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ValidationAssertions {

    private static final MessageBundle VALIDATION_MESSAGES = new MockMessageBundle(
        Locale.getDefault(), "i18n/validations"
    );

    protected void withMessagesMock(final Runnable runnable) {
        try (var mocked = Mockito.mockStatic(Messages.class)) {
            mocked.when(Messages::instance).thenReturn(new MockMessageBundle(
                Locale.getDefault(), "i18n/messages"
            ));
            runnable.run();
        }
    }

    protected void assertValid(final Validation validation) {
        final var result = validation.validate(VALIDATION_MESSAGES);
        assertNotNull(result);
        assertTrue(result.isValid());

        assertEquals("", result.toString());

        assertNotNull(result.getTarget());
    }

    protected ValidationResult assertInvalid(final Validation validation) {
        final var result = validation.validate(VALIDATION_MESSAGES);
        assertNotNull(result);
        assertFalse(result.isValid());

        assertFalse(result.toString().isBlank());

        assertNotNull(result.getTarget());

        return result;
    }
}
