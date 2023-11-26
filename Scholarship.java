import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;

public class Scholarship {

    private String name;
    private String description;
    private ArrayList<StudentProfile> applicants = new ArrayList<StudentProfile>();
    private DonorProfile donor;
    private float awardAmount;
    private HashMap<String, String> requirements = new HashMap<String, String>();
    private ArrayList<String> application = new ArrayList<String>();
    private boolean isArchived;
    private boolean isApproved;
    private boolean isAwarded;
    private StudentProfile recipient;

    // a LocalDate object (has a date but no time) for when this scholarship was added
    private LocalDate dateAdded;
    // a LocalDate object for when this scholarship is due. The scholarship is always due at the end 
    // of the day, so if today is the same date as dateDue, you can still apply
    private LocalDate dateDue;

    private int fileIndex;

    // TO DO: due date attribute and methods

    // constructor for donor typing details in
    public Scholarship(String name, String description, DonorProfile donor, float awardAmount,
            ArrayList<String> requirements, ArrayList<String> application, String dateAddedString, String dateDueString) {
        this.name = name;
        this.description = description;
        this.donor = donor;
        this.awardAmount = awardAmount;

        try {
            if (requirements.size() % 2 != 0) {
                throw new Exception(
                        "Uneven category-value pairings!\nPlease validate that every category name and desired value is present.");
            }

            this.requirements = InitializeRequirements(requirements);
        } catch (Exception except) {
            System.out.println(except.getMessage());
        }

        this.application = application;
        // default values, these didn't get passed in this specific constructor
        this.isApproved = false;
        this.isArchived = false;

        // the dateAdded and dateDue are stored as LocalDate objects in the
        // Scholarship, but we parse them (and get them as a parameter into this constructor)
        // as a String, so we have to parse the dates from the Strings
        this.dateAdded = LocalDate.parse(dateAddedString);
        this.dateDue = LocalDate.parse(dateDueString);
    }

    // constructor for loading data from files into program
    public Scholarship(String name, String description, DonorProfile donor, float awardAmount,
            ArrayList<String> requirements,
            ArrayList<String> application, ArrayList<StudentProfile> applicants, boolean isApproved,
            boolean isArchived, int fileIndex, String dateAddedString, String dateDueString) {
        this.name = name;
        this.description = description;
        this.donor = donor;
        this.awardAmount = awardAmount;

        try {
            if (requirements.size() % 2 != 0) {
                throw new Exception(
                        "Uneven category-value pairings!\nPlease validate that every category name and desired value is present.");
            }

            this.requirements = InitializeRequirements(requirements);
        } catch (Exception except) {
            System.out.println(except.getMessage());
        }

        this.application = application;
        this.applicants = applicants;
        this.isApproved = isApproved;
        this.isArchived = isArchived;
        this.fileIndex = fileIndex;

        // the dateAdded and dateDue are stored as LocalDate objects in the
        // Scholarship, but we parse them (and get them as a parameter into this constructor)
        // as a String, so we have to parse the dates from the Strings
        this.dateAdded = LocalDate.parse(dateAddedString);
        this.dateDue = LocalDate.parse(dateDueString);
    }

    //empty constructor for error mitigation purposes
    //TO DO: fill out
    public Scholarship() {

    }

    // initializes requirement categories and values into a hashmap
    public HashMap<String, String> InitializeRequirements(ArrayList<String> inputRequirements) {
        HashMap<String, String> requirements = new HashMap<String, String>();

        for (int i = 0; i < inputRequirements.size(); i = i + 2) {
            requirements.put(inputRequirements.get(i), inputRequirements.get(i + 1));
        }

        return requirements;
    }

    public void addApplicant(StudentProfile newStudent) {
        this.applicants.add(newStudent);
    }

    /**
     * This method is so we can check if a scholarship should be archived, 
     * we have a requirement that scholarships should be stored for 5 years past their due date.
     * And then our requirement doesn't specify what happens but we decided to archive them
     * 
     * @return returns true if this scholarship's due date is 5 years ago today, or earlier
     */
    public boolean due5PlusYearsAgo() {
        LocalDate todaysDate = LocalDate.now();
        
        // *****very important note: if you use Period to get the difference between dates 
        // that are e.g. 3 months and 2 days apart, and do getDays(), it will be 2 days

        // the time from the due date to today's date, can be either positive or negative
        Period timeSinceDue = Period.between(this.getDateDue(), todaysDate);

        // return true if it was due 5 years ago or longer than that
        // if it's 4 years and 364 days it's still ok, but after 5 years it should be archived
        return (timeSinceDue.getYears() >= 5);
    }

    /**
     * 
     * @return if the dateDue on this scholarship has passed, i.e. it was yesterday or before that.
     * Returns *false if the dateDue is today
     */
    public boolean isPastDue() {
        LocalDate todaysDate = LocalDate.now();

        // want to return true if the due date was yesterday or before that
        // not if it's today, since it's due tonight at midnight
        return dateDue.isBefore(todaysDate);
    }

