package it.logistics.tracking.domain.model.parcel;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    public Address() {}

    public Address(String street, String postalCode, String city, String country) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    @Column(nullable = true, length = 100)
    private String street;
    @Column(nullable = true, length = 15)
    private String postalCode;
    @Column(nullable = true, length = 50)
    private String city;
    @Column(nullable = true, length = 100)
    private String country;

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            Address typedObject = (Address) object;
            equalObjects =
                    this.getStreet().equals(typedObject.getStreet()) &&
                    this.getPostalCode().equals(typedObject.getPostalCode()) &&
                    this.getCountry().equals(typedObject.getCountry()) &&
                    this.getCity().equals(typedObject.getCity());
        }

        return equalObjects;
    }

    @Override
    public String toString() {
        return "Address [street=" + street + ", postalCode=" + postalCode +
                ", city=" + city + ", country=" + country + "]";
    }

}
