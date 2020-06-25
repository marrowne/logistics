package it.logistics.tracking.application;

import it.logistics.tracking.domain.model.parcel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ParcelService {

    void addParcel(Parcel parcel);

    Parcel findParcel(Long id);

    public Page<Parcel> allParcels(PageRequest pageRequest);

    public Page<Parcel> allParcels(PageRequest pageRequest, String query);

    public Page<Parcel> allParcels(PageRequest pageRequest, String query, Boolean filterAvailableOnly, DeliveryWorker deliveryWorker);

    public void courierReceived(Courier courier, Long parcelId);

    public void sortingReceived(SortingWorker sortingWorker, Long parcelId);

    public void sortingLeft(SortingWorker sortingWorker, Long parcelId);

    public void courierDelivered(Courier courier, Long parcelId);

    public void courierReturned(Courier courier, Long parcelId);

}
