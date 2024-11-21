package org.example.entities;

public abstract class BaseEntity {
    private final int id;

    protected BaseEntity(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
