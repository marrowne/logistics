package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Embeddable;

@Embeddable
public class Administrator extends DeliveryWorker {

    public Administrator(String id) {
        super(id);
    }

    public Administrator() {
    }

}
