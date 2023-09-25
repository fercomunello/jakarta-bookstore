package database;

public enum Table {
    ;

    final String name;

    Table() {
        this.name = name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
