import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class StudentProfile extends Profile {
	/*
	 * "Requirement text: "The Back End system shall store the name, Student ID,
	 * selected major, selected minor if applicable,
	 * citizenship statuses, GPA, academic standing, grade level, expected
	 * graduation date, gender,
	 * student demographics, type of student, number of currently enrolled units,
	 * whether or not they already receive funding, and a 500 word maximum personal
	 * statement."
	 */

	// firstName and lastName are already in Profile

	// constructor with everything
	public StudentProfile(String firstName, String lastName, int studentID, String username, String password,
			String major, boolean hasAMinor, String minor, boolean isUSCitizen, float GPA,
			boolean inGoodStanding, boolean hasAdvStanding, String gradeLevel, int gradMonth, int gradYear,
			String gender, boolean isFullTimeStudent, boolean isTransferStudent, int curNumCredits,
			boolean receivesFunding, String personalStatement, int fileIndex) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentID = studentID;
		this.username = username;
		this.password = password;
		this.major = major;
		this.minor = minor;
		this.hasAMinor = hasAMinor;
		this.isUSCitizen = isUSCitizen;
		this.GPA = GPA;
		this.inGoodStanding = inGoodStanding;
		this.hasAdvStanding = hasAdvStanding;
		this.gradeLevel = gradeLevel;
		this.gradMonth = gradMonth;
		this.gradYear = gradYear;
		this.gender = gender;
		this.isFullTimeStudent = isFullTimeStudent;
		this.isTransferStudent = isTransferStudent;
		this.curNumCredits = curNumCredits;
		this.receivesFunding = receivesFunding;
		this.personalStatement = personalStatement;
		this.fileIndex = fileIndex;
	}

	public StudentProfile() {

	}

	private int studentID;
	private String major;
	private String minor;
	private boolean hasAMinor;

	// TODO: what do we do for citizenship statuses?
	private boolean isUSCitizen;

	private float GPA;

	private boolean inGoodStanding;
	private boolean hasAdvStanding;

	private String gradeLevel;

	private int gradMonth;
	private int gradYear;

	private String gender;

	// TODO: what do we do for 'student demographics'?
	private HashMap<String, String> extraDemographics = new HashMap<String, String>();

	// for "type of student"
	private boolean isFullTimeStudent;
	private boolean isTransferStudent;

	private int curNumCredits;

	// whether or not a student already receives scholarship funding
	private boolean receivesFunding;

	// a 500-word max personal statement
	private String personalStatement;

	private ArrayList<Integer> matchObjects = new ArrayList<Integer>();

	private ArrayList<Scholarship> awardsRecieved = new ArrayList<Scholarship>();

	// getters

	// a getter for studentID
	public int getStudentID() {
		return this.studentID;
	}

	// a getter for major
	public String getMajor() {
		return this.major;
	}

	// a getter for minor
	public String getMinor() {
		return this.minor;
	}

	// a getter for hasAMinor
	public boolean getHasAMinor() {
		return this.hasAMinor;
	}

	// a getter for boolean isUSCitizen
	public boolean getIsUSCitizen() {
		return this.isUSCitizen;
	}

	// a getter for GPA
	public float getGPA() {
		return this.GPA;
	}

	// a getter for inGoodStanding
	public boolean getInGoodStanding() {
		return this.inGoodStanding;
	}

	// a getter for hasAdvStanding
	public boolean getHasAdvStanding() {
		return this.hasAdvStanding;
	}

	// a getter for gradeLevel
	public String getGradeLevel() {
		return this.gradeLevel;
	}

	// a getter for gradMonth
	public int getGradMonth() {
		return this.gradMonth;
	}

	// a getter for gradYear
	public int getGradYear() {
		return this.gradYear;
	}

	// a getter for gender
	public String getGender() {
		return this.gender;
	}

	// a getter for isFullTimeStudent
	public boolean getIsFullTimeStudent() {
		return this.isFullTimeStudent;
	}

	// a getter for isTransferStudent
	public boolean getIsTransferStudent() {
		return this.isTransferStudent;
	}

	// a getter for curNumCredits
	public int getCurNumCredits() {
		return this.curNumCredits;
	}

	// a getter for receivesFunding
	public boolean getReceivesFunding() {
		return this.receivesFunding;
	}

	// a getter for personalStatement
	public String getPersonalStatement() {
		return this.personalStatement;
	}

	public ArrayList<Integer> getMatchObjectIds() {
		return this.matchObjects;
	}

	//a getter for awardsRecieved
	public ArrayList<Scholarship> getAwardsRecieved() {
		return this.awardsRecieved;
	}

	// a setter for boolean isUSCitizen
	public void getIsUSCitizen(boolean inputIsUSCitizen) {
		this.isUSCitizen = inputIsUSCitizen;
	}

	// setters
	// TODO: **** eventually add more to the setters to control what values can be
	// set!

	// a setter for int studentID
	public void setStudentID(int inputStudentID) {
		this.studentID = inputStudentID;
	}

	// a setter for String major
	public void setMajor(String inputMajor) {
		this.major = inputMajor;
	}

	// a setter for String minor
	public void setMinor(String inputMinor) {
		this.minor = inputMinor;
	}

	// a setter for boolean hasAMinor
	public void setHasAMinor(boolean inputHasAMinor) {
		this.hasAMinor = inputHasAMinor;
	}

	// a setter for float GPA
	public void setGPA(float inputGPA) {

		try {
			if (inputGPA > 4.0005 || inputGPA < 0) {
				throw new Exception("GPA entered is not valid. Please enter a number between 0-4");
			}

			this.GPA = inputGPA;
		}

		catch (Exception except) {
			System.out.println(except.getMessage());
		}

	}

	// a setter for boolean inGoodStanding
	public void setInGoodStanding(boolean inputInGoodStanding) {
		this.inGoodStanding = inputInGoodStanding;
	}

	// a setter for boolean hasAdvStanding
	public void setHasAdvStanding(boolean inputHasAdvStanding) {
		this.hasAdvStanding = inputHasAdvStanding;
	}

	// a setter for String gradeLevel
	public void setGradeLevel(String inputGradeLevel) {
		this.gradeLevel = inputGradeLevel;
	}

	// a setter for int gradMonth
	public void setGradMonth(int inputGradMonth) {
		if (inputGradMonth < 1 || inputGradMonth > 12) {
			throw new IllegalArgumentException("gradMonth must be >= 1 and <= 12");
		}

		this.gradMonth = inputGradMonth;
	}

	// a setter for int gradYear
	public void setGradYear(int inputGradYear) {
		this.gradYear = inputGradYear;
	}

	// a setter for String gender
	public void setGender(String inputGender) {
		this.gender = inputGender;
	}

	// a setter for boolean isFullTimeStudent
	public void setIsFullTimeStudent(boolean inputIsFullTimeStudent) {
		this.isFullTimeStudent = inputIsFullTimeStudent;
	}

	// a setter for boolean isTransferStudent
	public void setIsTransferStudent(boolean inputIsTransferStudent) {
		this.isTransferStudent = inputIsTransferStudent;
	}

	// a setter for int curNumCredits
	public void setCurNumCredits(int inputCurNumCredits) {
		// TODO: what is a reasonable lower and upper limit for a number of credits?
		if (inputCurNumCredits < 0 || inputCurNumCredits > 35) {
			throw new IllegalArgumentException("curNumCredits must be >= 0 and <= 35");
		}
		this.curNumCredits = inputCurNumCredits;
	}

	// a setter for boolean receivesFunding
	public void setReceivesFunding(boolean inputReceivesFunding) {
		this.receivesFunding = inputReceivesFunding;
	}

	// a setter for String personalStatement
	public void setPersonalStatement(String inputPersonalStatement) {
		// count the actual number of words, since the requirement said max 500 words
		int numWords = new StringTokenizer(inputPersonalStatement, " ").countTokens();

		if (numWords > 500) {
			throw new IllegalArgumentException(
					"personalStatement must be <= 500 words (gets counted as space-separated tokens)");
		}
		this.personalStatement = inputPersonalStatement;
	}

	public void setAwardsRecieved(ArrayList<Scholarship> awardsRecieved) {
        this.awardsRecieved = awardsRecieved;
    }

	public String getFileText() {
		return this.firstName + "\n" +
				this.lastName + "\n" +
				this.studentID + "\n" +
				this.username + "\n" +
				this.password + "\n" +
				this.major + "\n" +
				this.hasAMinor + "\n" +
				this.minor + "\n" +
				this.isUSCitizen + "\n" +
				this.GPA + "\n" +
				this.inGoodStanding + "\n" +
				this.hasAdvStanding + "\n" +
				this.gradeLevel + "\n" +
				this.gradMonth + "\n" +
				this.gradYear + "\n" +
				this.gender + "\n" +
				this.isFullTimeStudent + "\n" +
				this.isTransferStudent + "\n" +
				this.curNumCredits + "\n" +
				this.receivesFunding + "\n" +
				this.personalStatement + "\n";
	}

	@Override
	public String toString() {
		return "StudentProfile [studentID=" + studentID + ", major=" + major + ", minor=" + minor + ", hasAMinor="
				+ hasAMinor + ", isUSCitizen=" + isUSCitizen + ", GPA=" + GPA + ", inGoodStanding=" + inGoodStanding
				+ ", hasAdvStanding=" + hasAdvStanding + ", gradeLevel=" + gradeLevel + ", gradMonth=" + gradMonth
				+ ", gradYear=" + gradYear + ", gender=" + gender + ", extraDemographics=" + extraDemographics
				+ ", isFullTimeStudent=" + isFullTimeStudent + ", isTransferStudent=" + isTransferStudent
				+ ", curNumCredits=" + curNumCredits + ", receivesFunding=" + receivesFunding + ", personalStatement="
				+ personalStatement + "]";
	}

}
