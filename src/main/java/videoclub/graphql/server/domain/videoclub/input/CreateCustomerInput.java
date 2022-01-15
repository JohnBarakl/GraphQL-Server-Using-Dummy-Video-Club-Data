package videoclub.graphql.server.domain.videoclub.input;

import java.time.LocalDate;

/**
 * The object equivalent to the input type of the same name used to accept input for the purposes
 * of creating a new customer.
 */
public class CreateCustomerInput {

    /** The customer's full name. */
    String fullName;

    /** The customer's date of birth. */
    LocalDate dateOfBirth;

    /** The full address where the customer lives */
    String address;

    /** The customer's phone number in any format. */
    String phoneNumber;

    /** The customer's e-mail address. */
    String email;

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
