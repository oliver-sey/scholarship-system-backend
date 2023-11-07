public class DonorProfile extends Profile{
    //contact information - phone number and email
    private phoneNumber;
    private email;

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
