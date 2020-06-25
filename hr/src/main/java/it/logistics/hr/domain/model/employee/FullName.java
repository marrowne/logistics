package it.logistics.hr.domain.model.employee;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class FullName {

    @Column(nullable = true, length = 100)
    private String firstName;

    @Column(nullable = true, length = 100)
    private String lastName;

    public FullName() {};

    public FullName(String aFirstName, String aLastName) {
        this.setFirstName(aFirstName);
        this.setLastName(aLastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object object) {
        boolean equalObjects = false;

        if (object != null && this.getClass() == object.getClass()) {
            FullName typedObject = (FullName) object;
            equalObjects =
                this.getFirstName().equals(typedObject.getFirstName()) &&
                this.getLastName().equals(typedObject.getLastName());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
            + (59151 * 191)
            + this.getFirstName().hashCode()
            + this.getLastName().hashCode();

        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "FullName [firstName=" + firstName + ", lastName=" + lastName + "]";
    }

    private void setFirstName(String firstName) {
        firstName = firstName.trim();
        if (firstName.equals("")) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (firstName.length() > 100) {

            throw new IllegalArgumentException("First name must be 100 characters or less.");
        }
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        lastName = lastName.trim();
        if (lastName.equals("")) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (lastName.length() > 100) {

            throw new IllegalArgumentException("Last name must be 100 characters or less.");
        }
        this.lastName = lastName;
    }

}
