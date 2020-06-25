package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Embeddable;

@Embeddable
public class SortingWorker extends DeliveryWorker {

    public SortingWorker(String id) {
        super(id);
    }

    public SortingWorker() {
    }

}
