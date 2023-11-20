public class DonorProfile extends Profile{
    //contact information - phone number and email
    private String phoneNumber;
    private String email;

    //create constructor for Donor child class
    public DonorProfile(){
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";
        phoneNumber = "0000000000";
        email = "N/A";
    }

    clearanceLevel = 2;
    
    public String getPhoneNumber() {
		return this.phoneNumber;
	}

    public String getEmail() {
        return this.email;
    }

    public void setPhoneNumber(String inputPhoneNumber) {
		this.phoneNumber = inputPhoneNumber;
	}

    public void setEmail(String inputEmail) {
		this.Email = inputEmail;
	}

}
