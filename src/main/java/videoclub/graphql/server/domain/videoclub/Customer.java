package videoclub.graphql.server.domain.videoclub;

import java.time.LocalDate;

/**
 * Defines a Customer object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class Customer {

    Integer id; // The customer's unique identifier (it is an integer).
    String fullName; // The customer's full name.
                     // If the customer has multiple first and/or last names, only one can be registered.
    LocalDate dateOfBirth; // The customer's date of birth.
    String address; // The full address where the customer lives.
                    // Can include any of: Address, Number, Area, City, Country, Area code, etc.
    String phoneNumber; // The customer's phone number in any format.
    String email; // The customer's e-mail address.

    /**
     * Builds a Customer object and sets its fields according to the arguments.
     * @param id The customer's unique identifier
     * @param fullName The customer's full name
     *                 <p> If the customer has multiple first and/or last names, only one can be registered.</p>
     * @param dateOfBirth The customer's date of birth.
     * @param address The full address where the customer lives.
     *                <p>Can include any of: Address, Number, Area, City, Country, Area code, etc.</p>
     * @param phoneNumber The customer's phone number.
     *                    <p>It can be in any format.</p>
     * @param email The customer's e-mail address.
     */
    public Customer(Integer id, String fullName, LocalDate dateOfBirth, String address, String phoneNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
