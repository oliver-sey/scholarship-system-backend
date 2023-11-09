import java.util.ArrayList;
import java.util.HashMap;


public class MatchRelationship {
    private StudentProfile student;
    private Scholarship scholarship;
    private float matchPercentage;
    private float matchIndex;
    private HashMap<String, String> application = new HashMap<String, String>();
    private String applicationStatus;
    private HashMap<String, String> additionalRequirements = new HashMap<String, String>();
    
    //input string of application questions into a hashmap as the keys
    public HashMap<String, String> InitializeApplication(ArrayList<String> applicationQuestions) {
        HashMap<String, String> application = new HashMap<String, String>();
        for (int i = 0; i < applicationQuestions.size(); i++) {
            application.put(applicationQuestions.get(i), "");
        }
        return application;
    }

    public HashMap<String, String> UpdateApplication(ArrayList<String> applicationQandA) {
        HashMap<String, String> application = new HashMap<String, String>();
        
        for (int i = 0; i < applicationQandA.size(); i = i + 2) {
                application.put(applicationQandA.get(i), applicationQandA.get(i + 1));
        }

        return application;
    }

    //need method to compare scholarship requirements with student requirements

    //constructors
    public MatchRelationship(StudentProfile inputStudent, Scholarship inputScholarship, float inputMatchPercentage, float inputMatchIndex,
        ArrayList<String> inputApplication) {
            this.student = inputStudent;
            this.scholarship = inputScholarship;
            this.matchPercentage = inputMatchPercentage;
            this.matchIndex = inputMatchIndex;
            this.application = InitializeApplication(inputApplication);
            this.applicationStatus = "Not Started";
            //initialize additional requirements
    }

    //getters

    public float getMatchPercentage(){
        return this.matchPercentage;
    }

    public float getMatchIndex(){
        return this.matchIndex;
    }

    public String getApplicationStatus(){
        return this.applicationStatus;
    }

    public HashMap<String, String> getApplication() {
        return this.application;
    }

    public HashMap<String, String> getAdditionalRequirements() {
        return this.additionalRequirements;
    }

    //setters

	public void setMatchPercentage(float inputMatchPercentage) {
		this.matchPercentage = inputMatchPercentage;
    }

    public void setMatchIndex(float inputMatchIndex) {
		this.matchIndex = inputMatchIndex;
    }

    public void setApplicationStatus(String inputApplicationStatus) {
		this.applicationStatus = inputApplicationStatus;
    }

    public void setApplication(ArrayList<String> inputApplication) {
        try {
            if (inputApplication.size() % 2 != 0) {
                throw new Exception("Uneven answer-question pairings!\nPlease enter an empty string for any unanswered questions.");
            }
            
            this.application = UpdateApplication(inputApplication);
        }

        catch (Exception except) {
            System.out.println(except.getMessage());
        }
    }

    public void setAdditionalRequirements(HashMap<String, String> inputAdditionalRequirements) {
        this.additionalRequirements = inputAdditionalRequirements;
    }

}
