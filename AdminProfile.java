

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
    public AdminProfile(String firstName, String lastName, String username, String password, String securityQAnswer1, String securityQAnswer2, String securityQAnswer3, int fileIndex) {
        clearanceLevel = 4;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        securityQAnswers[0] = "invalidSecurityQAnswer1";
		securityQAnswers[1] = "invalidSecurityQAnswer2";
		securityQAnswers[2] = "invalidSecurityQAnswer3";
    }  

}
