package it.logistics.tracking.domain.model.parcel;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AddressTest {
    public static final String STREET = "Miła 31/2";
    public static final String POSTALCODE = "40-423";
    public static final String CITY1 = "Ciche";
    public static final String CITY2 = "Olsztyn";
    public static final String COUNTRY = "Poland";

    @Test
    public void testAddressEquality() {
        Address address1 = new Address(STREET, POSTALCODE, CITY1, COUNTRY);
        Address address2 = new Address(STREET, POSTALCODE, CITY1, COUNTRY);
        assertTrue(address1.equals(address2));
    }

    @Test
    public void testAddressInequality() {
        Address address1 = new Address(STREET, POSTALCODE, CITY1, COUNTRY);
        Address address2 = new Address(STREET, POSTALCODE, CITY2, COUNTRY);
        assertFalse(address1.equals(address2));
    }

}
