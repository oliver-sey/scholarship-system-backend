import java.util.ArrayList;

public class DonorProfile extends Profile {
    ArrayList<Scholarship> scholarships = new ArrayList<Scholarship>();

    // create constructor for DonorProfile child class
    public DonorProfile(){
        clearanceLevel = 2;
        fileIndex = -1;
        firstName = "invalidFirstName";
        lastName = "invalidLastName";
        username = "invalidUserName";
        password = "invalidPassword";   
    }

     //TO DO: add for fileIndex?
    public DonorProfile(String firstName, String lastName, String username, String password) {
        clearanceLevel = 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public void addScholarship(Scholarship newScholarship) {
        scholarships.add(newScholarship);
    }

    public ArrayList<Scholarship> getScholarships() {
        return this.scholarships;
    }

}
