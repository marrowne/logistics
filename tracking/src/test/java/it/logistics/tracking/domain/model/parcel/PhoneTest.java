package it.logistics.tracking.domain.model.parcel;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PhoneTest {
    public static final String FIRSTNAME1 = "12345678";
    public static final String FIRSTNAME2 = "12345679";

    @Test
    public void testAddressEquality() {
        Phone phone1 = new Phone(FIRSTNAME1);
        Phone phone2 = new Phone(FIRSTNAME1);
        assertTrue(phone1.equals(phone2));
    }

    @Test
    public void testAddressInequality() {
        Phone phone1 = new Phone(FIRSTNAME1);
        Phone phone2 = new Phone(FIRSTNAME2);
        assertFalse(phone1.equals(phone2));
    }
}
