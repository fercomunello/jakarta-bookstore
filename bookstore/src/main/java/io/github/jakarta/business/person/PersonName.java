package io.github.jakarta.business.person;

import io.github.jakarta.common.Text;

public final class PersonName implements Name {

    private final Text firstName;
    private final Text lastName;

    public PersonName(final CharSequence firstName, final CharSequence lastName) {
        this(new Text(firstName), new Text(lastName));
    }

    public PersonName(final Text firstName, final Text lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String first() {
        return this.firstName.toString();
    }

    @Override
    public String last() {
        return this.lastName.toString();
    }

    @Override
    public String toString() {
        return first() + ' ' + last();
    }
}

