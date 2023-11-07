import java.util.ArrayList;
import java.util.HashMap;

public class Scholarship {
	private String name;
    private ArrayList<StudentProfile> applicants = new ArrayList<StudentProfile>();
    private DonorProfile Donor;
    private float awardAmount;
    private HashMap<String, String> requirements = new HashMap<String, String>();
    private ArrayList<String> application = new ArrayList<String>();

    //getters

    public String getName(){
		return this.name;
	}

    public float getAwardAmount() {
        return this.awardAmount;
    }

    //setters

	public void setName(String inputName) {
		this.name = inputName;
	}

    public void setAwardAmount(float inputAwardAmount) {
        this.awardAmount = inputAwardAmount;
    }
}
