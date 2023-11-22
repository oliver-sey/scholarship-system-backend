public class StaffProfile extends Profile {
    private String jobRole;

    // create constructor for StaffProfile child class
    public StaffProfile() {
        clearanceLevel = 1;
        fileIndex = -1;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
    }
    
    //TO DO: add for fileIndex?
    public StaffProfile(String firstName, String lastName, String username, String password, String jobRole) {
        clearanceLevel = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.jobRole = jobRole;
    }

    public String getjobRole() {
        return this.jobRole;
    }

    public void setjobRole(String inputJobRole) {
		this.jobRole = inputJobRole;
	}
}
