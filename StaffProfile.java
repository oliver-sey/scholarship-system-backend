public class StaffProfile extends Profile {
    private String jobRole;

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
