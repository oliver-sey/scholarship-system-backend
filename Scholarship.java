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
    private boolean isArchived;
    private boolean isApproved;

    // day month and year for when this scholarship was added
    // would be too complicated to mess around with date objects
    private int dayAdded;
    private int monthAdded;
    private int yearAdded;

    // for the scholarship due date
    // TODO: are scholarships always due end of day?
    private int dayDue;
    private int monthDue;
    private int yearDue;

    // TO DO: due date attribute and methods

    // constructor for donor typing details in
    public Scholarship(String name, String description, DonorProfile donor, float awardAmount,
            ArrayList<String> requirements,
            ArrayList<String> application) {
        this.name = name;
        this.description = description;
        this.Donor = donor;
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
        this.isApproved = false;
        this.isArchived = false;
    }

    // constructor for loading data from files into program
    public Scholarship(String name, String description, DonorProfile donor, float awardAmount,
            ArrayList<String> requirements,
            ArrayList<String> application, ArrayList<StudentProfile> applicants, boolean isApproved,
            boolean isArchived) {
        this.name = name;
        this.description = description;
        this.Donor = donor;
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
        return this.Donor;
    }

    public HashMap<String, String> getRequirements() {
        return this.requirements;
    }

    public ArrayList<StudentProfile> getApplicants() {
        return this.applicants;
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

    public DonorProfile getDonor() {
        return Donor;
    }

    public int getDayAdded() {
        return dayAdded;
    }

    public int getMonthAdded() {
        return monthAdded;
    }

    public int getYearAdded() {
        return yearAdded;
    }

    public int getDayDue() {
        return dayDue;
    }

    public int getMonthDue() {
        return monthDue;
    }

    public int getYearDue() {
        return yearDue;
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

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void setApplicants(ArrayList<StudentProfile> applicants) {
        this.applicants = applicants;
    }

    // TODO: what to do for this???
    public void setDonor(DonorProfile donor) {
        Donor = donor;
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

    public void setDayAdded(int dayAdded) {
        if (dayAdded < 1 || dayAdded > 31) {
            throw new IllegalArgumentException("dayAdded must be >= 1 and <= 31");
        }

        this.dayAdded = dayAdded;
    }

    public void setMonthAdded(int monthAdded) {
        if (monthAdded < 1 || monthAdded > 12) {
            throw new IllegalArgumentException("monthAdded must be >= 1 and <= 12");
        }

        this.monthAdded = monthAdded;
    }

    public void setYearAdded(int yearAdded) {
        if (yearAdded < 2000) {
            throw new IllegalArgumentException("yearAdded must be >= 2000");
        }

        this.yearAdded = yearAdded;
    }

    public void setDayDue(int dayDue) {
        if (dayDue < 1 || dayDue > 31) {
            throw new IllegalArgumentException("dayDue must be >= 1 and <= 31");
        }

        this.dayDue = dayDue;
    }

    public void setMonthDue(int monthDue) {
        if (monthDue < 1 || monthDue > 12) {
            throw new IllegalArgumentException("monthDue must be >= 1 and <= 12");
        }

        this.monthDue = monthDue;
    }

    public void setYearDue(int yearDue) {
        if (yearDue < 2000) {
            throw new IllegalArgumentException("yearDue must be >= 2000");
        }

        this.yearDue = yearDue;
    }

    @Override
    public String toString() {
        return "Scholarship [name=" + name + ", description=" + description + ", applicants=" + applicants + ", Donor="
                + Donor.getName() + ", awardAmount=" + awardAmount + ", requirements=" + requirements + ", application="
                + application + ", isArchived=" + isArchived + ", isApproved=" + isApproved + "]";
    }
}
