package io.github.jakarta.view.model;

import io.github.jakarta.business.validation.ValidatedGroup;
import io.github.jakarta.business.validation.ValidationResult;
import jakarta.inject.Named;
import jakarta.mvc.RedirectScoped;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

@Named
@RedirectScoped
public class FormBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -4486965548968619162L;

    private ValidatedGroup validations = ValidatedGroup.EMPTY;
    private LocalDateTime createdAt, updatedAt;
    private boolean cached = false;

    public FormBean() {
    }

    public Iterator<ValidationResult> getValidations() {
        return this.validations.iterator();
    }

    public void setValidations(final ValidatedGroup validations) {
        this.validations = validations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isCached() {
        return this.cached;
    }

    public void setCached(final boolean cached) {
        this.cached = cached;
    }
}
