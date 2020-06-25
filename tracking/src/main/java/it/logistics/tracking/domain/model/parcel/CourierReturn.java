package it.logistics.tracking.domain.model.parcel;

import it.logistics.common.domain.model.DomainEvent;

import java.util.Date;

public class CourierReturn implements DomainEvent {

    private int eventVersion;
    private Date occurDate;
    private long parcelId;
    private Courier courier;
    private ParcelStatus previousStatus;
    private ParcelStatus newStatus;

    public CourierReturn(Courier courier, long parcelId, ParcelStatus previousStatus, ParcelStatus newStatus) {

        super();

        this.eventVersion = 1;
        this.occurDate = new Date();
        this.courier = courier;
        this.parcelId = parcelId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }

    @Override
    public int getEventVersion() {
        return this.eventVersion;
    }

    @Override
    public Date getOccurDate() {
        return this.occurDate;
    }

    public long getParcelId() {
        return parcelId;
    }

    public Courier getCourier() {
        return courier;
    }

    public ParcelStatus getPreviousStatus() {
        return previousStatus;
    }

    public ParcelStatus getNewStatus() {
        return newStatus;
    }

}
