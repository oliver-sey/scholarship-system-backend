import java.util.ArrayList;

public class DonorProfile extends Profile{
    ArrayList<Scholarship> scholarships = new ArrayList<Scholarship>();

    public DonorProfile(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public void addScholarship(Scholarship newScholarship) {
        scholarships.add(newScholarship);
    }

    public ArrayList<Scholarship> getScholarships () {
        return this.scholarships;
    }
    

}
