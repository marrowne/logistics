package it.logistics.tracking.port.adapter.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.logistics.tracking.domain.model.parcel.DeliveryWorker;

import java.lang.reflect.Constructor;

public class TrackingTranslator {

    public TrackingTranslator() {
        super();
    }

    public <T extends DeliveryWorker> T toDeliveryWorkerFromRepresentation(
            String deliveryWorkerRepresentation,
            Class<T> deliveryWorkerClass)
    throws Exception {

        JsonObject jsonObject = new JsonParser().parse(deliveryWorkerRepresentation).getAsJsonObject();

        String id = jsonObject.get("id").getAsString();
        String positionRepr = jsonObject.get("position").getAsString();


        T collaborator = null;

        String position;
        if (positionRepr.equals("SORTING")) {
            position = "SORTINGWORKER";
        } else {
            position = positionRepr;
        }

        if (deliveryWorkerClass.getSimpleName().toUpperCase().equals(position) || position.equals("ADMINISTRATOR")) {
            collaborator = this.newDeliveryWorker(id, deliveryWorkerClass);
        }

        return collaborator;
    }

    private <T extends DeliveryWorker> T newDeliveryWorker(String id, Class<T> deliveryWorkerClass)
    throws Exception {

        Constructor<T> constructor =
            deliveryWorkerClass.getConstructor(String.class);

        T collaborator =
            constructor.newInstance(id);

        return collaborator;
    }

}
