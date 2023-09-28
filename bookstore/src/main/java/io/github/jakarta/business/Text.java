package io.github.jakarta.business;

public final class Text implements CharSequence {

    private final CharSequence origin;

    public Text(final CharSequence origin) {
        this.origin = origin;
    }

    public Text(final String origin) {
        this.origin = origin;
    }

    public Text(final Object origin) {
        this.origin = origin != null ? origin.toString() : null;
    }

    @Override
    public String toString() {
        return this.origin != null ? this.origin.toString() : "";
    }

    @Override
    public int length() {
        return this.origin != null ? this.origin.length() : 0;
    }

    @Override
    public char charAt(final int index) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return this.origin.charAt(index);
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        final int length = length();
        if (start < 0 || start > end || end > length) {
            throw new StringIndexOutOfBoundsException(
                "begin " + start + ", end " + end + ", length " + length);
        }
        return this.origin.subSequence(start, end);
    }
}
