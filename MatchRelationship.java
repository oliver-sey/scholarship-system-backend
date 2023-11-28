import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchRelationship {
    private StudentProfile student;
    private Scholarship scholarship;
    private int ID;
    private float matchPercentage;
    private float matchIndex;
    private HashMap<String, String> application = new HashMap<String, String>();
    private String applicationStatus;
    private Boolean isActive;

    


    // input string of application questions into a hashmap as the keys
    public HashMap<String, String> InitializeApplication(ArrayList<String> applicationQuestions) {
        HashMap<String, String> application = new HashMap<String, String>();
        for (int i = 0; i < applicationQuestions.size(); i++) {
            application.put(applicationQuestions.get(i), "");
        }
        return application;
    }

    // update application hashmap with answer values
    public HashMap<String, String> UpdateApplication(ArrayList<String> applicationQandA) {
        HashMap<String, String> application = new HashMap<String, String>();

        for (int i = 0; i < applicationQandA.size(); i = i + 2) {
            if (applicationQandA.get(i + 1).equals("*")){
                application.put(applicationQandA.get(i), "");
            }
            else{
                application.put(applicationQandA.get(i), applicationQandA.get(i + 1));
            }
            
        }

        return application;
    }

    // initialize additional requirement questions hashmap with requirements as keys
    public HashMap<String, String> InitializeAdditionalRequirements(ArrayList<String> additionalRequirementQs) {
        HashMap<String, String> additionalRequirements = new HashMap<String, String>();

        for (int i = 0; i < additionalRequirementQs.size(); i++) {
            application.put(additionalRequirementQs.get(i), "");
        }

        return additionalRequirements;
    }

    public HashMap<String, String> UpdateAdditionalRequirements(ArrayList<String> additionalRequirementsQandA) {
        HashMap<String, String> additionalRequirements = new HashMap<String, String>();

        for (int i = 0; i < additionalRequirementsQandA.size(); i = i + 2) {
            additionalRequirements.put(additionalRequirementsQandA.get(i), additionalRequirementsQandA.get(i + 1));
        }

        return additionalRequirements;
    }

    // need method to compare scholarship requirements with student requirements

    // constructors

    //for typed input
    public MatchRelationship(StudentProfile inputStudent, Scholarship inputScholarship, float inputMatchPercentage,
            float inputMatchIndex, int ID) {
        this.student = inputStudent;
        this.scholarship = inputScholarship;
        this.matchPercentage = inputMatchPercentage;
        this.matchIndex = inputMatchIndex;
        this.application = InitializeApplication(inputScholarship.getApplication());
        this.applicationStatus = "not started";
        this.ID = ID;
        this.isActive = true;
    }

    //for file read input
    public MatchRelationship(StudentProfile inputStudent, Scholarship inputScholarship, int ID, float inputMatchPercentage,
            float inputMatchIndex, ArrayList<String> application, String applicationStatus, Boolean isActive) {
        this.student = inputStudent;
        this.scholarship = inputScholarship;
        this.ID = ID;
        this.matchPercentage = inputMatchPercentage;
        this.matchIndex = inputMatchIndex;
        this.application = UpdateApplication(application);
        this.applicationStatus = applicationStatus;
        this.isActive = isActive;
    }

    public MatchRelationship() {
        
    }

    // getters

    public float getMatchPercentage() {
        return this.matchPercentage;
    }

    public float getMatchIndex() {
        return this.matchIndex;
    }

    public String getApplicationStatus() {
        return this.applicationStatus;
    }

    public HashMap<String, String> getApplication() {
        return this.application;
    }

    public String getStudentName() {
        return this.student.getName();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getScholarshipName() {
        return this.scholarship.getName();
    }

    public int getID() {
        return this.ID;
    }

    public Scholarship getScholarship() {
        return this.scholarship;
    }

    // setters

    public void setMatchPercentage(float inputMatchPercentage) {
        this.matchPercentage = inputMatchPercentage;
    }

    public void setMatchIndex(float inputMatchIndex) {
        this.matchIndex = inputMatchIndex;
    }

    public void setApplicationToInProgress() {
        this.applicationStatus = "in progress";
    }

    public void setApplicationToSubmitted() {
        this.applicationStatus = "submitted";
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setApplication(ArrayList<String> inputApplication) {
        try {
            if (inputApplication.size() % 2 != 0) {
                throw new Exception(
                        "Uneven answer-question pairings!\nPlease enter an empty string for any unanswered questions.");
            }

            this.application = UpdateApplication(inputApplication);
        }

        catch (Exception except) {
            System.out.println(except.getMessage());
        }
    }

    public String getDetailsFileText() {
        return this.student.getName() + "\n"
        + this.scholarship.getName() + "\n"
        + this.matchPercentage + "\n"
        + this.matchIndex + "\n"
        + this.applicationStatus + "\n"
        + this.isActive;
    }

    public String getApplicationFileText() {
        ArrayList<String> applicationList = new ArrayList<String>();

        //pass string of line seperated values 
        //pass a star if answer is empty
        for (HashMap.Entry<String, String> entry : this.application.entrySet()) {
            applicationList.add(entry.getKey());
            if (entry.getValue() == "") {
                applicationList.add("*");
            }
            else {
                applicationList.add(entry.getValue());
            }
            
        }
        
        String fileText = String.join("\n", applicationList);

        return fileText;
    }

    public String getDetailsString() {
        String info = "Scholarship Name: " + getScholarshipName() + "\n";
        info += "Student Name: " + getStudentName() + "\n";

        info += "Match percentage: " + String.format("%.1f", this.matchPercentage) + "%\n";

        info += "Match Index: " + String.format("%.2f", this.matchIndex) + "\n";

        info += "Current application: \n";

        int qIndex = 1;
        for (Map.Entry<String, String> pair : this.application.entrySet()) {
            info += "Question " + qIndex + ": ";
            info += pair.getKey() + "\n";
            info += pair.getValue() + "\n";
            qIndex++;
        }

        info += "Status of application: " + this.applicationStatus;

        return info;
    }

}
