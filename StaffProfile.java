
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

    // TO DO: add for fileIndex?
    public StaffProfile(String firstName, String lastName, String username, String password, String jobRole,
            String securityQAnswer1, String securityQAnswer2, String securityQAnswer3, int fileIndex) {
        clearanceLevel = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.jobRole = jobRole;
        this.securityQAnswers[0] = securityQAnswer1;
        this.securityQAnswers[1] = securityQAnswer2;
        this.securityQAnswers[2] = securityQAnswer3;
        this.fileIndex = fileIndex;
    }

    public String getJobRole() {
        return this.jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    // gets the details from the staff profile needed to enter on a file
    public String getDetailsFileText() {
        return this.firstName + "\n" +
                this.lastName + "\n" +
                this.username + "\n" +
                this.password + "\n" +
                this.jobRole + "\n" +
                this.securityQAnswers[0] + "\n" +
                this.securityQAnswers[1] + "\n" +
                this.securityQAnswers[2];
    }

}
