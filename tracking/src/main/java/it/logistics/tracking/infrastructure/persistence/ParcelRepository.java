package it.logistics.tracking.infrastructure.persistence;

import it.logistics.tracking.domain.model.parcel.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParcelRepository extends JpaRepository<Parcel, Long>, JpaSpecificationExecutor<Parcel> {
    // derived from JPA
}
