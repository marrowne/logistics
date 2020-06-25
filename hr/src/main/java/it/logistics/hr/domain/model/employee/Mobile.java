package it.logistics.hr.domain.model.employee;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Mobile {

    @Column(nullable = true, length = 20)
    private String number;

    public Mobile(String number) {
        this.setNumber(number);
    }

    public Mobile(Mobile mobile) {
        this(mobile.getNumber());
    }

    public String getNumber() {
        return this.number;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            Mobile typedObject = (Mobile) object;
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
        return "Mobile [getNumber=" + number + "]";
    }

    public Mobile() {};

    private void setNumber(String number) {
        number = number.trim();
        if (number.equals("")) {
            throw new IllegalArgumentException("Mobile getNumber is required.");
        }
        if (number.length() > 20) {
            throw new IllegalArgumentException("Mobile getNumber may not be more than 20 characters.");
        }
        this.number = number;
    }
}
