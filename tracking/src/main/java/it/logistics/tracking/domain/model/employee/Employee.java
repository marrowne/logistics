package it.logistics.tracking.domain.model.employee;

import it.logistics.common.domain.model.AbstractId;

public final class Employee extends AbstractId {

    public Employee(String id) {
        super(id);
    }

    protected Employee() {
        super();
    }

}