    // getters

    public String getName() {
        return this.name;
    }

    public float getAwardAmount() {
        return this.awardAmount;
    }

    public DonorProfile getDonorProfile() {
        return this.donor;
    }

    public HashMap<String, String> getRequirements() {
        return this.requirements;
    }

    public ArrayList<StudentProfile> getApplicants() {
        return this.applicants;
    }

    public ArrayList<String> getApplicantNames() {
        ArrayList<String> applicantNames = new ArrayList<String>();

        for (StudentProfile student : this.applicants) {
            applicantNames.add(student.getName());
        }
        
        return applicantNames;
    }

    public ArrayList<String> getApplication() {
        return this.application;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsApproved() {
        return this.isApproved;
    }

    public boolean getIsArchived() {
        return this.isArchived;
    }

    public String getDonorName() {
        return this.donor.getName();
    }

    public int getFileIndex() {
        return this.fileIndex;
    }

    public boolean getIsAwarded() {
        return isAwarded;
    }

    public StudentProfile getRecipient() {
        return recipient;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public String getDateAddedString() {
        return this.dateAdded.toString();
    }

    public LocalDate getDateDue() {
        return this.dateDue;
    }

    public String getDateDueString() {
        return this.dateDue.toString();
    }


    // setters

    // TODO: ****check if all these auto-generated setters are good!!

    public void setName(String inputName) {
        this.name = inputName;
    }

    public void setAwardAmount(float inputAwardAmount) {
        this.awardAmount = inputAwardAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setApplicants(ArrayList<StudentProfile> applicants) {
        this.applicants = applicants;
    }

    // TODO: what to do for this???
    public void setDonor(DonorProfile donor) {
        this.donor = donor;
    }

    public void setRequirements(HashMap<String, String> requirements) {
        this.requirements = requirements;
    }

    public void setApplication(ArrayList<String> application) {
        this.application = application;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setAwarded(boolean isAwarded) {
        this.isAwarded = isAwarded;
    }

    public void setRecipient(StudentProfile recipient) {
        this.recipient = recipient;
    }

    public String getDetailsFileText() {
        return this.name + "\n"
        + this.description + "\n"
        + getDonorName() + "\n"
        + this.awardAmount + "\n"
        + this.isApproved + "\n"
        + this.isArchived;
    }

    public String getApplicationFileText() {
        String fileText = String.join("\n", this.application);

        return fileText;
    }

    public String getApplicantsFileText() {
        String fileText = String.join("\n", getApplicantNames());

        return fileText;
    }

    public String getRequirementsFileText() {
        ArrayList<String> requirementsList = new ArrayList<String>();

        for (HashMap.Entry<String, String> entry : this.requirements.entrySet()) {
            requirementsList.add(entry.getKey());
            requirementsList.add(entry.getValue());
        }
        
        String fileText = String.join("\n", requirementsList);

        return fileText;
    }

    // Adding this just in case we want to set the date from a LocalDate object,
    // but there is also the method below to set it from a String
    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateAdded(String dateAddedString) {
        this.dateAdded = LocalDate.parse(dateAddedString);
    }

    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }

    public void setDateDue(String dateDueString) {
        this.dateDue = LocalDate.parse(dateDueString);
    }

    public String getBasicInfoString() {
        String info;

        info = "Scholarship with ID #" + fileIndex + ": " + this.name + "\n";
	
        info += "Award amount: " + String.format("%.2f", this.awardAmount) + "\n";
		
		info += "Due at the end of the day on: " + this.getDateDueString() + ", (format is YYYY-MM-DD)";

        return info;
    }

    public String getAllInfoString() {
        String info;

        info = "Scholarship with ID #" + fileIndex + ": " + this.name + "\nDescription: " + this.description + "\n";
	
        info += "Award amount: " + String.format("%.2f", this.awardAmount) + "\n";
		
		info += "Due at the end of the day on: " + this.getDateDueString() + ", (format is YYYY-MM-DD)\n";

        info += "Requirements: {\n";

        for (Map.Entry<String, String> reqValuePair : this.requirements.entrySet()) {
			info += reqValuePair.getKey() + ": " + reqValuePair.getValue() + "\n";
		}

        info += "}\n" + "Donor: " + this.donor.getName() + "\n" + "Date posted: " + getDateAddedString();

        return info;
    }


    @Override
    public String toString() {
        return "Scholarship [name=" + name + ", description=" + description + ", applicants=" + applicants + ", Donor="
                + donor.getName() + ", awardAmount=" + awardAmount + ", requirements=" + requirements + ", application="
                + application + ", isArchived=" + isArchived + ", isApproved=" + isApproved + ", dateAdded=" + dateAdded + ", dateDue=" + dateDue + "]";
    }
}
