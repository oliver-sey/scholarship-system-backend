public class FundStewardProfile extends Profile {
    clearanceLevel = 3;

    //create constructor for FundSteward child class
   public FundStewardProfileProfile() {
    firstName = "invalidFirstName";
    lastName = "invalidLastName";
    username = "invalidUserName";
    password = "invalidPassword";
`   }

    public FundStewardProfile(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

}
