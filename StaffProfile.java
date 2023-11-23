

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
        jobRole = "invalidJobRole";
        securityQAnswers[0] = "invalidSecurityQAnswer1";
		securityQAnswers[1] = "invalidSecurityQAnswer2";
		securityQAnswers[2] = "invalidSecurityQAnswer3";
    }
    
    //TO DO: add for fileIndex?
    public StaffProfile(String firstName, String lastName, String username, String password, String jobRole, String securityQAnswer1, String securityQAnswer2, String securityQAnswer3, int fileIndex) {
        clearanceLevel = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.jobRole = jobRole;
        this.securityQAnswers[0] = securityQAnswer1;
		this.securityQAnswers[1] = securityQAnswer2;
		this.securityQAnswers[2] = securityQAnswer3;
    }

    public String getjobRole() {
        return this.jobRole;
    }

    public void setjobRole(String inputJobRole) {
		this.jobRole = inputJobRole;
	}
}
