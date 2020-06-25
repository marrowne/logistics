package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Phone {

    @Column(nullable = true, length = 20)
    private String number;

    public Phone() {};

    public Phone(String number) {
        this.setNumber(number);
    }

    public Phone(Phone phone) {
        this(phone.getNumber());
    }

    public String getNumber() {
        return this.number;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            Phone typedObject = (Phone) object;
            equalObjects = this.getNumber().equals(typedObject.getNumber());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
                + (35137 * 239)
                        + this.getNumber().hashCode();

        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "Phone [getNumber=" + number + "]";
    }

    private void setNumber(String number) {
        number = number.trim();
        if (number.equals("")) {
            throw new IllegalArgumentException("Phone getNumber is required.");
        }
        if (number.length() > 20) {
            throw new IllegalArgumentException("Phone getNumber may not be more than 20 characters.");
        }
        this.number = number;
    }

}
