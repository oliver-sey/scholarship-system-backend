import java.util.HashMap;

public class MatchRelationship {
    private StudentProfile student;
    private Scholarship scholarship;
    private long matchPercentage;
    private long matchIndex;
    private HashMap<String, String> application = new HashMap<String, String>();
    private String applicationStatus;
    private HashMap<String, String> additionalRequirements = new HashMap<String, String>();
    
    //getters

    public long getMatchPercentage(){
        return this.matchPercentage;
    }

    public long getMatchIndex(){
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

	public void setMatchPercentage(long inputMatchPercentage) {
		this.matchPercentage = inputMatchPercentage;
    }

    public void setMatchIndex(long inputMatchIndex) {
		this.matchIndex = inputMatchIndex;
    }

    public void setApplicationStatus(String inputApplicationStatus) {
		this.applicationStatus = inputApplicationStatus;
    }

    public void setApplicationStatus(HashMap<String, String> inputApplication) {
        this.application = inputApplication;
    }

    public void setAdditionalRequirements(HashMap<String, String> inputAdditionalRequirements) {
        this.additionalRequirements = inputAdditionalRequirements;
    }

}
