package videoclub.graphql.server.domain.videoclub;

import java.util.Date;
import java.util.UUID;

public class Customer {
    UUID id;
    String fullName;
    int dateOfBirth;
    String address;
    String phoneNumber;
    String email;

    public Customer(UUID id, String fullName, int dateOfBirth, String address, String phoneNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Customer(UUID id) {
        this.id = id;
        this.fullName = "fullName";
        this.dateOfBirth = 0;
        this.address = "address";
        this.phoneNumber = "phoneNumber";
        this.email = "email";
    }
}
