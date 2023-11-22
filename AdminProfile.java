public class AdminProfile extends Profile {
    // create constructor for AdminProfile child class
    public AdminProfile() {
        clearanceLevel = 4;
        fileIndex = -1;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
    }

    //TO DO: add for fileIndex?
    public AdminProfile(String firstName, String lastName, String username, String password) {
        clearanceLevel = 4;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }  

}
