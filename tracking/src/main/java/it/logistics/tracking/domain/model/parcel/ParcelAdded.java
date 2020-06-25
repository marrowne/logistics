package it.logistics.tracking.domain.model.parcel;

import it.logistics.common.domain.model.DomainEvent;

import java.util.Date;

public class ParcelAdded implements DomainEvent {

    private int eventVersion;
    private long employeeId;
    private Date occurDate;
    private Sender sender;
    private Receiver receiver;
    private Courier courier;
    private DeliveryWorker created_by;

    public ParcelAdded(long employeeId, Sender sender, Receiver receiver, Courier courier, DeliveryWorker created_by) {

        super();

        this.eventVersion = 1;
        this.employeeId = employeeId;
        this.occurDate = new Date();
        this.sender = sender;
        this.receiver = receiver;
        this.courier = courier;
        this.created_by = created_by;
    }

    @Override
    public int getEventVersion() {
        return this.eventVersion;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    @Override
    public Date getOccurDate() {
        return this.occurDate;
    }

    public Sender getSender() {
        return sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public Courier getCourier() {
        return courier;
    }

    public DeliveryWorker getCreated_by() {
        return created_by;
    }

}
