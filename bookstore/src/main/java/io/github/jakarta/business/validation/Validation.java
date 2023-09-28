package io.github.jakarta.business.validation;

import io.github.jakarta.i18n.MessageBundle;
import io.github.jakarta.i18n.Messages;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface Validation {

    ValidationResult validate(MessageBundle messages);

    interface Metadata {

        ConcurrentMap<String, String> LABELS = new ConcurrentHashMap<>();

        default String label() {
            final String bundleKey, targetKey = this.target();
            bundleKey = LABELS.computeIfAbsent(
                targetKey, str -> str.replace('_', '.').toLowerCase()
            );
            return Messages.instance().get(bundleKey);
        }

        default String target() {
            return this.toString();
        }

        Metadata OF_TEXT = new Metadata() {
            @Override
            public String target() {
                return "TEXT";
            }
        };
    }

}
