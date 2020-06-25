package it.logistics.tracking.application;

import it.logistics.tracking.domain.model.parcel.*;
import it.logistics.tracking.infrastructure.persistence.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class ParcelServiceImpl implements ParcelService {

    private final ParcelStatus[] ADMINISTRATOR_AVAILABLE_STATES = {
            ParcelStatus.CREATED,
            ParcelStatus.PICKUP,
            ParcelStatus.SRC_SORTING,
            ParcelStatus.TRANSIT,
            ParcelStatus.DEST_SORTING,
            ParcelStatus.IN_DELIVERY,
            ParcelStatus.RETURNED,
            ParcelStatus.RET_DEST_SORTING,
            ParcelStatus.RET_TRANSIT,
            ParcelStatus.RET_SRC_SORTING,
            ParcelStatus.RET_IN_DELIVERY,
            ParcelStatus.RET_REJECTED
    };

    private final ParcelStatus[] COURIER_AVAILABLE_STATES = {
            ParcelStatus.CREATED,
            ParcelStatus.DEST_SORTING,
            ParcelStatus.IN_DELIVERY,
            ParcelStatus.RET_SRC_SORTING,
            ParcelStatus.RET_IN_DELIVERY
    };

    private final ParcelStatus[] SORTING_AVAILABLE_STATES = {
            ParcelStatus.PICKUP,
            ParcelStatus.SRC_SORTING,
            ParcelStatus.TRANSIT,
            ParcelStatus.RETURNED,
            ParcelStatus.RET_DEST_SORTING,
            ParcelStatus.RET_TRANSIT,
            ParcelStatus.RET_REJECTED
    };

    @Autowired
    private final ParcelRepository parcelRepository;

    public ParcelServiceImpl(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @Override
    public void addParcel(Parcel parcel) {
        parcelRepository.save(parcel);
    }

    @Override
    public Parcel findParcel(Long id) {
        return parcelRepository.getOne(id);
    }

    @Override
    public Page<Parcel> allParcels(PageRequest pageRequest) { return parcelRepository.findAll(pageRequest); }

    @Override
    public Page<Parcel> allParcels(PageRequest pageRequest, String query) {
        Example<Parcel> parcelExample = null;
        try {
            Parcel parcelQuery = new Parcel();
            parcelQuery.setSearchString(query);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withIgnorePaths("id")
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase();
            parcelExample = Example.of(parcelQuery,matcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parcelRepository.findAll(parcelExample, pageRequest);
    }

    @Override
    public Page<Parcel> allParcels(PageRequest pageRequest, String query, Boolean filterAvailableOnly, DeliveryWorker deliveryWorker) {
        ParcelSpecification parcelSpecification = null;
        if (deliveryWorker.getClass().getSimpleName().equals("Administrator")) {
            parcelSpecification = new ParcelSpecification(query,
                    filterAvailableOnly,
                    ADMINISTRATOR_AVAILABLE_STATES);
        } else if (deliveryWorker.getClass().getSimpleName().equals("Courier")) {
            parcelSpecification = new ParcelSpecification(query,
                    filterAvailableOnly,
                    COURIER_AVAILABLE_STATES);
        } else if (deliveryWorker.getClass().getSimpleName().equals("SortingWorker")) {
            parcelSpecification = new ParcelSpecification(query,
                    filterAvailableOnly,
                    SORTING_AVAILABLE_STATES);
        }
        return parcelRepository.findAll(parcelSpecification, pageRequest);
    }

    @Override
    public void courierReceived(Courier courier, Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(IllegalArgumentException::new);
        parcel.courierReceived(courier);
    }

    @Override
    public void sortingReceived(SortingWorker sortingWorker, Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(IllegalArgumentException::new);
        parcel.sortingReceived(sortingWorker);
    }

    @Override
    public void sortingLeft(SortingWorker sortingWorker, Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(IllegalArgumentException::new);
        parcel.sortingLeft(sortingWorker);
    }

    @Override
    public void courierDelivered(Courier courier, Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(IllegalArgumentException::new);
        parcel.courierDelivered(courier);
    }

    @Override
    public void courierReturned(Courier courier, Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(IllegalArgumentException::new);
        parcel.courierReturned(courier);
    }


}
