package it.logistics.tracking.domain.model.parcel;

import it.logistics.common.domain.model.AbstractId;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DeliveryWorker extends AbstractId {

    public DeliveryWorker(String id) {
        super(id);
    }

    public DeliveryWorker() {
    }

    @Override
    protected void validateId(String id) {
        if(id.length() < 6)
            throw new IllegalArgumentException("Delivery worker id should have at least 6 digits");
    }

}
