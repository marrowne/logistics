package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Embeddable;

@Embeddable
public class Courier extends DeliveryWorker {

    public Courier(String id) {
        super(id);
    }

    public Courier() {
    }

}
