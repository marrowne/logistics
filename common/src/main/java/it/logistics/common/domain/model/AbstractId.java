package it.logistics.common.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractId implements Identity, Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = true, length = 6)
    private String id;

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            AbstractId typedObject = (AbstractId) anObject;
            equalObjects = this.getId().equals(typedObject.getId());
        }

        return equalObjects;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [id=" + id + "]";
    }

    protected AbstractId(String id) {
        this();

        this.setId(id);
    }

    protected AbstractId() {
        super();
    }

    protected void validateId(String id) { }

    private void setId(String id) {
        if (id.equals("")) {
            throw new IllegalArgumentException("The basic identity is required.");
        }
        this.validateId(id);

        this.id = id;
    }
}
