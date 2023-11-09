import java.util.ArrayList;
import java.util.HashMap;

public class Scholarship {
	private String name;
    private String description;
    private ArrayList<StudentProfile> applicants = new ArrayList<StudentProfile>();
    private DonorProfile Donor;
    private float awardAmount;
    private HashMap<String, String> requirements = new HashMap<String, String>();
    private ArrayList<String> application = new ArrayList<String>();

    public Scholarship(String name, String description, ArrayList<StudentProfile> applicants, DonorProfile donor,
            float awardAmount, ArrayList<String> requirements, ArrayList<String> application) {
        this.name = name;
        this.description = description;
        this.applicants = applicants;
        this.Donor = donor;
        this.awardAmount = awardAmount;

        try {
            if (requirements.size() % 2 != 0) {
                throw new Exception("Uneven category-value pairings!\nPlease validate that every category name and desired value is present.");
            }
            
            this.requirements = InitializeRequirements(requirements);
        }
        catch (Exception except) {
            System.out.println(except.getMessage());
        }
        
        this.application = application;
    }

    public HashMap<String, String> InitializeRequirements(ArrayList<String> inputRequirements) {
        HashMap<String, String> requirements = new HashMap<String, String>();

        for (int i = 0; i < inputRequirements.size(); i = i + 2) {
            requirements.put(inputRequirements.get(i), inputRequirements.get(i + 1));
        }
        
        return requirements;
    }

    //getters

    public String getName(){
		return this.name;
	}

    public float getAwardAmount() {
        return this.awardAmount;
    }

    public DonorProfile getDonarProfile(){
        return this.Donor;
    }

    public HashMap<String, String> getRequirments(){
        return this.requirements;
    }

    public ArrayList<StudentProfile> getApplicants(){ 
        return this.applicants;
    }

    public ArrayList<String> getApplication(){ 
        return this.application;
    }

    public String getDescription() {
        return description;
    }

    //setters

	public void setName(String inputName) {
		this.name = inputName;
	}

    public void setAwardAmount(float inputAwardAmount) {
        this.awardAmount = inputAwardAmount;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
