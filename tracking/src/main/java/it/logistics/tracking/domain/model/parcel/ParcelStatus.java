package it.logistics.tracking.domain.model.parcel;

public enum ParcelStatus {
    CREATED,
    PICKUP,
    SRC_SORTING,
    TRANSIT,
    DEST_SORTING,
    IN_DELIVERY,
    DELIVERED,
    RETURNED,
    RET_DEST_SORTING,
    RET_TRANSIT,
    RET_SRC_SORTING,
    RET_IN_DELIVERY,
    RET_DELIVERED,
    RET_REJECTED,
    RET_UNDELIVERED
}
