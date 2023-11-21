import java.util.ArrayList;

public class DonorProfile extends Profile {
    // contact information - phone number and email
    private String phoneNumber;
    private String email;

    // create constructor for Donor child class
    public DonorProfile() {
        clearanceLevel = 2;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
        phoneNumber = "0000000000";
        email = "N/A";
    }

    public DonorProfile(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhoneNumber(String inputPhoneNumber) {
        this.phoneNumber = inputPhoneNumber;
    }

    public void setEmail(String inputEmail) {
        this.email = inputEmail;
    }

}
