package it.logistics.tracking.domain.model.parcel;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FullNameTest {
    public static final String FIRSTNAME1 = "Jack";
    public static final String FIRSTNAME2 = "John";
    public static final String LASTNAME = "Doe";

    @Test
    public void testAddressEquality() {
        FullName fullName1 = new FullName(FIRSTNAME1, LASTNAME);
        FullName fullName2 = new FullName(FIRSTNAME1, LASTNAME);
        assertTrue(fullName1.equals(fullName2));
    }

    @Test
    public void testAddressInequality() {
        FullName fullName1 = new FullName(FIRSTNAME1, LASTNAME);
        FullName fullName2 = new FullName(FIRSTNAME2, LASTNAME);
        assertFalse(fullName1.equals(fullName2));
    }
}
