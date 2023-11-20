public class FundStewardProfile extends Profile {
    // create constructor for FundSteward child class

    // TODO: do we need this? had red error squigglies for some reason
    // public FundStewardProfileProfile() {
    // clearanceLevel = 3;
    // firstName = "invalidFirstName";
    // lastName = "invalidLastName";
    // username = "invalidUserName";
    // password = "invalidPassword";
    // }

    public FundStewardProfile(String firstName, String lastName, String username, String password) {
        clearanceLevel = 3;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
}
