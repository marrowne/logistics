package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Receiver {

    public Receiver() {}

    public Receiver(FullName name, Phone phone, Address address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    @Embedded
    private FullName name;

    @Embedded
    private Phone phone;

    @Embedded
    private Address address;

    public FullName getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
                + (25183 * 441)
                        + this.getName().hashCode()
                        + this.getPhone().hashCode()
                        + this.getAddress().hashCode();

        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "Receiver [firstName=" + getName().getFirstName() + ", lastName=" + getName().getLastName() +
                         ", getNumber=" + getPhone().getNumber() +
                         ", street=" + getAddress().getStreet() + ", postalCode=" + getAddress().getPostalCode() +
                         ", city=" + getAddress().getCity() + ", country=" + getAddress().getCountry() + "]";
    }

}
