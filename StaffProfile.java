public class StaffProfile extends Profile {
   private String jobRole;
   

   //create constructor for Staff child class
   public StaffProfile() {
      clearanceLevel = 1;
    firstName = "invalidFirstName";
    lastName = "invalidLastName";
    username = "invalidUserName";
    password = "invalidPassword";
    jobRole = "noJobListed";
   }

   public String getjobRole() {
    return this.jobRole;
    } 

    public void setjobRole(String inputJobRole) {
		this.jobRole = inputJobRole;
	}
}
