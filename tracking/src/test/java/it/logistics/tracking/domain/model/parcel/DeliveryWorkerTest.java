package it.logistics.tracking.domain.model.parcel;

import org.junit.Test;

public class DeliveryWorkerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testTooShortId() {
        final String ID = "10000";
        DeliveryWorker deliveryWorker = new Courier(ID);
        deliveryWorker.validateId(ID);
    }

    @Test()
    public void testCorrectId() {
        final String ID = "100000";
        DeliveryWorker deliveryWorker = new Courier(ID);
        deliveryWorker.validateId(ID);
    }

}