

public class AdminProfile extends Profile {
    // create constructor for AdminProfile child class
    public AdminProfile() {
        clearanceLevel = 4;
        fileIndex = -1;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
        securityQAnswers[0] = "invalidSecurityQAnswer1";
		securityQAnswers[1] = "invalidSecurityQAnswer2";
		securityQAnswers[2] = "invalidSecurityQAnswer3";
    }

    //TO DO: add for fileIndex?
    public AdminProfile(String firstName, String lastName, String username, String password, String securityQAnswer1, String securityQAnswer2, String securityQAnswer3, int fileIndex) {
        clearanceLevel = 4;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.securityQAnswers[0] = securityQAnswer1;
		this.securityQAnswers[1] = securityQAnswer2;
		this.securityQAnswers[2] = securityQAnswer3;
        this.fileIndex = fileIndex;
    }  

    //gets the details from the admin profile needed to enter on a file
    public String getDetailsFileText() {
        return this.firstName + "\n" +
        this.lastName + "\n" +
        this.username + "\n" +
        this.password + "\n" +
        this.securityQAnswers[0] + "\n" +
		this.securityQAnswers[1] + "\n" +
		this.securityQAnswers[2];
    }

}
