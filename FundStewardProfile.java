public class FundStewardProfile extends Profile {
    // create constructor for FundSteward child class
    public FundStewardProfile() {
        clearanceLevel = 3;
        fileIndex = -1;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
    }

    //TO DO: add for fileIndex?
    public FundStewardProfile(String firstName, String lastName, String username, String password) {
        clearanceLevel = 3;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
}
