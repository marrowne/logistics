package it.logistics.tracking.application;

import it.logistics.tracking.domain.model.parcel.Courier;
import it.logistics.tracking.domain.model.parcel.Parcel;
import it.logistics.tracking.domain.model.parcel.SortingWorker;
import it.logistics.tracking.infrastructure.persistence.ParcelRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;


public class ParcelServiceTest {

    @Test
    public void testAddParcel() {
        // given
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        // when
        service.addParcel(new Parcel());

        // then
        verify(repository).save(any(Parcel.class));
    }

    @Test
    public void testFindParcel() {
        // given
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);
        when(service.findParcel(100000L)).thenReturn(mock(Parcel.class));

        // when
        Parcel parcel = service.findParcel(100000L);

        // then
        assertNotNull(parcel);
    }

    @Test
    public void testAllParcels() {
        // given
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        // when
        service.allParcels(mock(PageRequest.class));

        // then
        verify(repository).findAll(any(PageRequest.class));
    }

    @Test
    public void testAllParcelsQuery() {
        // given
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        // when
        service.allParcels(mock(PageRequest.class), "Query");

        // then
        verify(repository).findAll(any(Example.class), any(PageRequest.class));
    }

    @Test
    public void testAllParcelsFullText() {
        // given
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        // when
        service.allParcels(mock(PageRequest.class),
                "Query",
                true,
                new SortingWorker("100002L"));

        // then
        verify(repository).findAll(any(ParcelSpecification.class), any(PageRequest.class));
    }

    @Test
    public void testCourierReceivedWithIllegalArg() {
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        try {
            service.courierReceived(mock(Courier.class), 1000000L);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testSortingReceivedWithIllegalArg() {
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        try {
            service.sortingReceived(mock(SortingWorker.class), 1000000L);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testSortingLeftWithIllegalArg() {
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        try {
            service.sortingLeft(mock(SortingWorker.class), 1000000L);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testCourierDeliveredWithIllegalArg() {
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        try {
            service.courierDelivered(mock(Courier.class), 1000000L);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testCourierReturnedWithIllegalArg() {
        ParcelRepository repository = mock(ParcelRepository.class);
        ParcelService service = new ParcelServiceImpl(repository);

        try {
            service.courierReturned(mock(Courier.class), 1000000L);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {}
    }
}
