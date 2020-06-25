package it.logistics.tracking.domain.model.parcel;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParcelTest {

    @Test
    public void testParcelDelivery() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Sender sender = mock(Sender.class);
        Receiver receiver = mock(Receiver.class);
        Courier courier = mock(Courier.class);
        DeliveryWorker created_by = mock(DeliveryWorker.class);
        Parcel parcel = new Parcel(sender, receiver, courier, created_by);

        assertNull(parcel.getStatus());
        callAddParcel(parcel);
        assertEquals(ParcelStatus.CREATED, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.PICKUP, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.SRC_SORTING, parcel.getStatus());
        parcel.sortingLeft(mock(SortingWorker.class));
        assertEquals(ParcelStatus.TRANSIT, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.DEST_SORTING, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.IN_DELIVERY, parcel.getStatus());
        parcel.courierDelivered(mock(Courier.class));
        assertEquals(ParcelStatus.DELIVERED, parcel.getStatus());
    }

    @Test
    public void testParcelReturnToSender() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Sender sender = mock(Sender.class);
        Receiver receiver = mock(Receiver.class);
        Courier courier = mock(Courier.class);
        DeliveryWorker created_by = mock(DeliveryWorker.class);
        Parcel parcel = new Parcel(sender, receiver, courier, created_by);

        assertNull(parcel.getStatus());
        callAddParcel(parcel);
        assertEquals(ParcelStatus.CREATED, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.PICKUP, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.SRC_SORTING, parcel.getStatus());
        parcel.sortingLeft(mock(SortingWorker.class));
        assertEquals(ParcelStatus.TRANSIT, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.DEST_SORTING, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.IN_DELIVERY, parcel.getStatus());
        parcel.courierReturned(mock(Courier.class));
        assertEquals(ParcelStatus.RETURNED, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_DEST_SORTING, parcel.getStatus());
        parcel.sortingLeft(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_TRANSIT, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_SRC_SORTING, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.RET_IN_DELIVERY, parcel.getStatus());
        parcel.courierDelivered(mock(Courier.class));
        assertEquals(ParcelStatus.RET_DELIVERED, parcel.getStatus());
    }

    @Test
    public void testParcelReturnReject() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Sender sender = mock(Sender.class);
        Receiver receiver = mock(Receiver.class);
        Courier courier = mock(Courier.class);
        DeliveryWorker created_by = mock(DeliveryWorker.class);
        Parcel parcel = new Parcel(sender, receiver, courier, created_by);

        assertNull(parcel.getStatus());
        callAddParcel(parcel);
        assertEquals(ParcelStatus.CREATED, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.PICKUP, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.SRC_SORTING, parcel.getStatus());
        parcel.sortingLeft(mock(SortingWorker.class));
        assertEquals(ParcelStatus.TRANSIT, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.DEST_SORTING, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.IN_DELIVERY, parcel.getStatus());
        parcel.courierReturned(mock(Courier.class));
        assertEquals(ParcelStatus.RETURNED, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_DEST_SORTING, parcel.getStatus());
        parcel.sortingLeft(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_TRANSIT, parcel.getStatus());
        parcel.sortingReceived(mock(SortingWorker.class));
        assertEquals(ParcelStatus.RET_SRC_SORTING, parcel.getStatus());
        parcel.courierReceived(mock(Courier.class));
        assertEquals(ParcelStatus.RET_IN_DELIVERY, parcel.getStatus());
        parcel.courierReturned(mock(Courier.class));
        assertEquals(ParcelStatus.RET_UNDELIVERED, parcel.getStatus());
    }

    @Test
    public void testAddParcelMock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        Sender sender = mock(Sender.class);
        Receiver receiver = mock(Receiver.class);
        Courier courier = mock(Courier.class);
        DeliveryWorker created_by = mock(DeliveryWorker.class);
        Parcel parcel = new Parcel(sender, receiver, courier, created_by);

        // when
        callAddParcel(parcel);
        String[] tokens = parcel.getSearchString().split("\\s+");

        // then
        assertEquals(tokens[0], "0");
    }

    @Test
    public void testAddParcelDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        Parcel dummyParcel = new Parcel();

        // when
        callAddParcel(dummyParcel);
        String[] tokens = dummyParcel.getSearchString().split("\\s+");

        // then
        assertEquals(tokens[0], "0");
    }

    @Test
    public void testAddParcelWithCountry() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // given
        String number = "123-456-789";
        Phone phone = mock(Phone.class);
        when(phone.getNumber()).thenReturn(number);
        Sender sender = new Sender(
                mock(FullName.class),
                phone,
                mock(Address.class));
        Receiver receiver = mock(Receiver.class);
        Courier courier = mock(Courier.class);
        DeliveryWorker created_by = mock(DeliveryWorker.class);
        Parcel dummyParcel = new Parcel(sender, receiver, courier, created_by);

        // when
        callAddParcel(dummyParcel);
        String[] tokens = dummyParcel.getSearchString().split("\\s+");

        // then
        assertEquals(tokens[0], "0");
        assertEquals(tokens[1], number);
    }

    private void callAddParcel(Parcel parcel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = parcel.getClass().getDeclaredMethod("addParcel");
        method.setAccessible(true);
        Object r = method.invoke(parcel);
    }

}
