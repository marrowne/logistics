package it.logistics.tracking.domain.model.parcel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.logistics.common.domain.model.DomainEventPublisher;
import liquibase.util.StringUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Optional;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Parcel {

    public Parcel() { }

    public Parcel(Sender sender, Receiver receiver, Courier courier, DeliveryWorker created_by) {
        this.sender = sender;
        this.receiver = receiver;
        this.courier = courier;
        this.modified_by = created_by.getId();

        DomainEventPublisher
                .getInstance()
                .publish(new ParcelAdded(id,
                        sender,
                        receiver,
                        courier,
                        created_by));
    }

    @Id
    private long id;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="name.firstName",
                    column = @Column(name = "SENDER_FIRST_NAME")),
            @AttributeOverride(name="name.lastName",
                    column = @Column(name = "SENDER_LAST_NAME")),
            @AttributeOverride(name="phone.number",
                    column = @Column(name = "SENDER_PHONE")),
            @AttributeOverride(name="address.postalCode",
                    column = @Column(name = "SENDER_POSTAL_CODE")),
            @AttributeOverride(name="address.street",
                    column = @Column(name = "SENDER_STREET")),
            @AttributeOverride(name="address.city",
                    column = @Column(name = "SENDER_CITY")),
            @AttributeOverride(name="address.country",
                    column = @Column(name = "SENDER_COUNTRY"))
    })
    private Sender sender;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="name.firstName",
                    column = @Column(name = "RECEIVER_FIRST_NAME")),
            @AttributeOverride(name="name.lastName",
                    column = @Column(name = "RECEIVER_LAST_NAME")),
            @AttributeOverride(name="phone.number",
                    column = @Column(name = "RECEIVER_PHONE")),
            @AttributeOverride(name="address.postalCode",
                    column = @Column(name = "RECEIVER_POSTAL_CODE")),
            @AttributeOverride(name="address.street",
                    column = @Column(name = "RECEIVER_STREET")),
            @AttributeOverride(name="address.city",
                    column = @Column(name = "RECEIVER_CITY")),
            @AttributeOverride(name="address.country",
                    column = @Column(name = "RECEIVER_COUNTRY"))
    })
    private Receiver receiver;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="id",
                    column = @Column(name = "COURIER_ID"))
    })
    private Courier courier;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ParcelStatus status;

    private Float weight;

    private String modified_by;

    private String searchString;

    @PrePersist
    private void addParcel() {
        this.status = ParcelStatus.CREATED;
        final String fullSearchString = StringUtils.join(Arrays.asList(
                String.valueOf(id),
                Optional.ofNullable(sender).map(Sender::getName).map(FullName::getFirstName).orElse(""),
                Optional.ofNullable(sender).map(Sender::getName).map(FullName::getLastName).orElse(""),
                Optional.ofNullable(sender).map(Sender::getPhone).map(Phone::getNumber).orElse(""),
                Optional.ofNullable(sender).map(Sender::getAddress).map(Address::getStreet).orElse(""),
                Optional.ofNullable(sender).map(Sender::getAddress).map(Address::getPostalCode).orElse(""),
                Optional.ofNullable(sender).map(Sender::getAddress).map(Address::getCity).orElse(""),
                Optional.ofNullable(sender).map(Sender::getAddress).map(Address::getCountry).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getName).map(FullName::getFirstName).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getName).map(FullName::getLastName).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getPhone).map(Phone::getNumber).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getAddress).map(Address::getStreet).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getAddress).map(Address::getPostalCode).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getAddress).map(Address::getCity).orElse(""),
                Optional.ofNullable(receiver).map(Receiver::getAddress).map(Address::getCountry).orElse(""),
                Optional.ofNullable(weight).map(String::valueOf).orElse(""),
                Optional.ofNullable(courier).map(Courier::getId).orElse("")),
                " ");
        this.searchString = StringUtils.substring(fullSearchString, 0, 999);
    }

    public long getId() {
        return id;
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

    public ParcelStatus getStatus() {
        return status;
    }

    public Float getWeight() {
        return weight;
    }

    public String getModified_by() {
        return modified_by;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public void courierReceived(Courier courier) {
        if (status == ParcelStatus.CREATED) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierReceived(courier,
                            id,
                            status,
                            ParcelStatus.PICKUP));
            status = ParcelStatus.PICKUP;
            modified_by = courier.getId();
        } else if (status == ParcelStatus.DEST_SORTING) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierReceived(courier,
                            id,
                            status,
                            ParcelStatus.IN_DELIVERY));
            status = ParcelStatus.IN_DELIVERY;
            modified_by = courier.getId();
        } else if (status == ParcelStatus.RET_SRC_SORTING) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierReceived(courier,
                            id,
                            status,
                            ParcelStatus.RET_IN_DELIVERY));
            status = ParcelStatus.RET_IN_DELIVERY;
            modified_by = courier.getId();
        } else {
            throw new IllegalStateException("Must be in 'created', 'destination sorting' or 'return source sorting' state.");
        }
    }

    public void courierDelivered(Courier courier) {
        if (status == ParcelStatus.IN_DELIVERY) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierDelivered(courier,
                            id,
                            status,
                            ParcelStatus.DELIVERED));
            status = ParcelStatus.DELIVERED;
            modified_by = courier.getId();
        } else if (status == ParcelStatus.RET_IN_DELIVERY) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierDelivered(courier,
                            id,
                            status,
                            ParcelStatus.RET_DELIVERED));
            status = ParcelStatus.RET_DELIVERED;
            modified_by = courier.getId();
        } else {
            throw new IllegalStateException("Must be in 'in delivery' or in it's return equivalent state.");
        }
    }

    public void courierReturned(Courier courier) {
        if (status == ParcelStatus.IN_DELIVERY) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierReturn(courier,
                            id,
                            status,
                            ParcelStatus.RETURNED));
            status = ParcelStatus.RETURNED;
            modified_by = courier.getId();
        } else  if (status == ParcelStatus.RET_IN_DELIVERY) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new CourierReturn(courier,
                            id,
                            status,
                            ParcelStatus.RET_UNDELIVERED));
            status = ParcelStatus.RET_UNDELIVERED;
            modified_by = courier.getId();
        } else {
            throw new IllegalStateException("Must be in 'in delivery' or it's equivalent state.");
        }
    }

    public void sortingReceived(SortingWorker sortingWorker) {
        if (status == ParcelStatus.PICKUP) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingReceived(sortingWorker,
                            id,
                            status,
                            ParcelStatus.SRC_SORTING));
            status = ParcelStatus.SRC_SORTING;
            modified_by = sortingWorker.getId();
        } else if (status == ParcelStatus.TRANSIT) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingReceived(sortingWorker,
                            id,
                            status,
                            ParcelStatus.DEST_SORTING));
            status = ParcelStatus.DEST_SORTING;
            modified_by = sortingWorker.getId();
        } else if (status == ParcelStatus.RETURNED) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingReceived(sortingWorker,
                            id,
                            status,
                            ParcelStatus.RET_DEST_SORTING));
            status = ParcelStatus.RET_DEST_SORTING;
            modified_by = sortingWorker.getId();
        } else if (status == ParcelStatus.RET_TRANSIT) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingReceived(sortingWorker,
                            id,
                            status,
                            ParcelStatus.RET_SRC_SORTING));
            status = ParcelStatus.RET_SRC_SORTING;
            modified_by = sortingWorker.getId();
        } else if (status == ParcelStatus.RET_REJECTED) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingReceived(sortingWorker,
                            id,
                            status,
                            ParcelStatus.RET_UNDELIVERED));
            status = ParcelStatus.RET_UNDELIVERED;
            modified_by = sortingWorker.getId();
        } else {
            throw new IllegalStateException("Must be in 'pickup', 'transit' or it's return equivalent state.");
        }
    }

    public void sortingLeft(SortingWorker sortingWorker) {
        if (status == ParcelStatus.SRC_SORTING) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingLeft(sortingWorker,
                            id,
                            status,
                            ParcelStatus.TRANSIT));
            status = ParcelStatus.TRANSIT;
            modified_by = sortingWorker.getId();
        } else if (status == ParcelStatus.RET_DEST_SORTING) {
            DomainEventPublisher
                    .getInstance()
                    .publish(new SortingLeft(sortingWorker,
                            id,
                            status,
                            ParcelStatus.RET_TRANSIT));
            status = ParcelStatus.RET_TRANSIT;
            modified_by = sortingWorker.getId();
        } else {
            throw new IllegalStateException("Must be in 'source sorting' or 'return destination sorting' state.");
        }
    }

}
