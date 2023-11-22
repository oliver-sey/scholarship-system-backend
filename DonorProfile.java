import java.util.ArrayList;

public class DonorProfile extends Profile {
    ArrayList<Scholarship> scholarships = new ArrayList<Scholarship>();

    // create constructor for DonorProfile child class
    public DonorProfile() {
        clearanceLevel = 2;
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
    public DonorProfile(String firstName, String lastName, String username, String password, String securityQAnswer1, String securityQAnswer2, String securityQAnswer3) {
        clearanceLevel = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.securityQAnswers[0] = securityQAnswer1;
		this.securityQAnswers[1] = securityQAnswer2;
		this.securityQAnswers[2] = securityQAnswer3;
    }

    public void addScholarship(Scholarship newScholarship) {
        scholarships.add(newScholarship);
    }

    public ArrayList<Scholarship> getScholarships() {
        return this.scholarships;
    }

}
