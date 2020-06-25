package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Sender {

    public Sender() {}

    public Sender(FullName name, Phone phone, Address address) {
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
                + (85631 * 579)
                        + this.getName().hashCode()
                        + this.getPhone().hashCode()
                        + this.getAddress().hashCode();

        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "Sender [firstName=" + getName().getFirstName() + ", lastName=" + getName().getLastName() +
                ", getNumber=" + getPhone().getNumber() +
                ", street=" + getAddress().getStreet() + ", postalCode=" + getAddress().getPostalCode() +
                ", city=" + getAddress().getCity() + ", country=" + getAddress().getCountry() + "]";
    }

}
