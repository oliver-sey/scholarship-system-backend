import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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


    @Override
    public String toString() {
        return "Scholarship [name=" + name + ", description=" + description + ", applicants=" + applicants + ", Donor="
                + donor.getName() + ", awardAmount=" + awardAmount + ", requirements=" + requirements + ", application="
                + application + ", isArchived=" + isArchived + ", isApproved=" + isApproved + ", dateAdded=" + dateAdded + ", dateDue=" + dateDue + "]";
    }
}
