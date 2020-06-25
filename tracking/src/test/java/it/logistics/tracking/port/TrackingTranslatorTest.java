package it.logistics.tracking.port;

import it.logistics.tracking.domain.model.parcel.Courier;
import it.logistics.tracking.domain.model.parcel.SortingWorker;
import it.logistics.tracking.port.adapter.service.TrackingTranslator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TrackingTranslatorTest {

    @Test
    public void testCourierFromRepresentation() throws Exception {
        // given
        String courierUsername = "100001";
        String courierPosition = "COURIER";
        String parcelJSON = "{\"id\":\"" + courierUsername + "\", " +
                "\"position\": \"" + courierPosition + "\"}";

        // when
        TrackingTranslator trackingTranslator = new TrackingTranslator();
        Courier courier = trackingTranslator
                .toDeliveryWorkerFromRepresentation(parcelJSON, Courier.class);

        // then
        assertEquals(courier.getId(), courierUsername);
    }

    @Test
    public void testSortingWorkerFromRepresentation() throws Exception {
        // given
        String sortingUsername = "100002";
        String sortingPosition = "SORTING";
        String parcelJSON = "{\"id\":\"" + sortingUsername + "\", " +
                "\"position\": \"" + sortingPosition + "\"}";

        // when
        TrackingTranslator trackingTranslator = new TrackingTranslator();
        SortingWorker courier = trackingTranslator
                .toDeliveryWorkerFromRepresentation(parcelJSON, SortingWorker.class);

        // then
        assertEquals(courier.getId(), sortingUsername);
    }

}
